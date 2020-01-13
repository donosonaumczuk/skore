import React from 'react';
import { Redirect } from 'react-router-dom';
import AuthService from './../services/AuthService';

const LogOut = () => {
    const currentUser = AuthService.getCurrentUser();
    if(currentUser) {
        AuthService.logOutUser();
    //TODO validate errors
    }
    return(<Redirect to="/" />);
}

export default LogOut;