import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import CreateUserFormValidator from '../validators/CreateUserValidator';
import UserService from '../../../services/UserService';
import ErrorPage from '../../screens/ErrorPage';
import EditUserInfoForm from './layout';

const validate = values => {
    const errors = {}
    errors.firstName = CreateUserFormValidator.validateFirstName(values.firstName);
    errors.lastName = CreateUserFormValidator.validateLastName(values.lastName);
    errors.image = CreateUserFormValidator.validateImage(values.image);
    errors.cellphone = CreateUserFormValidator.validateCellphone(values.cellphone);
    errors.birthday = CreateUserFormValidator.validateDate(values.birthday);
    return errors;
}

class EditUserInfoFormContainer extends Component {
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
        let user = {
            "firstName": values.firstName,
            "lastName": values.lastName,
            "email": values.email,
            "cellphone": values.cellphone ? values.cellphone : null ,
            "birthday": birthday
        };
        if (image && image.size > 0) {
            user = {
                ...user,
                "image": image.data
            };
        }
        return user;
    }
    
    onSubmit = async (values) => {
        let user = this.loadUser(values, this.state.image);
        const response = await UserService.updateUser(user, this.state.username);
        if (response.status) {
            this.setState({ error: response.status });
        }
        else {
            this.props.history.push(`/${this.state.username}`);
        }
    }
    
    render() {
        const { handleSubmit, submitting } = this.props;
        let imageName = "";
        if (this.state.error) {
            return <ErrorPage status={this.state.error} />; //TODO maybe improve with hoc
        }
        if (this.state.image != null) {
            imageName = this.state.image.name;
        }
        return (
            <EditUserInfoForm handleSubmit={handleSubmit} submitting={submitting}
                                onSubmit={this.onSubmit} imageName={imageName}
                                handleChange={this.handleChange} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}              

EditUserInfoFormContainer.propTypes = {
    username: PropTypes.string.isRequired
}

EditUserInfoFormContainer = reduxForm({
    form: 'editUserInfo',
    destroyOnUnmount: true,
    validate
})(EditUserInfoFormContainer)

export default EditUserInfoFormContainer;
