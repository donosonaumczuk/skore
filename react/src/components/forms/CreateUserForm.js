import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { Redirect } from 'react-router-dom';
import i18next from 'i18next';
import RenderInput from './inputs/RenderInput';
import ImageInput from './inputs/ImageInput';
import RenderDatePicker from './inputs/RenderDatePicker';
import SubmitButton from './elements/SubmitButton';
import FormTitle from './elements/FormTitle';
import SuggestionText from './elements/SuggestionText';
import AuthService from './../../services/AuthService';
import CreateUserFormValidator from './validators/CreateUserValidator';
import UserService from '../../services/UserService';
import FormComment from './elements/FormComment';
import LocationInput from './inputs/LocationInput';
import SubLocationInput from './inputs/SubLocationInput';

const validate = values => {
    const errors = {}
    errors.username = CreateUserFormValidator.validateUsername(values.username);
    errors.password = CreateUserFormValidator.validatePassword(values.password);
    errors.repeatPassword = CreateUserFormValidator.validateRepeatedPassword(values.repeatPassword, values.password);
    errors.firstName = CreateUserFormValidator.validateFirstName(values.firstName);
    errors.lastName = CreateUserFormValidator.validateLastName(values.lastName);
    errors.email = CreateUserFormValidator.validateEmail(values.email);
    errors.image = CreateUserFormValidator.validateImage(values.image);
    errors.cellphone = CreateUserFormValidator.validateCellphone(values.cellphone);
    errors.birthday = CreateUserFormValidator.validateDate(values.birthday);
    return errors;
}

class CreateUserForm extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            image: null,
            created: false,
            street: null,
            city: null,
            state: null,
            country: null
        };
    }
 
    handleChange = (image) => {
        if (image && image.size > 0) {
            let reader = new FileReader();
            reader.readAsDataURL(image);
            reader.onload = (e) => {
                const data = (e.target.result);
                this.setState ({
                    image: {
                        name: image.name,
                        type: image.type,
                        size: image.size,
                        data: data
                    }
                });
            }
        }
        else {
            this.setState ({
                image: null
            });
        }
    }

    getBirthdayWithCorrectFormat = birthday => {
        const birthdayInfo = birthday.split("/");
        let newBirthday = `${birthdayInfo[2]}-${birthdayInfo[0]}-${birthdayInfo[1]}`;
        return newBirthday;
    }

    loadUser = (values, image) => {
        const birthday = this.getBirthdayWithCorrectFormat(values.birthday);
        const user = {
            "username": values.username,
            "password": values.password,
            "firstName": values.firstName,
            "lastName": values.lastName,
            "email": values.email,
            "image": image ? image.data : null,
            "cellphone": values.cellphone ? values.cellphone : null ,
            "birthday": birthday,
            "home": {
                "country": this.state.country,
                "state": this.state.state,
                "city": this.state.city,
                "street": this.state.street
            }
        };
        return user;
    }

    updateLocation = home => {
        if (this.mounted) {
            this.setState({
                street: home.street,
                city: home.city,
                state: home.state,
                country: home.country            
            });
        }
    }

    onSubmit = async (values) => {
        let user = this.loadUser(values, this.state.image);
        const res = await UserService.createUser(user);
        if (res.status) {
           //TODO handle error
        }
        else {
            this.props.history.push(`/confirmAccount`);
        }
    }

    componentDidMount() {
        this.mounted = true;
    }

    render() {
        const { handleSubmit, submitting } = this.props;
        let imageName = "";
        const currentUser = AuthService.getCurrentUser();
        if (currentUser) {
            return <Redirect to={`/users/${currentUser}`} />
        }
        if (this.state.image != null) {
            imageName = this.state.image.name;
        }
    
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                        <FormTitle />
                        <form onSubmit={handleSubmit(this.onSubmit)}>
                            <Field name="username" label={i18next.t('createUserForm.username')}
                                    id="username" inputType="text" required={true} component={RenderInput} />
                            <Field name="password" label={i18next.t('createUserForm.password')} 
                                    id="password" inputType="password" required={true} component={RenderInput} />
                            <Field name="repeatPassword" label={i18next.t('createUserForm.repeatPassword')}
                                    id="repeatPassword" inputType="password" required={true} 
                                    component={RenderInput} />
                            <Field name="firstName" label={i18next.t('createUserForm.firstName')} id="firstName"
                                    inputType="text" required={true} component={RenderInput} />
                            <Field name="lastName" label={i18next.t('createUserForm.lastName')} id="lastName"
                                    inputType="text" required={true} component={RenderInput} />
                            <Field name="email" label={i18next.t('createUserForm.email')} id="email"
                                    inputType="text" required={true} component={RenderInput} />
                            <Field name="image" label={i18next.t('createUserForm.profilePicture')} type="file"
                                    imageName={imageName} acceptedFormat={i18next.t('createUserForm.imageFormat')}
                                    component={ImageInput} onChange={this.handleChange} />
                            <Field name="cellphone" label={i18next.t('createUserForm.cellphone')} id="cellphone"
                                        inputType="text" required={false} component={RenderInput} />
                            <Field name="birthday" label={i18next.t('createUserForm.birthday')} id="birthday"
                                    inputType="text" required={true} component={RenderDatePicker} />
                            <LocationInput updateLocation={this.updateLocation} />
                            <SubLocationInput label={i18next.t('location.country')} id="country"
                                                value={this.state.country ? this.state.country : ""} 
                                                divStyle="form-group" />
                            <SubLocationInput label={i18next.t('location.street')} id="route"
                                                value={this.state.street ? this.state.street : ""} 
                                                divStyle="form-group" />
                            <div className="form-row">
                                <SubLocationInput label={i18next.t('location.city')} id="locality" 
                                                    value={this.state.city ? this.state.city : ""} 
                                                    divStyle="form-group col-6" />
                                <SubLocationInput label={i18next.t('location.state')} id="administrative_area_level_1"
                                                    value={this.state.state ? this.state.state : ""} 
                                                    divStyle="form-group col-6"/> 
                            </div>
                            <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2" text={i18next.t('forms.requiredFields')} />
                            <SubmitButton label={i18next.t('createUserForm.signUpButton')} divStyle="text-center" buttonStyle="btn btn-green mb-2" submitting={submitting} />
                            <SuggestionText suggestion={i18next.t('createUserForm.existingUser')} link="/login" linkText={i18next.t('login.login')} />
                        </form>
                    </div>
                </div>
            </div>
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}               

CreateUserForm = reduxForm({
    form: 'createUser',
    destroyOnUnmount: false, // set to true to remove data on refresh
    validate
})(CreateUserForm)

export default CreateUserForm;
