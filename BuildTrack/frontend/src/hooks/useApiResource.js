import { useCallback, useEffect, useState } from 'react';
import axios from 'axios';
import api from '../api/axiosInstance';
import { apiErrorMessage } from '../api/apiErrorMessage';

async function fetchResource(endpoint, responseType, signal) {
  const response = await api.get(endpoint, { signal });
  const payload = responseType === 'page' ? response.data?.content : response.data;

  if (responseType === 'object') {
    if (!payload || typeof payload !== 'object' || Array.isArray(payload)) {
      throw new Error('The server returned an unexpected response.');
    }
  } else if (!Array.isArray(payload)) {
    throw new Error('The server returned an unexpected list response.');
  }

  return payload;
}

export default function useApiResource(endpoint, responseType = 'list') {
  const [data, setData] = useState(responseType === 'object' ? null : []);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = useCallback(async (signal) => {
    try {
      const payload = await fetchResource(endpoint, responseType, signal);
      setError('');
      setData(payload);
    } catch (requestError) {
      if (signal?.aborted || axios.isCancel(requestError)) return;
      setError(apiErrorMessage(requestError));
    } finally {
      if (!signal?.aborted) setLoading(false);
    }
  }, [endpoint, responseType]);

  const reload = useCallback(() => {
    setLoading(true);
    setError('');
    return load();
  }, [load]);

  useEffect(() => {
    const controller = new AbortController();
    fetchResource(endpoint, responseType, controller.signal)
      .then((payload) => {
        if (controller.signal.aborted) return;
        setError('');
        setData(payload);
      })
      .catch((requestError) => {
        if (controller.signal.aborted || axios.isCancel(requestError)) return;
        setError(apiErrorMessage(requestError));
      })
      .finally(() => {
        if (!controller.signal.aborted) setLoading(false);
      });
    return () => controller.abort();
  }, [endpoint, responseType]);

  return { data, loading, error, reload };
}
