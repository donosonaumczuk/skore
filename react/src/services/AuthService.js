import api from './../config/Api';
import { AUTH_ENDPOINT } from './constants/EndpointConstants';
import { SC_OK, SC_UNAUTHORIZED, SC_TIME_OUT } from './constants/StatusCodesConstants';

const setToken = token => localStorage.setItem('jwt', token);

const getToken = () => localStorage.getItem('jwt');

const removeToken = () => localStorage.removeItem('jwt');

const loadUser = (username, isAdmin, userId) => {
    const user = {
        username: username,
        isAdmin: isAdmin,
        userId: userId,
    };
    localStorage.setItem('currentUser', JSON.stringify(user));
}

const removeUser = () => localStorage.removeItem('currentUser');

const getUser = () => localStorage.getItem('currentUser');

const logInUser = async user => {
    try {
        const response = await api.post(`${AUTH_ENDPOINT}/login`, user);
        setToken(response.headers['x-token']);
        loadUser(user.username, response.data.admin, response.data.userId);
        return { "status": SC_OK };
    }
    catch(err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }

    //TODO if error of token expiry remove token and user and catch other errors
    //TODO token expiry should be controlled on every get or post that uses authentication
}

const autoLogin = (token, username, userId) => {
    setToken(token);
    loadUser(username, false, userId);
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
        if (!err.response || err.response.status === SC_UNAUTHORIZED) {
            removeToken();
            removeUser();
        }
        else {
            return { "status": err.response.status };
        }
    }
}

const getCurrentUser = () => {
    if (getToken()) {
        const user = JSON.parse(getUser());
        return user.username;
    }
    return null;
}

const isAdmin = () => {
    if (getToken()) {
        const user = JSON.parse(getUser());
        return user.isAdmin;
        // return user.isAdmin === "true" ? true : false;
    }
    return null;
}

const getUserId = () => {
    if (getToken()) {
        const user = JSON.parse(getUser());
        return user.userId;
    }
    return null;
}

const AuthService = {
    getToken: getToken,
    logInUser: logInUser,
    autoLogin: autoLogin,
    logOutUser: logOutUser,
    getCurrentUser: getCurrentUser,
    isAdmin: isAdmin,
    getUserId: getUserId
};

export default AuthService;