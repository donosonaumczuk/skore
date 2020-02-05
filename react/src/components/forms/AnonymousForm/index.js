import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import CreateUserFormValidator from '../validators/CreateUserValidator';
import AnonymousForm from './layout';
import MatchService from '../../../services/MatchService';

const validate = values => {
    const errors = {}
    errors.firstName = CreateUserFormValidator.validateFirstName(values.firstName);
    errors.lastName = CreateUserFormValidator.validateLastName(values.lastName);
    errors.email = CreateUserFormValidator.validateEmail(values.email);
    return errors;
}

class AnonymousFormContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            created: false
        };
    }

    onSubmit = async (values) => {
        const { currentMatch } = this.props;
        const user =  {
            "firstName": values.firstName,
            "lastName": values.lastName,
            "email": values.email
        };
        const response = await MatchService.joinMatchAnonymous(currentMatch.key, user);
        
        if (response.status) {
            this.setState({ status: response.status })
        }
        else {
           this.setState({ created: true });
        }
    }

    render() {
        const { handleSubmit, submitting } = this.props; 
        console.log(this.state.created);
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