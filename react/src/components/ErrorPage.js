import React from 'react';
import i18next from 'i18next';
import { Link } from 'react-router-dom';

const getErrorMessage = (status) => {
    if (status === 404) {
        return i18next.t('errorPage.404');
    }
    else {
        return "";
        //TODO make more default messages for diferent errors
    }
}


const ErrorPage = ({ status }) => {
    const errorStatus = status ? status : 404;
    const errorMessage = getErrorMessage(status);
    return (
        <div >
            <h1> Error {errorStatus} </h1>
            <p>{errorMessage}</p>
            <p>
                <Link to="/">
                    {i18next.t('errorPage.returnToHome')}
                </Link>
            </p>
        </div>
        //TODO improve layout similar to github
    );
}

export default ErrorPage;