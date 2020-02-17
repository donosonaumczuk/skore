import React from 'react';
import { Field } from 'redux-form';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import RenderInput from '../inputs/RenderInput';
import ImageInput from '../inputs/ImageInput';
import SubmitButton from '../elements/SubmitButton';
import FormTitle from '../elements/FormTitle';
import FormComment from '../elements/FormComment';
import WithExecuting from '../../hocs/WithExecuting';
import WithError from '../../hocs/WithError';

const EditSportForm = ({ handleSubmit, submitting, onSubmit, 
                            imageName, handleChange, errorMessage }) => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid sign-in-container offset-sm-2
                                col-sm-8 offset-md-3 col-md-6 offset-lg-3
                                col-lg-6 offset-xl-4 col-xl-4">
                    <FormTitle />
                    <form onSubmit={handleSubmit(onSubmit)} >
                        {errorMessage}
                        <Field name="sportName" id="sportName" inputType="text"
                                label={i18next.t('createSportForm.sportName')}
                                required={true} isDisabled={true}
                                component={RenderInput} />
                        <Field name="displayName" id="displayName" inputType="text"
                                label={i18next.t('createSportForm.displayName')}
                                required={true} component={RenderInput} />
                        <Field name="playersPerTeam" id="playersPerTeam"
                                label={i18next.t('createSportForm.playersPerTeam')}
                                inputType="text" required={true}
                                component={RenderInput} />
                        <Field name="sportImage" type="file" imageName={imageName}
                                label={i18next.t('createSportForm.sportImage')} 
                                acceptedFormat={i18next.t('createUserForm.imageFormat')}
                                required={false} onChange={handleChange}
                                component={ImageInput} />
                        <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2"
                                        text={i18next.t('forms.requiredFields')} />
                        <SubmitButton label={i18next.t('editUserInfoForm.updateInfoButton')} 
                                        divStyle="text-center" buttonStyle="btn btn-green mb-2" 
                                        submitting={submitting} />
                    </form>
                </div>
            </div>
        </div>
    );
} 

EditSportForm.propTypes = {
    handleSubmit: PropTypes.func.isRequired,
    onSubmit: PropTypes.func.isRequired,
    imageName: PropTypes.string.isRequired,
    handleChange: PropTypes.func.isRequired
}

export default WithError(WithExecuting(EditSportForm));

