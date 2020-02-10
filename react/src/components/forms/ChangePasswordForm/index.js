import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import CreateUserFormValidator from '../validators/CreateUserValidator';
import UserService from '../../../services/UserService';
import { SC_CONFLICT } from '../../../services/constants/StatusCodesConstants';
import ChangePasswordForm from './layout';

const validate = values => {
    const errors = {}
    errors.username = CreateUserFormValidator.validateUsername(values.username);
    errors.oldPassword = CreateUserFormValidator.validatePassword(values.oldPassword);
    errors.newPassword = CreateUserFormValidator.validatePassword(values.newPassword);
    errors.repeatNewPassword = CreateUserFormValidator.validateRepeatedPassword(values.repeatNewPassword, values.newPassword);
    return errors;
}

class ChangePasswordFormContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            error: null
        }
    }

    loadUser = (values) => {
        const user = {
            "oldPassword": values.oldPassword,
            "password": values.newPassword
        };
        return user;
    }

    onSubmit = async (values) => {
        const user = this.loadUser(values);
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await UserService.updateUser(user, values.username);
        if (response.status) {
            if (this.mounted) {
                this.setState({ error: response.status, executing: false });
            }
        }
        else {
            this.props.history.push(`/users/${values.username}`);
        }
    }

    componentDidMount() {
        this.mounted = true;
    }

    componentDidMount() {
        this.mounted = true;
    }

    render() {
        const { handleSubmit, submitting } = this.props;
        const hasError = this.state.error && this.state.error !== SC_CONFLICT;
        return (
            <ChangePasswordForm handleSubmit={handleSubmit} submitting={submitting}
                                onSubmit={this.onSubmit} errorMessage={this.state.error} 
                                isExecuting={this.state.executing} 
                                error={hasError ? this.state.error : null} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

ChangePasswordFormContainer = reduxForm({
    form: 'changePassword',
    destroyOnUnmount: true,
    validate
})(ChangePasswordFormContainer)

export default ChangePasswordFormContainer;