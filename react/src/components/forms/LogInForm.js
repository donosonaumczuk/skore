import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { Redirect } from 'react-router-dom';
import Proptypes from 'prop-types';
import i18next from 'i18next';
import FormTitle from './inputs/FormTitle';
import RenderInput from './inputs/RenderInput';
import SubmitButton from './inputs/SubmitButton';
import SuggestionText from './inputs/SuggestionText';
import AuthService from './../../services/AuthService';
import LogInValidator from './validators/LogInValidator';
import FormComment from './inputs/FormComment';
import { SC_UNAUTHORIZED } from './../../services/constants/StatusCodesConstants';

const validate = values => {
    const errors = {}
    errors.username = LogInValidator.validateUsername(values.username);
    errors.password =  LogInValidator.validatePassword(values.password);
    return errors;
}

class LogInForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            errorMessage: null,
        };
    }

    onSubmit = async (values) => {
        const response = await AuthService.logInUser(
            {
                "username": values.username,
                "password": values.password
            }
        );
        if (response.status === SC_UNAUTHORIZED) {
            const errorMessage = i18next.t('login.errors.invalidUsernameOrPassword')
            this.setState({ errorMessage: errorMessage })
        }
        else {
            //TODO handle other error status maybe?
            this.props.updateUser(values.username);
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

    render() {
        const { handleSubmit, submitting } = this.props; 
        const currentUser = AuthService.getCurrentUser();
        const errorMessage = this.getErrorMessage();
        if (currentUser) {
            return <Redirect to={`/users/${currentUser}`} />
        }
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                        <FormTitle />
                        <form onSubmit={handleSubmit(this.onSubmit)}>
                            {errorMessage}
                            <Field name="username" label={i18next.t('createUserForm.username')}
                            id="username" inputType="text" required={true} component={RenderInput} />
                            <Field name="password" label={i18next.t('createUserForm.password')}
                                inputType="password" required={true} component={RenderInput} />
                            {/* TODO remember me */}
                            <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2" text={i18next.t('forms.requiredFields')} />
                            <SubmitButton label={i18next.t('login.loginButton')} divStyle="text-center" buttonStyle="btn btn-green mb-2" submitting={submitting} />
                            <SuggestionText suggestion={i18next.t('login.newUser')} link="/signUp" linkText={i18next.t('createUserForm.signUp')} />
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

LogInForm = reduxForm({
    form: 'login',
    destroyOnUnmount: false, // set to true to remove data on refresh
    validate
})(LogInForm) 

LogInForm.propTypes = {
    updateUser: Proptypes.func.isRequired
}

export default LogInForm;