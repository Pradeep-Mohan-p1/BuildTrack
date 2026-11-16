import axios from 'axios';

const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: `${baseURL}/api/v1`,
  headers: {
    'Content-Type': 'application/json'
  }
});

api.interceptors.request.use((config) => {
  const sessionValue = sessionStorage.getItem('buildtrack_auth');

  if (sessionValue) {
    try {
      const { email, password } = JSON.parse(sessionValue);
      if (email && password) {
        config.headers.Authorization = `Basic ${btoa(`${email}:${password}`)}`;
      }
    } catch (error) {
      console.error('Failed to read auth session:', error);
    }
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 403) {
      console.warn('Forbidden: backend rejected the action.');
    }

    if (error.response?.status === 401) {
      console.warn('Unauthorized: please check your credentials.');
    }

    return Promise.reject(error);
  }
);

export default api;
