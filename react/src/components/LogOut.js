import React from 'react';
import { Redirect } from 'react-router-dom';
import Proptypes from 'prop-types';
import AuthService from './../services/AuthService';

const LogOut = (props) => {
    const currentUser = AuthService.getCurrentUser();
    if (currentUser) {
        AuthService.logOutUser();
        props.updateUser(null);
    //TODO validate errors
    }
    return (<Redirect to="/" />);
}

LogOut.propTypes = {
    updateUser: Proptypes.func.isRequired
}

export default LogOut;