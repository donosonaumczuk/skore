import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import RenderInput from './inputs/RenderInput';
import ImageInput from './inputs/ImageInput';
import RenderDatePicker from './inputs/RenderDatePicker';
import SubmitButton from './inputs/SubmitButton';
import FormTitle from './inputs/FormTitle';
import CreateUserFormValidator from './validators/CreateUserValidator';
import FormComment from './inputs/FormComment';
import UserService from '../../services/UserService';

const validate = values => {
    const errors = {}
    errors.firstName = CreateUserFormValidator.validateFirstName(values.firstName);
    errors.lastName = CreateUserFormValidator.validateLastName(values.lastName);
    errors.image = CreateUserFormValidator.validateImage(values.image);
    errors.cellphone = CreateUserFormValidator.validateCellphone(values.cellphone);
    errors.birthday = CreateUserFormValidator.validateDate(values.birthday);
    return errors;
}

class EditUserInfoForm extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const { username } = this.props;
        this.state = {
            image: null,
            username: username
        };
    } 

    handleChange = (image) => {
        if (image && image.size > 0) {
            let reader = new FileReader();
            reader.readAsDataURL(image);
            reader.onload = (e) => {
                const data = (e.target.result);
                if (this.mounted) {
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
        }
        else if (this.mounted) {
            this.setState ({
                image: null
            });
        }
    }

    componentDidMount() {
        this.mounted = true;
    }

    getBirthdayWithCorrectFormat = birthday => {
        const birthdayInfo = birthday.split("/");
        let newBirthday = `${birthdayInfo[2]}-${birthdayInfo[0]}-${birthdayInfo[1]}`;
        return newBirthday;
    }

    loadUser = (values, image) => {
        const birthday = this.getBirthdayWithCorrectFormat(values.birthday);
        const user = {
            "firstName": values.firstName,
            "lastName": values.lastName,
            "email": values.email,
            "image": image,
            "cellphone": values.cellphone ? values.cellphone : null ,
            "birthday": birthday
        };
        return user;
    }
    
    onSubmit = async (values) => {
        let user = this.loadUser(values, this.state.image);
        const response = await UserService.updateUser(user, this.state.username);
        if (response.status) {
           //TODO handle error
        }
        else {
            this.props.history.push(`/${this.state.username}`);
        }
    }
    
    render() {
        const { handleSubmit, submitting } = this.props;
        let imageName = "";
        if (this.state.image != null) {
            imageName = this.state.image.name;
        }
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 
                                    col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                        <FormTitle />
                        <form onSubmit={handleSubmit(this.onSubmit)} >
                            <Field name="username" label={i18next.t('createUserForm.username')} 
                                    inputType="text" required={true} isDisabled={true}
                                    component={RenderInput} />
                            <Field name="firstName" label={i18next.t('createUserForm.firstName')} 
                                    inputType="text" required={true} component={RenderInput} />
                            <Field name="lastName" label={i18next.t('createUserForm.lastName')} 
                                    inputType="text" required={true} component={RenderInput} />
                            <Field name="email" label={i18next.t('createUserForm.email')} 
                                    inputType="text" required={true} isDisabled={true}
                                    component={RenderInput} />
                            <Field name="image" label={i18next.t('createUserForm.profilePicture')}
                                    type="file" imageName={imageName} component={ImageInput}
                                    acceptedFormat={i18next.t('createUserForm.imageFormat')}
                                    onChange={this.handleChange} />
                            <Field name="cellphone" label={i18next.t('createUserForm.cellphone')}
                                        inputType="text" required={false} component={RenderInput} />
                            <Field name="birthday" label={i18next.t('createUserForm.birthday')}
                                    inputType="text" required={true} component={RenderDatePicker} />
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
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}              

EditUserInfoForm.propTypes = {
    username: PropTypes.string.isRequired
}

EditUserInfoForm = reduxForm({
    form: 'editUserInfo',
    destroyOnUnmount: false,
    validate
})(EditUserInfoForm)

export default EditUserInfoForm;
