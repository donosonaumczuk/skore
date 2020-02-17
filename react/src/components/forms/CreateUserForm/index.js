import React, { Component } from 'react';
import { reduxForm, change, touch } from 'redux-form';
import { Redirect } from 'react-router-dom';
import { connect } from 'react-redux';
import AuthService from '../../../services/AuthService';
import CreateUserFormValidator from '../validators/CreateUserValidator';
import UserService from '../../../services/UserService';
import CreateUserForm from './layout';
import { SC_CONFLICT } from '../../../services/constants/StatusCodesConstants';
import { EC_EMAIL_EXISTS } from '../../../services/constants/ErrorCodesConstants';
import i18next from 'i18next';
import Utils from '../../utils/Utils';

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

    getBirthdayWithCorrectFormat = (year, month, day) => {
        return {
            "year": parseInt(year),
            "monthNumber": parseInt(month),
            "dayOfMonth":  parseInt(day)
        };
    }

    loadHome = () => {
        const { country, state, city, street } = this.state
        if (!country && !state && !city && !street) {
            return null;
        }
        let home = {};
        if (country) {
            home.country = country;
        }
        if (state) {
            home.state = state;
        }
        if (city) {
            home.city = city;
        }
        if (street) {
            home.street = street;
        }
        return home;
    }

    loadUser = (values, image) => {
        const birthday = this.getBirthdayWithCorrectFormat(values.year, values.month, values.day);
        const home = this.loadHome();
        let user = {
            "username": values.username,
            "password": values.password,
            "firstName": values.firstName,
            "lastName": values.lastName,
            "email": values.email,
            "birthday": birthday,
        };
        if (image) {
            user = { ...user, "image": image.data };
        }
        if (values.cellphone) {
            user = { ...user, "cellphone": values.cellphone };
        }
        if (home) {
            user = { ...user, "home": home };
        }
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
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await UserService.createUser(user);
        if (response.status) {
            if (this.mounted) {
                if (response.status === SC_CONFLICT) {
                    if (response.data.errorCode === EC_EMAIL_EXISTS) {
                        const errorMessage = i18next.t('createUserForm.errors.emailExists');
                        this.setState({ errorMessage: errorMessage, executing: false });
                    }
                    else {
                        const errorMessage = i18next.t('createUserForm.errors.usernameExists');
                        this.setState({ errorMessage: errorMessage, executing: false });
                    }
                }
                else {
                    this.setState({ status: response.status, executing: false });
                }
            }
        }
        else {
            this.props.history.push(`/createdAccount`);
        }
    }

    getErrorMessage = () => {
        if (this.state.errorMessage) {
            return (<span className="invalid-feedback d-block">
                        {this.state.errorMessage}
                    </span>);
        }
        return <React.Fragment></React.Fragment>;
    }

    componentDidMount() {
        this.mounted = true;
    }

    render() {
        const { handleSubmit, submitting, birthday, change, touch } = this.props;
        const { country, state, city, street, executing, status } = this.state;
        const errorMessage = Utils.getErrorMessage(this.state.errorMessage);
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
                            touchField={touch} isExecuting={executing}
                            error={status} errorMessage={errorMessage} />
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
