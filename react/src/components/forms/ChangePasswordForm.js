import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import i18next from 'i18next';
import RenderInput from './inputs/RenderInput';
import SubmitButton from './elements/SubmitButton';
import FormTitle from './elements/FormTitle';
import CreateUserFormValidator from './validators/CreateUserValidator';
import FormComment from './elements/FormComment';
import UserService from '../../services/UserService';
import ErrorPage from '../screens/ErrorPage';
import { SC_CONFLICT } from '../../services/constants/StatusCodesConstants';

const validate = values => {
    const errors = {}
    errors.username = CreateUserFormValidator.validateUsername(values.username);
    errors.oldPassword = CreateUserFormValidator.validatePassword(values.oldPassword);
    errors.newPassword = CreateUserFormValidator.validatePassword(values.newPassword);
    errors.repeatNewPassword = CreateUserFormValidator.validateRepeatedPassword(values.repeatNewPassword, values.newPassword);
    return errors;
}

class ChangePasswordForm extends Component {
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
            <div className="container-fluid">
            <div className="row">
                <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 
                                col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                <FormTitle />
                <form onSubmit={handleSubmit(this.onSubmit)} >
                    {this.state.error && <span className="invalid-feedback d-block">
                            {i18next.t('changePasswordForm.passwordError')}</span>}
                    <Field name="username" label={i18next.t('createUserForm.username')} 
                            inputType="text" required={false} isDisabled={true}
                            component={RenderInput} />
                    <Field name="oldPassword" label={i18next.t('changePasswordForm.oldPassword')}
                        inputType="password" required={true} component={RenderInput} />
                    <Field name="newPassword" label={i18next.t('changePasswordForm.newPassword')}
                            inputType="password" required={true} component={RenderInput} />
                    <Field name="repeatNewPassword" label={i18next.t('changePasswordForm.repeatNewPassword')}
                            inputType="password" required={true} component={RenderInput} />
                    {/* TODO address with all of its fields and make them autoload as on deploy */}
                    <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2" text={i18next.t('forms.requiredFields')} />
                    <SubmitButton label={i18next.t('changePasswordForm.changePasswordButton')} divStyle="text-center" buttonStyle="btn btn-green mb-2" submitting={submitting} />
                </form>
                </div>
            </div>
            </div>
        );
    }
}

ChangePasswordForm = reduxForm({
    form: 'changePassword',
    destroyOnUnmount: false, // set to true to remove data on refresh
    validate
})(ChangePasswordForm)

export default ChangePasswordForm;