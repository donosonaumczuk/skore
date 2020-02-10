import React from 'react';
import { Field } from 'redux-form';
import PropTypes from 'prop-types';
import i18next from 'i18next';
import FormTitle from '../elements/FormTitle';
import RenderInput from '../inputs/RenderInput';
import SubmitButton from '../elements/SubmitButton';
import SuggestionText from '../elements/SuggestionText';

const LogInForm = ({ handleSubmit, onSubmit, submitting, errorMessage }) => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                    <FormTitle />
                    <form onSubmit={handleSubmit(onSubmit)}>
                        {errorMessage}
                        <Field name="username" label={i18next.t('createUserForm.username')}
                                id="username" inputType="text" required={false} component={RenderInput} />
                        <Field name="password" label={i18next.t('createUserForm.password')}
                                inputType="password" required={false} component={RenderInput} />
                        <SubmitButton label={i18next.t('login.loginButton')} divStyle="text-center"
                                        buttonStyle="btn btn-green mb-2" submitting={submitting} />
                        <SuggestionText suggestion={i18next.t('login.newUser')} link="/signUp" 
                                        linkText={i18next.t('createUserForm.signUp')} />
                    </form>
                </div>
            </div>
        </div>
    );
}

LogInForm.propTypes = {
    onSubmit: PropTypes.func.isRequired,
    errorMessage: PropTypes.object.isRequired,
    handleSubmit: PropTypes.func.isRequired
}

export default LogInForm;