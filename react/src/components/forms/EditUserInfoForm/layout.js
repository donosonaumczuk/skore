import React from 'react';
import { Field } from 'redux-form';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import RenderInput from '../inputs/RenderInput';
import ImageInput from '../inputs/ImageInput';
import RenderDatePicker from '../inputs/RenderDatePicker';
import SubmitButton from '../elements/SubmitButton';
import FormTitle from '../elements/FormTitle';
import FormComment from '../elements/FormComment';

const EditUserInfoForm = ({ handleSubmit, submitting, onSubmit,
                            imageName, handleChange }) => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid sign-in-container offset-sm-2
                                col-sm-8 offset-md-3 col-md-6 offset-lg-3
                                col-lg-6 offset-xl-4 col-xl-4">
                    <FormTitle />
                    <form onSubmit={handleSubmit(onSubmit)} >
                        <Field name="username" inputType="text" required={true}
                                label={i18next.t('createUserForm.username')} 
                                isDisabled={true} component={RenderInput} />
                        <Field name="firstName" inputType="text"
                                label={i18next.t('createUserForm.firstName')} 
                                required={true} component={RenderInput} />
                        <Field name="lastName" inputType="text" required={true}
                                label={i18next.t('createUserForm.lastName')} 
                                component={RenderInput} />
                        <Field name="email" inputType="text" required={true}
                                label={i18next.t('createUserForm.email')} 
                                isDisabled={true} component={RenderInput} />
                        <Field name="image" type="file" imageName={imageName}
                                label={i18next.t('createUserForm.profilePicture')}
                                acceptedFormat={i18next.t('createUserForm.imageFormat')}
                                onChange={handleChange} component={ImageInput} />
                        <Field name="cellphone" inputType="text" required={false}
                                label={i18next.t('createUserForm.cellphone')}
                                component={RenderInput} />
                        <Field name="birthday" inputType="text" required={true}
                                label={i18next.t('createUserForm.birthday')}
                                component={RenderDatePicker} />
                        {/* TODO address with all of its fields and make them autoload as on deploy */}
                        <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2"
                                        text={i18next.t('forms.requiredFields')} />
                        <SubmitButton label={i18next.t('editUserInfoForm.updateInfoButton')}
                                        divStyle="text-center" buttonStyle="btn btn-green mb-2" 
                                        submitting={submitting} />
                    </form>
                </div>
            </div>
        </div>
    )
}

EditUserInfoForm.propTypes = {
    handleSubmit: PropTypes.func.isRequired,
    onSubmit: PropTypes.func.isRequired,
    imageName: PropTypes.string.isRequired,
    handleChange: PropTypes.func.isRequired
}

export default EditUserInfoForm;