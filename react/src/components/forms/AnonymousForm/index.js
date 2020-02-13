import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import CreateUserFormValidator from '../validators/CreateUserValidator';
import AnonymousForm from './layout';
import MatchService from '../../../services/MatchService';
import Message from '../../Message';
import i18next from 'i18next';

const validate = values => {
    const errors = {}
    errors.firstName = CreateUserFormValidator.validateFirstName(values.firstName);
    errors.lastName = CreateUserFormValidator.validateLastName(values.lastName);
    errors.email = CreateUserFormValidator.validateEmail(values.email);
    return errors;
}

class AnonymousFormContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            created: false
        };
    }

    componentDidMount() {
        this.mounted = true;
    }

    onSubmit = async (values) => {
        const { currentMatch } = this.props;
        const user =  {
            "firstName": values.firstName,
            "lastName": values.lastName,
            "email": values.email
        };
        if (this.mounted) {
            this.setState({ isLoading: true });
        }
        const response = await MatchService.joinMatchAnonymous(currentMatch.key, user);
        if (this.mounted) {
            if (response.status) {
                this.setState({ status: response.status })
            }
            else {
                this.setState({ created: true });
            }
        }
    }

    render() {
        const { handleSubmit, submitting } = this.props; 
        if (this.state.created) {
            return (
                <Message message={i18next.t('confirmAsistance.confirm')} />
            );
        }
        return (
            <AnonymousForm onSubmit={this.onSubmit} handleSubmit={handleSubmit}
                            submitting={submitting} error={this.state.status}
                            isLoading={this.state.isLoading} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

AnonymousFormContainer = reduxForm({
    form: 'anonymous',
    destroyOnUnmount: true,
    validate
})(AnonymousFormContainer) 

export default AnonymousFormContainer;