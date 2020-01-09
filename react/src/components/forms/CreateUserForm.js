import React from 'react';
import { Field, reduxForm } from 'redux-form';
import RenderInput from './utils/RenderInput';
import SubmitButton from './utils/SubmitButton';

const validate = values => {
    const errors = {}
    if (!values.username) {
      errors.username = 'Required';
    }

    return errors;
}

let CreateUserForm = ({ handleSubmit, submitting }) => {
  // const usernameLabel = "" + i18next.t('createUserForm.username') + " *";
  // const createAccountLabel = i18next.t('createUserForm.createAccount');
  //TODO see why it does not work I think because the namespaces haven been loaded yet
  return ( 
    <form onSubmit={handleSubmit}>
      <Field name="username" label={"Username *"} component={RenderInput} />
      <SubmitButton label="Create" submitting={submitting} />
    </form>
  );
}

CreateUserForm = reduxForm({
  form: 'createUse',
  destroyOnUnmount: false, // set to true to remove data on refresh
  validate
})(CreateUserForm)

export default CreateUserForm;
