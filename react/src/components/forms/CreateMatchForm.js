import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import { Redirect } from 'react-router-dom';
import i18next from 'i18next';
import FormTitle from './inputs/FormTitle';
import RenderInput from './inputs/RenderInput';
import SubmitButton from './inputs/SubmitButton';
import AuthService from '../../services/AuthService';
import FormComment from './inputs/FormComment';
import RenderSelect from './inputs/SelectInput';
import SportService from '../../services/SportService';
import Loader from '../Loader';

class CreateMatchForm extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            image: null,
            sports: null,
        };
    }

    onSubmit = async (values) => {
       //TODO implement
    }

    componentDidMount = async () => {
        this.mounted = true;
        let response = await SportService.getSports();
        if (response.status) {
            //TODO handle error
        }
        else if (this.mounted) {
            //TODO find a way to request all sports
            this.setState({
                sports: response.sports
            });
        }
    }

    generateSportOptions = () => {
        return (
            this.state.sports.map( sport => 
                <option key={sport.sportName} value={sport.sportName}>
                    {sport.displayName}
                </option>
            )
        );
    }

    render() {
        const { handleSubmit, submitting } = this.props; 
        const currentUser = AuthService.getCurrentUser();
        if (!currentUser) {
            return <Redirect to="/" />
        }
        else if (!this.state.sports) {
            return <Loader />
        }
        const sportOptions = this.generateSportOptions();
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid create-match-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                        <FormTitle />
                        <form onSubmit={handleSubmit(this.onSubmit)}>
                            <Field name="title" label={i18next.t('createMatchForm.matchName')} 
                                    inputType="text" required={true} component={RenderInput} />
                            <Field name="sport" label={i18next.t('createMatchForm.sport')} 
                                    inputType="text" required={true} defaultText={i18next.t('createMatchForm.chooseSport')}
                                    options={sportOptions} component={RenderSelect} />
                            <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2" text={i18next.t('forms.requiredFields')} />
                            <SubmitButton label={i18next.t('createMatchForm.createMatch')} divStyle="text-center" buttonStyle="btn btn-green mb-2" submitting={submitting} />
                        </form>

                    </div>
                </div>
            </div>
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

CreateMatchForm = reduxForm({
    form: 'createMatch',
    destroyOnUnmount: false, // set to true to remove data on refresh
})(CreateMatchForm)

export default CreateMatchForm;