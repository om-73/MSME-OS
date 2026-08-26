import axios from 'axios';

export const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081/api/v1';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor: injects active user JWT and Tenant ID
api.interceptors.request.use((config) => {
  try {
    const userJson = localStorage.getItem('mfgos_user');
    if (userJson) {
      const user = JSON.parse(userJson);
      if (user?.token) {
        config.headers['Authorization'] = `Bearer ${user.token}`;
      }
      if (user?.tenantId) {
        config.headers['X-Tenant-ID'] = user.tenantId;
      }
    }
  } catch (e) {
    console.error('Failed to parse user from localStorage in API interceptor', e);
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

// Response interceptor: handle session invalidation
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      console.warn('Session expired or unauthorized request. Redirecting to login.');
      localStorage.removeItem('mfgos_user');
    }
    return Promise.reject(error);
  }
);

export default api;
