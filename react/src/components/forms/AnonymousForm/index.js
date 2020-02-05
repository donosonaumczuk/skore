import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import CreateUserFormValidator from '../validators/CreateUserValidator';
import AnonymousForm from './layout';

const validate = values => {
    const errors = {}
    errors.firstName = CreateUserFormValidator.validateFirstName(values.firstName);
    errors.lastName = CreateUserFormValidator.validateLastName(values.lastName);
    errors.email = CreateUserFormValidator.validateEmail(values.email);
    return errors;
}

class AnonymousFormContainer extends Component {
    onSubmit = async (values) => {
        const { currentMatch } = this.props;
        console.log(currentMatch);
        // const response = await MatchService.joinMatchAnonymous(
            // {
            //     "firstName": values.firstName,
            //     "lastName": values.lastName,
            //     "email": values.email
            // }
        // );
        const user =  {
            "firstName": values.firstName,
            "lastName": values.lastName,
            "email": values.email
        };
        console.log(user);
        // if (response.status) {
        //     this.setState({ status: response.status })
        // }
        // else {
           
        // }
    }

    render() {
        const { handleSubmit, submitting } = this.props; 
        return (
            <AnonymousForm onSubmit={this.onSubmit} handleSubmit={handleSubmit}
                            submitting={submitting} />
        );
    }
}

AnonymousFormContainer = reduxForm({
    form: 'anonymous',
    destroyOnUnmount: true,
    validate
})(AnonymousFormContainer) 

export default AnonymousFormContainer;