import React from 'react';
import { Field } from 'redux-form';
import PropTypes from 'prop-types';
import i18next from 'i18next';
import FormTitle from '../elements/FormTitle';
import RenderInput from '../inputs/RenderInput';
import SubmitButton from '../elements/SubmitButton';
import SuggestionText from '../elements/SuggestionText';
import WithError from '../../hocs/WithError';
import WithLoading from '../../hocs/WithLoading';

const AnonymousForm = ({ handleSubmit, onSubmit, submitting }) => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                    <FormTitle />
                    <form onSubmit={handleSubmit(onSubmit)}>
                        <Field name="firstName" label={i18next.t('createUserForm.firstName')}
                                id="firstName" inputType="text" required={true} 
                                component={RenderInput} />
                        <Field name="lastName" label={i18next.t('createUserForm.lastName')}
                                inputType="text" required={true} component={RenderInput} />
                        <Field name="email" label={i18next.t('createUserForm.email')}
                                inputType="text" required={true} component={RenderInput} />
                        <SubmitButton label={i18next.t('home.joinMatch')} divStyle="text-center"
                                        buttonStyle="btn btn-green mb-2" submitting={submitting} />
                    </form>
                </div>
            </div>
        </div>
    );
}

AnonymousForm.propTypes = {
    onSubmit: PropTypes.func.isRequired,
    handleSubmit: PropTypes.func.isRequired
}

export default WithError(WithLoading(AnonymousForm));