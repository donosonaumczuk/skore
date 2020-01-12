import React from 'react';
import { Field, reduxForm } from 'redux-form';
import FormTitle from './utils/FormTitle';
import i18next from 'i18next';


let SignInForm = () => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                   <FormTitle />
                    <form onSubmit={handleSubmit(this.onSubmit)}>
                        <Field name="username" label={i18next.t('createUserForm.username')} 
                                inputType="text" required={true} component={RenderInput} />
                        <Field name="password" label={i18next.t('createUserForm.password')}
                            inputType="password" required={true} component={RenderInput} />
                    </form>
                </div>
            </div>
        </div>
    );
}

SignInForm = reduxForm({
    form: 'createUse',
    destroyOnUnmount: false, // set to true to remove data on refresh
    // validate TODO
  })(SignInForm)

export default SignInForm;