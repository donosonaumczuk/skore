import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import CreateUserFormValidator from '../validators/CreateUserValidator';
import UserService from '../../../services/UserService';
import ErrorPage from '../../screens/ErrorPage';
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
        const response = await UserService.updateUser(user, values.username);
        if (response.status) {
            this.setState({ error: response.status });
        }
        else {
            this.props.history.push(`/users/${values.username}`);
        }
        // let user = this.loadUser(values, this.state.image);
        //TODO make post
    }

    render() {
        const { handleSubmit, submitting } = this.props;
        if (this.state.error && this.state.error !== SC_CONFLICT) {
            return <ErrorPage status={this.state.error} />
        }
        return (
            <ChangePasswordForm handleSubmit={handleSubmit} submitting={submitting}
                                onSubmit={this.onSubmit} error={this.state.error} />
        );
    }
}

ChangePasswordFormContainer = reduxForm({
    form: 'changePassword',
    destroyOnUnmount: true,
    validate
})(ChangePasswordFormContainer)

export default ChangePasswordFormContainer;