import React from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import i18next from 'i18next';
import FormTitle from '../elements/FormTitle';
import RenderInput from '../inputs/RenderInput';
import SubmitButton from '../elements/SubmitButton';
import FormComment from '../elements/FormComment';
import WithError from '../../hocs/WithError';
import WithExecuting from '../../hocs/WithExecuting';
import WithPermission from '../../hocs/WithPermission';

const SetMatchScoreForm = ({ onSubmit, handleSubmit, submitting, errorMessage }) => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                    <FormTitle />
                    <form onSubmit={handleSubmit(onSubmit)}>
                        {errorMessage}
                        <Field name="teamOneScore" id="teamOneScore" inputType="text"
                                label={i18next.t('setMatchScoreForm.teamOneScore')}
                                required={true} component={RenderInput} />
                        <Field name="teamTwoScore" id="teamTwoScore" inputType="text"
                                label={i18next.t('setMatchScoreForm.teamTwoScore')}
                                required={true} component={RenderInput} />
                        <Field name="repeatTeamOneScore" id="repeatTeamOneScore" 
                                inputType="text" required={true} 
                                label={i18next.t('setMatchScoreForm.repeatTeamOneScore')}
                                component={RenderInput} />
                        <Field name="repeatTeamTwoScore" id="repeatTeamTwoScore" 
                                inputType="text" required={true} 
                                label={i18next.t('setMatchScoreForm.repeatTeamTwoScore')}
                                component={RenderInput} />
                        <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2"
                                        text={i18next.t('forms.requiredFields')} />
                        <SubmitButton label={i18next.t('setMatchScoreForm.setScoreButton')}
                                        divStyle="text-center" buttonStyle="btn btn-green mb-2"
                                        submitting={submitting} />
                    </form>
                </div>
            </div>
        </div>
    );
}

SetMatchScoreForm.propTypes = {
    onSubmit: PropTypes.func.isRequired,
    handleSubmit: PropTypes.func.isRequired
}

export default WithPermission(WithError(WithExecuting(SetMatchScoreForm)));