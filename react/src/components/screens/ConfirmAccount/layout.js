import React from 'react';

const ConfirmAccount = () => {
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
    //TODO link to profile
}