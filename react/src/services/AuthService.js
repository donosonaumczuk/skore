import React from 'react';
import api from './../config/Api';

const setToken = token => localStorage.setItem('jwt', token);

const getToken = () => localStorage.getItem('jwt');

const removeToken = () => localStorage.removeItem('jwt');

const loadUser = user => localStorage.setItem('currentUser', user);

const removeUser = localStorage.removeItem('currentUser');

const getUser = () => localStorage.getItem('currentUser');

const logInUser = async user => {
    const response = await api.post("login", user);
    console.table(response);
    console.log(response);
    //TODO get token, load token, load user add interceptor to post catch error if any
}

const logOutUser = () => {
    if(getToken) {
        removeToken();
        removeUser(); 
        //TODO disable interceptor   
    }
}

const getCurrentUser = () => {
    if(getToken()) {
        return getUser();
    }
    return null;
}

const AuthService = {
    logInUser: logInUser,
    logOutUser: logOutUser,
    getCurrentUser: getCurrentUser
};

export default AuthService;