import React from 'react';
import PropTypes from 'prop-types';
import AuthService from '../../../services/AuthService';
import ChangePasswordForm from '../../forms/ChangePasswordForm';

const ChangePassword = (props) => {
    const currentUser = AuthService.getCurrentUser();
    const { username } = props.match.params;
    const needsPermission = !currentUser || currentUser !== username;
    return (
        <ChangePasswordForm initialValues={{ "username": username }} 
                            history={props.history}
                            needsPermission={needsPermission} />
    );
}

ChangePassword.propTypes = {
    match: PropTypes.object.isRequired
}

export default ChangePassword;