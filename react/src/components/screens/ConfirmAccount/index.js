import React from 'react';
import { Redirect } from 'react-router-dom';
import AuthService from '../../../services/AuthService';

const ConfirmAccount = () => {
    //TODO improve layout
    if (AuthService.getCurrentUser()) {
        return <Redirect to="/" />
    }
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid profile-container bg-white rounded-border alert alert-info alert-dismissible fade show mt-1">
                    <h1>Congratulations!! You have created a new account.</h1>
                    <h2>An email has been sent to you to confirm your account.</h2>
                </div>
            </div>
        </div>
    );
}

export default ConfirmAccount;