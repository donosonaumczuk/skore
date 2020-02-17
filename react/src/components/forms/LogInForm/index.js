import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import { Redirect } from 'react-router-dom';
import PropTypes from 'prop-types';
import i18next from 'i18next';
import AuthService from '../../../services/AuthService';
import LogInValidator from '../validators/LogInValidator';
import { SC_UNAUTHORIZED } from '../../../services/constants/StatusCodesConstants';
import LogInForm from './layout';
import Utils from '../../utils/Utils';

const validate = values => {
    const errors = {}
    errors.username = LogInValidator.validateUsername(values.username);
    errors.password =  LogInValidator.validatePassword(values.password);
    return errors;
}

class LogInFormContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        let message = null;
        if (!AuthService.getCurrentUser() && props.currentUser) {
            props.updateUser(null);
            message = i18next.t('login.sesionExpired');
        }
        else if(AuthService.getCurrentUser() && !props.currentUser) {
            props.updateUser({
                    username: AuthService.getCurrentUser(),
                    isAdmin: AuthService.isAdmin()
                }
            );
        }
        this.state = {
            errorMessage: null,
            message: message
        };
    }

    onSubmit = async (values) => {
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await AuthService.logInUser(
            {
                "username": values.username,
                "password": values.password
            }
        );
        if (response.status === SC_UNAUTHORIZED) {
            const errorMessage = i18next.t('login.errors.invalidUsernameOrPassword');
            if (this.mounted) {
                this.setState({ errorMessage: errorMessage, executing: false });
            }
        }
        else {
            if (this.mounted) {
                this.setState({ executing: false });
            }

            const isAdmin = AuthService.isAdmin();
            this.props.updateUser({
                username: values.username,
                isAdmin: isAdmin
            });
        }
    }

    componentDidMount() {
        this.mounted = true;
    }

    render() {
        const { handleSubmit, submitting, url } = this.props; 
        const currentUser = AuthService.getCurrentUser();
        const errorMessage = Utils.getErrorMessage(this.state.errorMessage);
        if (currentUser && !url) {
            return <Redirect to={`/users/${currentUser}`} />
        }
        else if (currentUser && url) {
            return <Redirect to={`${url}`} />
        }
        return (
            <LogInForm onSubmit={this.onSubmit} errorMessage={errorMessage}
                        handleSubmit={handleSubmit} submitting={submitting}
                        isExecuting={this.state.executing}
                        message={this.state.message} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

LogInFormContainer = reduxForm({
    form: 'login',
    destroyOnUnmount: true,
    validate
})(LogInFormContainer) 

LogInFormContainer.propTypes = {
    updateUser: PropTypes.func.isRequired,
    currentUser: PropTypes.string
}

export default LogInFormContainer;