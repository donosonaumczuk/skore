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

const RequestNewPasswordForm = ({ handleSubmit, submitting, onSubmit }) => {
    return (
        <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid sign-in-container
                            offset-sm-2 col-sm-8 offset-md-3 col-md-6
                            offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                        <FormTitle />
                        <form onSubmit={handleSubmit(onSubmit)}>
                            <Field name="username" id="username" inputType="text"
                                    label={i18next.t('createUserForm.username')}
                                    required={true} component={RenderInput} />
                            <Field name="email" id="email" inputType="text"
                                    label={i18next.t('createUserForm.email')} 
                                    required={true} component={RenderInput} />
                            <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2"
                                            text={i18next.t('forms.requiredFields')} />
                            <SubmitButton label={i18next.t('requestNewPasswordForm.requestNewPasswordButton')} 
                                            divStyle="text-center" buttonStyle="btn btn-green mb-2"
                                            submitting={submitting} />
                        </form>
                    </div>
                </div>
            </div>
    );
}

RequestNewPasswordForm.propTypes = {
    handleSubmit: PropTypes.func.isRequired,
    onSubmit: PropTypes.func.isRequired,
}

export default WithError(WithExecuting(RequestNewPasswordForm));