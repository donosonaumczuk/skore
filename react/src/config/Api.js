import { create } from 'axios';
import AuthService from './../services/AuthService';

const api = create({ baseURL: `${process.env.REACT_APP_BASE_URL}/api/`});

// Intercepting requests
api.interceptors.request.use(
    config => {
        const token = AuthService.getToken();
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

const errorHandler = (error) => {
    return Promise.reject({ ...error })
}
  
const successHandler = (response) => response;

// Intercepting responses
api.interceptors.response.use(response => successHandler(response),
                                error => errorHandler(error));

export default api;