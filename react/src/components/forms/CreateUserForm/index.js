import React, { Component } from 'react';
import { reduxForm, change, touch } from 'redux-form';
import { Redirect } from 'react-router-dom';
import { connect } from 'react-redux';
import AuthService from '../../../services/AuthService';
import CreateUserFormValidator from '../validators/CreateUserValidator';
import UserService from '../../../services/UserService';
import CreateUserForm from './layout';

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
    errors.year = CreateUserFormValidator.validateYear(values.year);
    errors.month = CreateUserFormValidator.validateMonth(values.month);
    errors.day = CreateUserFormValidator.validateDay(values.day);
    return errors;
}

class CreateUserFormContainer extends Component {
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
        let newBirthday = {
            "year": parseInt(birthdayInfo[2]),
            "monthNumber": parseInt(birthdayInfo[0]),
            "dayOfMonth":  parseInt(birthdayInfo[1])
        }
        return newBirthday;
    }

    loadUser = (values, image) => {
        const birthday = this.getBirthdayWithCorrectFormat(values.birthday);
        let user = {
            "username": values.username,
            "password": values.password,
            "firstName": values.firstName,
            "lastName": values.lastName,
            "email": values.email,
            "cellphone": values.cellphone ? values.cellphone : null ,
            "birthday": birthday,
            "home": {
                "country": this.state.country,
                "state": this.state.state,
                "city": this.state.city,
                "street": this.state.street
            }
        };
        if (image) {
            user = { ...user, "image":image.data };
        }
        console.log(user);
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
            this.props.history.push(`/createdAccount`);
        }
    }

    componentDidMount() {
        this.mounted = true;
    }

    render() {
        const { handleSubmit, submitting, birthday, change, touch } = this.props;
        const { country, state, city, street } = this.state;
        let imageName = "";
        const currentUser = AuthService.getCurrentUser();
        if (currentUser) {
            return <Redirect to={`/users/${currentUser}`} />
        }
        if (this.state.image != null) {
            imageName = this.state.image.name;
        }
    
        return (
            <CreateUserForm handleSubmit={handleSubmit} submitting={submitting}
                            onSubmit={this.onSubmit} imageName={imageName}
                            handleChange={this.handleChange}
                            updateLocation={this.updateLocation}
                            country={country} state={state} city={city}
                            street={street} birthday={birthday} 
                            changeFieldsValue={change}
                            touchField={touch} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}               

const mapStateToProps = (state) => {
    const { values } = state.form.createUser;
    if (values) {
        return (
            {
                birthday: {
                    year: values.year,
                    month: values.month,
                    day: values.day,
                }
            }
        );
    }
    else {
        return {
            birthday: {
                year: null,
                month: null,
                day: null,
            }
        };
    }
};

CreateUserFormContainer = connect(
    mapStateToProps,
    null
)(CreateUserFormContainer);

CreateUserFormContainer = reduxForm({
    form: 'createUser',
    destroyOnUnmount: true,
    validate,
    change,
    touch
})(CreateUserFormContainer)

export default CreateUserFormContainer;
