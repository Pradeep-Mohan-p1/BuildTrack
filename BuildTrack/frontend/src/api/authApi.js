import api from './axiosInstance';

export const loginUser = (email, password) =>
  api.get('/auth/me', {
    auth: { username: email, password }
  });

export const registerUser = ({ username, email, password }) =>
  api.post('/auth/register', {
    username,
    email,
    password
  });

export const getCurrentUser = () => api.get('/auth/me');
