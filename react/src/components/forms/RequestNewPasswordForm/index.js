import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import { Redirect } from 'react-router-dom';
import AuthService from '../../../services/AuthService';
import CreateUserFormValidator from '../validators/CreateUserValidator';
import UserService from '../../../services/UserService';
import RequestNewPasswordForm from './layout';
import { SC_CONFLICT, SC_NOT_FOUND } from '../../../services/constants/StatusCodesConstants';
import Message from '../../Message';
import i18next from 'i18next';
import Utils from '../../utils/Utils';

const validate = values => {
    const errors = {}
    errors.username = CreateUserFormValidator.validateUsername(values.username);
    errors.email = CreateUserFormValidator.validateEmail(values.email);
    return errors;
}

class RequestNewPasswordFormContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            executing: false
        };
    }

    onSubmit = async (values) => {
        let user = { "email": values.email };
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await UserService.requestNewPassword(values.username, user);
        if (response.status) {
            if (response.status === SC_CONFLICT) {
                if (this.mounted) {
                    const errorMessage = i18next.t('requestNewPasswordForm.emailUserNotMatch');
                    this.setState({ executing: false, errorMessage: errorMessage });
                } 
            }
            else if (response.status === SC_NOT_FOUND) {
                const errorMessage = i18next.t('requestNewPasswordForm.userNotExists');
                this.setState({ executing: false, errorMessage: errorMessage });
            }
            else {
                this.setState({ executing: false, error: response.status })
            }
        }
        else {
            this.setState({ requested: true });
        }
    }

    componentDidMount() {
        this.mounted = true;
    }

    render() {
        const { handleSubmit, submitting } = this.props;
        const { executing, error, requested } = this.state;
        const currentUser = AuthService.getCurrentUser();
        const errorMessage = Utils.getErrorMessage(this.state.errorMessage);
        if (currentUser) {
            return <Redirect to={`/users/${currentUser}`} />
        }
        else if (requested) {
            return <Message message={i18next.t('requestNewPasswordForm.resetPasswordMessage')} />
        }
    
        return (
            <RequestNewPasswordForm handleSubmit={handleSubmit}
                                    submitting={submitting}
                                    onSubmit={this.onSubmit}
                                    isExecuting={executing}
                                    error={error} 
                                    errorMessage={errorMessage} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}               

RequestNewPasswordFormContainer = reduxForm({
    form: 'requestNewPassword',
    destroyOnUnmount: true,
    validate,
})(RequestNewPasswordFormContainer)

export default RequestNewPasswordFormContainer;
