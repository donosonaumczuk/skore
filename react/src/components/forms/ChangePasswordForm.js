import React from 'react';
import { Field, reduxForm } from 'redux-form';
import i18next from 'i18next';
import RenderInput from './utils/RenderInput';
import SubmitButton from './utils/SubmitButton';
import FormTitle from './utils/FormTitle';
import CreateUserFormValidator from './validators/CreateUserValidator';

const validate = values => {
    const errors = {}
    errors.username = CreateUserFormValidator.validateUsername(values.username);
    errors.oldPassword = CreateUserFormValidator.validatePassword(values.oldPassword);
    errors.newPassword = CreateUserFormValidator.validatePassword(values.newPassword);
    errors.repeatNewPassword = CreateUserFormValidator.validateRepeatedPassword(values.repeatNewPassword, values.newPassword);
    return errors;
}

const onSubmit = async (values) => {
    // let user = this.loadUser(values, this.state.image);
    //TODO make post
}

let ChangePasswordForm = (props) => {
    const { handleSubmit, submitting } = props;
    return (
        <div className="container-fluid">
        <div className="row">
            <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 
                            col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
            <FormTitle />
            <form onSubmit={handleSubmit(onSubmit)} >
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
                <SubmitButton label={i18next.t('changePasswordForm.changePasswordButton')} divStyle="text-center" buttonStyle="btn btn-green mb-2" submitting={submitting} />
            </form>
            </div>
        </div>
        </div>
    );
}

ChangePasswordForm = reduxForm({
    form: 'changePassword',
    destroyOnUnmount: false, // set to true to remove data on refresh
    validate
})(ChangePasswordForm)

export default ChangePasswordForm;