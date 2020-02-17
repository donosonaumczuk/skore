import React from 'react';
import { Field } from 'redux-form';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import FormTitle from '../elements/FormTitle';
import RenderInput from '../inputs/RenderInput';
import ImageInput from '../inputs/ImageInput';
import SubmitButton from '../elements/SubmitButton';
import FormComment from '../elements/FormComment';
import WithError from '../../hocs/WithError';
import WithExecuting from '../../hocs/WithExecuting';


const CreateSportForm = ({ handleSubmit, submitting, onSubmit, sportNameError,
                            imageName, handleChange }) => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid sign-in-container offset-sm-2
                                col-sm-8 offset-md-3 col-md-6 offset-lg-3
                                col-lg-6 offset-xl-4 col-xl-4">
                    <FormTitle />
                    <form onSubmit={handleSubmit(onSubmit)}>
                        <Field name="sportName" id="sportName" inputType="text"
                                label={i18next.t('createSportForm.sportName')}
                                required={true} component={RenderInput} />
                                {sportNameError && 
                                <span className="invalid-feedback d-block">
                                    {i18next.t('createSportForm.errors.sportName.alreadyExists')}
                                </span> }
                        <Field name="displayName" id="displayName" inputType="text"
                                label={i18next.t('createSportForm.displayName')}
                                required={true} component={RenderInput} />
                        <Field name="playersPerTeam" id="playersPerTeam" inputType="text"
                                label={i18next.t('createSportForm.playersPerTeam')}
                                required={true} component={RenderInput} />
                        <Field name="sportImage" imageName={imageName} type="file"
                                label={i18next.t('createSportForm.sportImage')} 
                                acceptedFormat={i18next.t('createUserForm.imageFormat')}
                                required={true} component={ImageInput}
                                checkOnSubmit={true} onChange={handleChange} />
                        <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2"
                                        text={i18next.t('forms.requiredFields')} />
                        <SubmitButton label={i18next.t('createSportForm.createSportButton')}
                                        divStyle="text-center" buttonStyle="btn btn-green mb-2"
                                        submitting={submitting} />
                    </form>
                </div>
            </div>
        </div>
    );
}

CreateSportForm.propTypes = {
    handleSubmit: PropTypes.func.isRequired,
    onSubmit: PropTypes.func.isRequired,
    sportNameError: PropTypes.bool,
    imageName: PropTypes.string.isRequired,
    handleChange: PropTypes.func.isRequired
}

export default WithError(WithExecuting(CreateSportForm));