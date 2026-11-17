import axios from 'axios';

const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
const publicEndpoints = ['/auth/login', '/auth/register', '/users/register'];

const api = axios.create({
  baseURL: `${baseURL.replace(/\/+$/, '')}/api/v1`,
  headers: {
    'Content-Type': 'application/json'
  }
});

api.interceptors.request.use((config) => {
  const token = sessionStorage.getItem('buildtrack_token');

  if (token && !publicEndpoints.includes(config.url)) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const requestUrl = error.config?.url || '';
    const isPublicRequest = publicEndpoints.some((endpoint) => requestUrl.endsWith(endpoint));

    if (error.response?.status === 401 && !isPublicRequest) {
      sessionStorage.removeItem('buildtrack_token');
      window.dispatchEvent(new Event('buildtrack:unauthorized'));
    }

    return Promise.reject(error);
  }
);

export default api;
