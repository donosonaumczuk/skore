import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import { Redirect } from 'react-router-dom';
import Proptypes from 'prop-types';
import i18next from 'i18next';
import AuthService from '../../../services/AuthService';
import LogInValidator from '../validators/LogInValidator';
import { SC_UNAUTHORIZED } from '../../../services/constants/StatusCodesConstants';
import LogInForm from './layout';

const validate = values => {
    const errors = {}
    errors.username = LogInValidator.validateUsername(values.username);
    errors.password =  LogInValidator.validatePassword(values.password);
    return errors;
}

class LogInFormContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            errorMessage: null,
        };
    }

    onSubmit = async (values) => {
        const response = await AuthService.logInUser(
            {
                "username": values.username,
                "password": values.password
            }
        );
        if (response.status === SC_UNAUTHORIZED) {
            const errorMessage = i18next.t('login.errors.invalidUsernameOrPassword')
            this.setState({ errorMessage: errorMessage })
        }
        else {
            //TODO handle other error status maybe?
            const isAdmin = AuthService.isAdmin();
            this.props.updateUser({
                username: values.username,
                isAdmin: isAdmin
            });
        }
    }

    getErrorMessage = () => {
        if (this.state.errorMessage) {
            return (<span className="invalid-feedback d-block">
                        {this.state.errorMessage}
                    </span>);
        }
        return <React.Fragment></React.Fragment>;
    }

    render() {
        const { handleSubmit, submitting } = this.props; 
        const currentUser = AuthService.getCurrentUser();
        const errorMessage = this.getErrorMessage();
        if (currentUser) {
            return <Redirect to={`/users/${currentUser}`} />
        }
        return (
            <LogInForm onSubmit={this.onSubmit} errorMessage={errorMessage}
                        handleSubmit={handleSubmit} submitting={submitting} />
        );
    }
}

LogInFormContainer = reduxForm({
    form: 'login',
    destroyOnUnmount: true,
    validate
})(LogInFormContainer) 

LogInFormContainer.propTypes = {
    updateUser: Proptypes.func.isRequired
}

export default LogInFormContainer;