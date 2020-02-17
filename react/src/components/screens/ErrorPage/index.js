import React, { Component } from 'react';
import i18next from 'i18next';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import notFoundGif from './../../../img/404.gif';
import serverErrorGif from './../../../img/500.gif';
import unauthorizedGif from './../../../img/401.gif';
import forbiddenGif from './../../../img/403.gif';
import timeOutGif from './../../../img/408.gif';
import {
    SC_FORBIDDEN,
    SC_NOT_FOUND,
    SC_SERVER_ERROR,
    SC_TIME_OUT,
    SC_UNAUTHORIZED
} from '../../../services/constants/StatusCodesConstants';

const mapErrorStatus = (status) => {
    switch (status) {
        case SC_NOT_FOUND:
        case SC_FORBIDDEN:
        case SC_UNAUTHORIZED:
        case SC_TIME_OUT:
            return status;
        default:
            return SC_SERVER_ERROR;
    }
}

const getErrorMessage = (status) => {
    switch (status) {
        case SC_NOT_FOUND:
            return i18next.t('errorPage.404');
        case SC_FORBIDDEN:
            return i18next.t('errorPage.403');
        case SC_UNAUTHORIZED:
            return i18next.t('errorPage.401');
        case SC_TIME_OUT:
            return i18next.t('errorPage.408');
        default:
            return i18next.t('errorPage.500');
    }
}

const getErrorGif = (status) => {
    switch (status) {
        case SC_NOT_FOUND:
            return notFoundGif;
        case SC_FORBIDDEN:
            return forbiddenGif;
        case SC_UNAUTHORIZED:
            return unauthorizedGif;
        case SC_TIME_OUT:
            return timeOutGif;
        default:
            return serverErrorGif;
    }
}

class ErrorPage extends Component {
    constructor(props) {
        super(props);
        let status = SC_NOT_FOUND;
        if (props.match && props.match.params && props.match.params.statusCode) {
            status = props.match.params.statusCode;
            try {
                status = parseInt(status);
            }
            catch (err) {
                status = SC_NOT_FOUND;
            }
        }
        this.state = {
            status: status
        };
    }

    render() {
        const { status } = this.state;
        const errorStatus = status ? mapErrorStatus(status) : SC_NOT_FOUND;
        const errorMessage = getErrorMessage(status);
        const errorGif = getErrorGif(status);
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                        <div className="row text-center">
                            <div className="col">
                                <p><Link className="sign-in-brand" to="/">sk<i className="fas fa-bullseye"/>re</Link></p>
                            </div>
                        </div>
                        <div className="row text-center">
                            <div className="col">
                                <img src={errorGif} alt="error-gif"/>
                                <p className="error-message">{errorMessage}</p>
                                <p className="profile-username">Error {errorStatus}</p>
                                <p><Link className="return-to-home" to="/">{i18next.t('errorPage.returnToHome')}</Link></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

ErrorPage.propTypes = {
    match: PropTypes.object
}

export default ErrorPage;