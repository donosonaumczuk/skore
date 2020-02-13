import React, { Component } from 'react';
import { connect } from 'react-redux';
import { reduxForm, change, touch, formValueSelector } from 'redux-form';
import PropTypes from 'prop-types';
import CreateUserFormValidator from '../validators/CreateUserValidator';
import UserService from '../../../services/UserService';
import EditUserInfoForm from './layout';

const validate = values => {
    const errors = {}
    errors.firstName = CreateUserFormValidator.validateFirstName(values.firstName);
    errors.lastName = CreateUserFormValidator.validateLastName(values.lastName);
    errors.image = CreateUserFormValidator.validateImage(values.image);
    errors.cellphone = CreateUserFormValidator.validateCellphone(values.cellphone);
    errors.year = CreateUserFormValidator.validateYear(values.year);
    errors.month = CreateUserFormValidator.validateMonth(values.month);
    errors.day = CreateUserFormValidator.validateDay(values.day);
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
        let newBirthday = {
            "year": parseInt(birthdayInfo[2]),
            "monthNumber": parseInt(birthdayInfo[0]),
            "dayOfMonth":  parseInt(birthdayInfo[1])
        }
        return newBirthday;
    }

    loadHome = home => {
        if (home) {
            const { country, state, city, street } = home;
            return {
                "country": country ? country : "",
                "state": state ? state : "",
                "city": city ? city : "",
                "street": street ? street : ""
            };
        }
        return null;
    }

    loadUser = (values, image) => {
        const birthday = this.getBirthdayWithCorrectFormat(values.birthday);
        const home = this.loadHome(values.home);
        let user = {
            "firstName": values.firstName,
            "lastName": values.lastName,
            "email": values.email,
            "cellphone": values.cellphone ? values.cellphone : null ,
            "birthday": birthday
        };
        if (image && image.size > 0) {
            user = { ...user, "image": image.data };
        }
        if (values.home) {
            user = { ...user, "home": home};
        }
        return user;
    }
    
    onSubmit = async (values) => {
        let user = this.loadUser(values, this.state.image);
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await UserService.updateUser(user, this.state.username);
        if (response.status) {
            if (this.mounted) {
                this.setState({ error: response.status, executing: false });
            }
        }
        else {
            this.props.history.push(`/users/${this.state.username}`);
        }
    }
    
    render() {
        const { handleSubmit, submitting, home, birthday, change, touch } = this.props;
        let imageName = "";
        if (this.state.image != null) {
            imageName = this.state.image.name;
        }
        return (
            <EditUserInfoForm handleSubmit={handleSubmit} submitting={submitting}
                                onSubmit={this.onSubmit} imageName={imageName}
                                changeFieldsValue={change} touchField={touch}
                                handleChange={this.handleChange} 
                                home={home} birthday={birthday}
                                isExecuting={this.state.executing}
                                error={this.state.error} />
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
    validate,
    change,
    touch
})(EditUserInfoFormContainer)

const selector = formValueSelector('editUserInfo');

EditUserInfoFormContainer = connect(state => {    
    let home = selector(state, 'home');
    const year = selector(state, 'year');
    const month = selector(state, 'month');
    const day = selector(state, 'day');
    if (!home) {
        home = {
            "country": null,
            "state": null,
            "city": null,
            "street": null,    
        }
    }
    return {
        home: home,
        birthday: {
            "year": year,
            "month": month,
            "day": day
        }
    }
})(EditUserInfoFormContainer)  

export default EditUserInfoFormContainer;
