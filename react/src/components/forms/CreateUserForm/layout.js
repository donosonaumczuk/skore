import React from 'react';
import { Field } from 'redux-form';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import RenderInput from '../inputs/RenderInput';
import ImageInput from '../inputs/ImageInput';
// import RenderDatePicker from '../inputs/RenderDatePicker';
import SubmitButton from '../elements/SubmitButton';
import FormTitle from '../elements/FormTitle';
import SuggestionText from '../elements/SuggestionText';
import FormComment from '../elements/FormComment';
import LocationInput from '../inputs/LocationInput';
import SubLocationInput from '../inputs/SubLocationInput';
import BirthdayInput from '../inputs/BirthdayInput';

const CreateUserForm = ({ handleSubmit, submitting, onSubmit,
                            imageName, handleChange, updateLocation,
                            country, state, city, street, birthday,
                            changeFieldsValue, touchField }) => {
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
                            <Field name="password" id="password" inputType="password"
                                    label={i18next.t('createUserForm.password')} 
                                    required={true} component={RenderInput} />
                            <Field name="repeatPassword" id="repeatPassword"
                                    label={i18next.t('createUserForm.repeatPassword')}
                                    inputType="password" required={true} 
                                    component={RenderInput} />
                            <Field name="firstName" inputType="text" required={true}
                                    label={i18next.t('createUserForm.firstName')}
                                    id="firstName" component={RenderInput} />
                            <Field name="lastName" id="lastName" inputType="text"
                                    label={i18next.t('createUserForm.lastName')}
                                    required={true} component={RenderInput} />
                            <Field name="email" id="email" inputType="text"
                                    label={i18next.t('createUserForm.email')} 
                                    required={true} component={RenderInput} />
                            <Field name="image" imageName={imageName} type="file"
                                    label={i18next.t('createUserForm.profilePicture')} 
                                    acceptedFormat={i18next.t('createUserForm.imageFormat')}
                                    component={ImageInput} onChange={handleChange} />
                            <Field name="cellphone" id="cellphone" inputType="text"
                                    label={i18next.t('createUserForm.cellphone')} 
                                    required={false} component={RenderInput} />
                            <BirthdayInput required={true} birthday={birthday}
                                            changeFieldsValue={changeFieldsValue}
                                            touchField={touchField} />
                            <LocationInput updateLocation={updateLocation} />
                            <SubLocationInput label={i18next.t('location.country')}
                                                id="country" divStyle="form-group"
                                                value={country ? country : ""} />
                            <SubLocationInput label={i18next.t('location.street')} 
                                                id="route" divStyle="form-group"
                                                value={street ? street : ""} />
                            <div className="form-row">
                                <SubLocationInput label={i18next.t('location.city')}
                                                    id="locality" 
                                                    value={city ? city : ""} 
                                                    divStyle="form-group col-6" />
                                <SubLocationInput label={i18next.t('location.state')} 
                                                    id="administrative_area_level_1"
                                                    value={state ? state : ""} 
                                                    divStyle="form-group col-6"/> 
                            </div>
                            <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2"
                                            text={i18next.t('forms.requiredFields')} />
                            <SubmitButton label={i18next.t('createUserForm.signUpButton')} 
                                            divStyle="text-center" buttonStyle="btn btn-green mb-2"
                                            submitting={submitting} />
                            <SuggestionText suggestion={i18next.t('createUserForm.existingUser')}
                                            link="/login" linkText={i18next.t('login.login')} />
                        </form>
                    </div>
                </div>
            </div>
    );
}

CreateUserForm.propTypes = {
    handleSubmit: PropTypes.func.isRequired,
    onSubmit: PropTypes.func.isRequired,
    imageName: PropTypes.string.isRequired,
    handleChange: PropTypes.func.isRequired,
    updateLocation: PropTypes.func.isRequired,
    country: PropTypes.string,
    state: PropTypes.string,
    city: PropTypes.string,
    street: PropTypes.string,
    birthday: PropTypes.object.isRequired
}

export default CreateUserForm;