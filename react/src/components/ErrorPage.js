import React from 'react';
import i18next from 'i18next';
import { Link } from 'react-router-dom';
import { SC_NOT_FOUND } from './../services/constants/StatusCodesConstants';

const getErrorMessage = (status) => {
    if (status === SC_NOT_FOUND) {
        return i18next.t('errorPage.404');
    }
    else {
        return "";
        //TODO make more default messages for diferent errors
    }
}


const ErrorPage = ({ status }) => {
    const errorStatus = status ? status : SC_NOT_FOUND;
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