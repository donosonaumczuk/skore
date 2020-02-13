import React, { Component } from 'react';
import i18next from 'i18next';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { SC_NOT_FOUND } from '../../../services/constants/StatusCodesConstants';

const getErrorMessage = (status) => {
    if (status === SC_NOT_FOUND) {
        return i18next.t('errorPage.404');
    }
    else {
        return "";
        //TODO make more default messages for diferent errors
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
}

ErrorPage.propTypes = {
    match: PropTypes.object
}

export default ErrorPage;