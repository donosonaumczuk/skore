import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import { Redirect } from 'react-router-dom';
import queryString from 'query-string';
import PropTypes from 'prop-types';
import AuthService from '../../../services/AuthService';
import CreateUserFormValidator from '../validators/CreateUserValidator';
import UserService from '../../../services/UserService';
import { SC_CONFLICT } from '../../../services/constants/StatusCodesConstants';

const validate = values => {
    const errors = {}
    errors.newPassword = CreateUserFormValidator.validatePassword(values.newPassword);
    errors.repeatNewPassword = CreateUserFormValidator.validateRepeatedPassword(values.repeatNewPassword, values.newPassword);
    return errors;
}

class ResetPasswordFormContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const queryParams = queryString.parse(props.location.search);
        const { username, code } = queryParams;

        this.state = {
            executing: false,
            username: username,
            code: code
        };
    }

    onSubmit = async (values) => {
        let password = { "password": values.password };
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const { username, code } = this.state;
        const response = await UserService.resetPassword(username, password, code);
        if (response.status) {
            if (response.status === SC_CONFLICT) {
                if (this.mounted) {
                    //TODO handle error 409 add message for error
                    this.setState({ executing: false});
                } 
            }
            else {
                this.setState({ executing: false, error: response.status})
            }
        }
        else {
            this.setState({ requested: true });
        }
    }

    componentDidMount() {
        this.mounted = true;
    }

    render() {
        const { handleSubmit, submitting } = this.props;
        const { executing, error, requested } = this.state;
        const currentUser = AuthService.getCurrentUser();
        if (currentUser) {
            return <Redirect to={`/users/${currentUser}`} />
        }
        else if (requested) {
            <Redirect to="/login" />
        }
    
        return (
            <ResetPasswordForm handleSubmit={handleSubmit}
                                    submitting={submitting}
                                    onSubmit={this.onSubmit}
                                    isExecuting={executing}
                                    error={error} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}               

ResetPasswordFormContainer = reduxForm({
    form: 'resetPassword',
    destroyOnUnmount: true,
    validate,
})(ResetPasswordFormContainer)

ResetPasswordFormContainer.propTypes = {
    location: PropTypes.object.isRequired
}

export default ResetPasswordFormContainer;
