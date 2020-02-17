import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';
import queryString from 'query-string';
import PropTypes from 'prop-types';
import i18next from 'i18next';
import AuthService from '../../../services/AuthService';
import UserService from '../../../services/UserService';
import Loader from '../../Loader';
import { SC_CONFLICT } from '../../../services/constants/StatusCodesConstants';
import Message from '../../Message';
import { EC_INVALID_CODE } from '../../../services/constants/ErrorCodesConstants';

class ConfirmAccountContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const queryParams = queryString.parse(props.location.search);
        const { username, code } = queryParams;   
        this.state = {
            status: null,
            isLoading: true,
            username: username,
            code: code
        };
    }

    componentDidMount = async () => {
        this.mounted = true;
        const { username, code } = this.state;
        const response = await UserService.verifyUser(username, code);
        if (response.status && this.mounted) {
            if (response.status === SC_CONFLICT) {
                if (response.data.errorCode === EC_INVALID_CODE) {
                    const errorMessage = i18next.t('confirmAccount.invalidCode');
                    this.setState({ isLoading: false, errorMessage: errorMessage });
                }
                else {
                    const errorMessage = i18next.t('confirmAccount.alreadyConfirmed');
                    this.setState({ isLoading: false, errorMessage: errorMessage });
                }
            }
            this.setState({ status: response.status, isLoading: false });
        }
        else {
            if (this.mounted) {
                this.setState({ isLoading: false });
                this.props.updateUser({ username: username });
                this.props.history.push(`/users/${username}`);
            }
        }
    }

    render() {
        if (AuthService.getCurrentUser()) {
            return <Redirect to="/" />
        }
        const { status, errorMessage } = this.state;
        if (errorMessage) {
            return <Message message={errorMessage} />
        }
        else if (status) {
            return <Redirect to={`/error/${status}`} />
        }
        return <Loader />;
    }

    componentWillUnmount = () => {
        this.mounted =false;
    }
}

ConfirmAccountContainer.propTypes = {
    location: PropTypes.object.isRequired,
    history: PropTypes.object.isRequired,
    updateUser: PropTypes.func.isRequired
}

export default ConfirmAccountContainer;