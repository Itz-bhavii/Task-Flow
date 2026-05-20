import axios from 'axios';

const api = axios.create({
  baseURL: 'https://task-flow-production-ff0a.up.railway.app',
});

// Automatically add JWT token to every request if it exists.
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    if (!config.headers) {
      config.headers = {};
    }
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
