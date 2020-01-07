import { create } from 'axios';

const api = create({ baseURL: '/api/' });


// Intercepting requests
api.interceptors.request.use(
    config => {
        console.log(`${config.method.toUpperCase()} request sent to ${config.baseURL}${config.url}`);
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);
export default api;