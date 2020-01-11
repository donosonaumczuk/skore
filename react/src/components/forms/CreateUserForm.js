import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import RenderInput from './utils/RenderInput';
import ImageInput from './utils/ImageInput';
import SubmitButton from './utils/SubmitButton';

const validate = values => {
    const errors = {}
    if (!values.username) {
      errors.username = 'Required';
    }
    console.log(values.username);

    console.table(values.image);

    return errors;
}

const handleChange = (image) => {
  console.table(image);
  let reader = new FileReader();
  reader.readAsDataURL(image);
  reader.onload = (e) => {
    console.log("image data");
    console.log(e.target.result);
  }

}

class CreateUserForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      image: null
    };
  }
  
  render() {
    const { handleSubmit, submitting} = this.props;
    let imageName = "";
    if(this.state.image != null) {
      imageName = this.state.image.name;
    }
    // const usernameLabel = "" + i18next.t('createUserForm.username') + " *";
    // const createAccountLabel = i18next.t('createUserForm.createAccount');
    //TODO see why it does not work I think because the namespaces haven been loaded yet
    return (
      <div className="container-fluid">
        <div className="row">
          <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
            <div className="row text-center">
                <div className="col">
                    <a className="sign-in-brand" href="/">sk<i className="fas fa-bullseye"></i>re</a>
                </div>
            </div>
            <form onSubmit={handleSubmit}>
              <Field name="username" label="Username " inputType="text" required={true} component={RenderInput} />
              <Field name="password" label="Password " inputType="password" required={true} component={RenderInput} />
              <Field name="repeatPassword" label="Repeat Password " inputType="password" required={true} component={RenderInput} />
              <Field name="firstName" label="First Name " inputType="text" required={true} component={RenderInput} />
              <Field name="lastName" label="Last Name " inputType="text" required={true} component={RenderInput} />
              <Field name="email" label="Email " inputType="text" required={true} component={RenderInput} />
              <Field name="image" label="Profile Picture" type="file" imageName={imageName} acceptedFormat="Accepted formats: png, jpeg or jpg" component={ImageInput} onChange={handleChange} />
              <SubmitButton label="Create" submitting={submitting} />
            </form>
          </div>
        </div>
      </div>
    );
  }
}

// let CreateUserForm = ({ handleSubmit, submitting }) => {
//   // const usernameLabel = "" + i18next.t('createUserForm.username') + " *";
//   // const createAccountLabel = i18next.t('createUserForm.createAccount');
//   //TODO see why it does not work I think because the namespaces haven been loaded yet
//   return (
//     <div className="container-fluid">
//       <div className="row">
//         <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
//           <div className="row text-center">
//               <div className="col">
//                   <a className="sign-in-brand" href="/">sk<i className="fas fa-bullseye"></i>re</a>
//               </div>
//           </div>
//           <form onSubmit={handleSubmit}>
//             <Field name="username" label="Username " inputType="text" required={true} component={RenderInput} />
//             <Field name="password" label="Password " inputType="password" required={true} component={RenderInput} />
//             <Field name="repeatPassword" label="Repeat Password " inputType="password" required={true} component={RenderInput} />
//             <Field name="firstName" label="First Name " inputType="text" required={true} component={RenderInput} />
//             <Field name="lastName" label="Last Name " inputType="text" required={true} component={RenderInput} />
//             <Field name="email" label="Email " inputType="text" required={true} component={RenderInput} />
//             <Field name="image" label="Profile Picture" type="file" acceptedFormat="Accepted formats: png, jpeg or jpg" component={FileInput} onChange={handleChange} />
//             <SubmitButton label="Create" submitting={submitting} />
//           </form>
//         </div>
//       </div>
//     </div>
//   );
// }

CreateUserForm = reduxForm({
  form: 'createUse',
  destroyOnUnmount: false, // set to true to remove data on refresh
  validate
})(CreateUserForm)

export default CreateUserForm;
