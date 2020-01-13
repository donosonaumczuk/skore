import { create } from 'axios';
import AuthService from './../services/AuthService';

const api = create({ baseURL: '/api/'});

// TODO good for debuging intercepting all requests
// Intercepting requests
api.interceptors.request.use(
    config => {
        const token = AuthService.getToken();
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
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

api.interceptors.response.use(response => successHandler(response),
                                error => errorHandler(error));

export default api;