import api from './../config/Api';
import { AUTH_ENDPOINT } from './constants/EndpointConstants';
import { SC_OK, SC_UNAUTHORIZED } from './constants/StatusCodesConstants';

const setToken = token => localStorage.setItem('jwt', token);

const getToken = () => localStorage.getItem('jwt');

const removeToken = () => localStorage.removeItem('jwt');

const loadUser = user => localStorage.setItem('currentUser', user);

const removeUser = () => localStorage.removeItem('currentUser');

const getUser = () => localStorage.getItem('currentUser');

const logInUser = async user => {
    try {
        const response = await api.post(`${AUTH_ENDPOINT}/login`, user);
        setToken(response.headers['x-token']);
        loadUser(user.username);
        return { "status": SC_OK };
    }
    catch(err) {
        return { "status": err.response.status }
    }

    //TODO if error of token expiry remove token and user and catch other errors
    //TODO token expiry should be controlled on every get or post that uses authentication
}

const logOutUser = async () => {
    try {
        await api.post(`${AUTH_ENDPOINT}/logout`);
        if (getToken) {
            removeToken();
            removeUser();  
        }
        return { "status": SC_OK };
    }
    catch(err) {
        if (err.response.status === SC_UNAUTHORIZED) {
            removeToken();
            removeUser();
        }
        else {
            return { "status": err.response.status };
        }
    }
    
    // console.log(response); //TODO remove on production is here to avoid warning
    //TODO handle errors using response
}

const getCurrentUser = () => {
    if (getToken()) {
        return getUser();
    }
    return null;
}

const AuthService = {
    getToken: getToken,
    logInUser: logInUser,
    logOutUser: logOutUser,
    getCurrentUser: getCurrentUser
};

export default AuthService;