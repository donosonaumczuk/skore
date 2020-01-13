import { create } from 'axios';
import AuthService from './../services/AuthService';

const api = create({ baseURL: '/api/'});

// TODO good for debuging intercepting all requests
// Intercepting requests
api.interceptors.request.use(
    config => {
        const token = AuthService.getToken();
        if (token) {
            config.headers.authorization =`Bearer ${token}`;
        }
        else {
            console.log("no agregar header");
        }
        // console.log(`${config.method.toUpperCase()} request sent to ${config.baseURL}${config.url}`);
        // console.log(config);
        // console.table(config);
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);


export default api;