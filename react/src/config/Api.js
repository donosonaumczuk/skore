import { create } from 'axios';

const api = create({ baseURL: '/api/'});

// TODO good for debuging intercepting all requests
// Intercepting requests
api.interceptors.request.use(
    config => {
        console.log(`${config.method.toUpperCase()} request sent to ${config.baseURL}${config.url}`);
        console.log(config);
        console.table(config);
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);


export default api;