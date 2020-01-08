import React from 'react'
import { Field, reduxForm } from 'redux-form'

const validate = values => {
    const errors = {}
    if (!values.username) {
      errors.username = 'Required';
    }

    return errors;
}

const createRenderer = render => ({ input, meta, label, ...rest }) =>
 
 <div >
    {/* className={[
      meta.error && meta.touched ? 'error' : '',
      meta.active ? 'active' : ''
    ].join(' ')} add class we want to style error*/}
    <label>
    {console.log(meta)}
      {label}
    </label>
    <div>
      {render(input, label, rest)}
      {meta.error &&
        meta.touched &&
        <span>
          {meta.error}
        </span>}
      </div>
  </div>


const RenderInput = createRenderer((input, label) =>
  <input {...input} placeholder={label} />
)

let CreateUserForm = ({ handleSubmit, submitting }) => {
  return ( 
    <form onSubmit={handleSubmit}>
      <Field name="username" label="Username *" component={RenderInput} />
      <div>
        <button type="submit" disabled={submitting}>
          Create
        </button>
      </div>
    </form>
  );
}

CreateUserForm = reduxForm({
  form: 'createUse',
  destroyOnUnmount: false, // set to true to remove data on refresh
  validate
})(CreateUserForm)

export default CreateUserForm;
