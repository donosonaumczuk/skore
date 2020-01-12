import React from 'react';
import { Field, reduxForm } from 'redux-form';
import FormTitle from './utils/FormTitle';
import RenderInput from './utils/RenderInput';
import SubmitButton from './utils/SubmitButton';
import i18next from 'i18next';
import SuggestionText from './utils/SuggestionText';

const onSubmit = async (values) => {
    console.table(values);
    //TODO make post
}

let LogInForm = (props) => {
    const { handleSubmit, submitting} = props; 

    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                   <FormTitle />
                    <form onSubmit={handleSubmit(onSubmit)}>
                        <Field name="username" label={i18next.t('createUserForm.username')} 
                                inputType="text" required={true} component={RenderInput} />
                        <Field name="password" label={i18next.t('createUserForm.password')}
                            inputType="password" required={true} component={RenderInput} />
                       {/* TODO remember me */}
                        <SubmitButton label={i18next.t('login.loginButton')} divStyle="text-center" buttonStyle="btn btn-green mb-2" submitting={submitting} />
                        <SuggestionText suggestion={i18next.t('login.newUser')} link="/signUp" linkText={i18next.t('createUserForm.signUp')} />
                    </form>
                </div>
            </div>
        </div>
    );
}

LogInForm = reduxForm({
    form: 'login',
    destroyOnUnmount: false, // set to true to remove data on refresh
    // validate TODO
  })(LogInForm)

export default LogInForm;