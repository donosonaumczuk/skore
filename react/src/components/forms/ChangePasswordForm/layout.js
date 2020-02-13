import React from 'react';
import { Field } from 'redux-form';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import RenderInput from '../inputs/RenderInput';
import SubmitButton from '../elements/SubmitButton';
import FormTitle from '../elements/FormTitle';
import FormComment from '../elements/FormComment';
import WithExecuting from '../../hocs/WithExecuting';
import WithError from '../../hocs/WithError';

const ChangePasswordForm = ({ handleSubmit, submitting, onSubmit, errorMessage }) => {
    return  (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid sign-in-container offset-sm-2
                                col-sm-8 offset-md-3 col-md-6 offset-lg-3
                                col-lg-6 offset-xl-4 col-xl-4">
                    <FormTitle />
                    <form onSubmit={handleSubmit(onSubmit)} >
                        {errorMessage && <span className="invalid-feedback d-block">
                                {i18next.t('changePasswordForm.passwordError')}</span>}
                        <Field name="username" label={i18next.t('createUserForm.username')} 
                                inputType="text" required={false} isDisabled={true}
                                component={RenderInput} />
                        <Field name="oldPassword" inputType="password" required={true}
                                label={i18next.t('changePasswordForm.oldPassword')}
                                component={RenderInput} />
                        <Field name="newPassword" inputType="password"
                                label={i18next.t('changePasswordForm.newPassword')}
                                required={true} component={RenderInput} />
                        <Field name="repeatNewPassword" inputType="password" required={true}
                                label={i18next.t('changePasswordForm.repeatNewPassword')}
                                component={RenderInput} />
                        <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2" 
                                    text={i18next.t('forms.requiredFields')} />
                        <SubmitButton label={i18next.t('changePasswordForm.changePasswordButton')}
                                        divStyle="text-center" buttonStyle="btn btn-green mb-2"
                                        submitting={submitting} />
                    </form>
                </div>
            </div>
        </div>
    )
}

ChangePasswordForm.propTypes = {
    handleSubmit: PropTypes.func.isRequired,
    onSubmit: PropTypes.func.isRequired,
    errorMessage: PropTypes.string, 
}

export default WithError(WithExecuting(ChangePasswordForm));