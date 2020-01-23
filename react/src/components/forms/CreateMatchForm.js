import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { Redirect } from 'react-router-dom';
import i18next from 'i18next';
import FormTitle from './utils/FormTitle';
import RenderInput from './utils/RenderInput';
import SubmitButton from './utils/SubmitButton';
import AuthService from '../../services/AuthService';

class CreateMatchForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            image: null,
        };
    }

    onSubmit = async (values) => {
       //TODO implement
    }

    render() {
        const { handleSubmit, submitting } = this.props; 
        const currentUser = AuthService.getCurrentUser();
        if (!currentUser) {
            return <Redirect to="/" />
        }
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                        <FormTitle />
                        <form onSubmit={handleSubmit(this.onSubmit)}>
                            <Field name="username" label={i18next.t('createMatchForm.matchName')} 
                                    inputType="text" required={true} component={RenderInput} />
                            <SubmitButton label={i18next.t('createMatchForm.createMatch')} divStyle="text-center" buttonStyle="btn btn-green mb-2" submitting={submitting} />
                        </form>
                    </div>
                </div>
            </div>
        );
    }
}

CreateMatchForm = reduxForm({
    form: 'createMatch',
    destroyOnUnmount: false, // set to true to remove data on refresh
})(CreateMatchForm)

export default CreateMatchForm;