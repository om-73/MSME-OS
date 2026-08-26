import axios from 'axios';

export const getApiBaseUrl = (): string => {
  const savedUrl = localStorage.getItem('mfgos_api_url');
  if (savedUrl && savedUrl.trim().length > 0) {
    return savedUrl.trim().replace(/\/+$/, '');
  }
  return (import.meta.env.VITE_API_URL || 'http://localhost:8081/api/v1').replace(/\/+$/, '');
};

export const API_BASE_URL = getApiBaseUrl();

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor: injects active user JWT, Tenant ID, and dynamic baseURL
api.interceptors.request.use((config) => {
  config.baseURL = getApiBaseUrl();
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
