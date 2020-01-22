import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { Redirect } from 'react-router-dom';
import i18next from 'i18next';
import RenderInput from './utils/RenderInput';
import ImageInput from './utils/ImageInput';
import RenderDatePicker from './utils/RenderDatePicker';
import SubmitButton from './utils/SubmitButton';
import FormTitle from './utils/FormTitle';
import SuggestionText from './utils/SuggestionText';
import AuthService from './../../services/AuthService';
import CreateUserFormValidator from './validators/CreateUserValidator';

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
    constructor(props) {
        super(props);
        this.state = {
        image: null
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

    loadUser = (values, image) => {
        const user = {
            "username": values.username,
            "password": values.password,
            "firstName": values.firstName,
            "lastName": values.lastName,
            "email": values.email,
            "image": image,
            "cellphone": values.cellphone ? values.cellphone : null ,
            "birthday": values.birthday

        };
        return user;
    }

  onSubmit = async (values) => {
    // let user = this.loadUser(values, this.state.image);
    //TODO make post
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
                            inputType="text" required={true} component={RenderInput} />
                    <Field name="password" label={i18next.t('createUserForm.password')}
                            inputType="password" required={true} component={RenderInput} />
                    <Field name="repeatPassword" label={i18next.t('createUserForm.repeatPassword')}
                                inputType="password" required={true} component={RenderInput} />
                    <Field name="firstName" label={i18next.t('createUserForm.firstName')}
                                inputType="text" required={true} component={RenderInput} />
                    <Field name="lastName" label={i18next.t('createUserForm.lastName')}
                                inputType="text" required={true} component={RenderInput} />
                    <Field name="email" label={i18next.t('createUserForm.email')} 
                                inputType="text" required={true} component={RenderInput} />
                    <Field name="image" label={i18next.t('createUserForm.profilePicture')}
                            type="file" imageName={imageName} acceptedFormat={i18next.t('createUserForm.imageFormat')}
                            component={ImageInput} onChange={this.handleChange} />
                    <Field name="cellphone" label={i18next.t('createUserForm.cellphone')}
                                inputType="text" required={false} component={RenderInput} />
                    <Field name="birthday" label={i18next.t('createUserForm.birthday')}
                            inputType="text" required={true} component={RenderDatePicker} />
                    {/* TODO address with all of its fields and make them autoload as on deploy */}
                    <SubmitButton label={i18next.t('createUserForm.signUpButton')} divStyle="text-center" buttonStyle="btn btn-green mb-2" submitting={submitting} />
                    <SuggestionText suggestion={i18next.t('createUserForm.existingUser')} link="/login" linkText={i18next.t('login.login')} />
                </form>
                </div>
            </div>
            </div>
        );
    }
}               

CreateUserForm = reduxForm({
    form: 'createUse',
    destroyOnUnmount: false, // set to true to remove data on refresh
    validate
})(CreateUserForm)

export default CreateUserForm;
