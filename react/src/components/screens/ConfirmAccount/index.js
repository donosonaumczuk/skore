import React from 'react';
import { Redirect } from 'react-router-dom';
import i18next from 'i18next';
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
                    <h1>{i18next.t('confirmAccount.confirmed')}</h1>
                    <h2>{i18next.t('confirmAccount.confirmEmail')}</h2>
                </div>
            </div>
        </div>
    );
}

export default ConfirmAccount;