import React from 'react';
import { Redirect } from 'react-router-dom';
import AuthService from '../services/AuthService';
import ChangePasswordForm from './forms/ChangePasswordForm';


const ChangePassword = (props) => {
    const currentUser = AuthService.getCurrentUser();
    const { username } = props.match.params;
    if (!currentUser) {
        //TODO maybe render erro page with unauthorize instead of redirecting
        return <Redirect to="/" />
    }
    else if (currentUser !== username) {
        //TODO maybe render error page with unauthorize instead of redirecting
        return <Redirect to={`/users/${currentUser}/editUserInfo`} />
    }
    return (
        <ChangePasswordForm initialValues={{ "username": username }} />
    );
}

export default ChangePassword;