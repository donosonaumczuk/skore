import { create } from 'axios';
import AuthService from './../services/AuthService';

// const api = create({ baseURL: '/api/'});//TODO remove on deploy
const api = create({ baseURL: '/paw-2018b-04/api/'});

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

const isHandlerEnabled = errorConfig => {
    // TODO dispatch to specific error handler depending on config
}

const errorHandler = (error) => {
    if (isHandlerEnabled(error.config)) {
      //TODO implement handle errors with specific status, disable token if expiry
      // and redirect to /login if unauthorized
      // Handle errors
    }
    return Promise.reject({ ...error })
  }
  
const successHandler = (response) => response;

// Intercepting responses
api.interceptors.response.use(response => successHandler(response),
                                error => errorHandler(error));

export default api;