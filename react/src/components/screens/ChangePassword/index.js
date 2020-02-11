import React from 'react';
import { Redirect } from 'react-router-dom';
import PropTypes from 'prop-types';
import AuthService from '../../../services/AuthService';
import ChangePasswordForm from '../../forms/ChangePasswordForm';

const ChangePassword = (props) => {
    const currentUser = AuthService.getCurrentUser();
    const { username } = props.match.params;
    if (!currentUser) {
        //TODO maybe render error page with 403 forbidden
        return <Redirect to="/" />
    }
    else if (currentUser !== username) {
        //TODO maybe render error page with 403 forbidden
        return <Redirect to={`/users/${currentUser}/editUserInfo`} />
    }
    return (
        <ChangePasswordForm initialValues={{ "username": username }} history={props.history} />
    );
}

ChangePassword.propTypes = {
    match: PropTypes.object.isRequired
}

export default ChangePassword;