import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import i18next from 'i18next';
import FormTitle from './inputs/FormTitle';
import RenderInput from './inputs/RenderInput';
import ImageInput from './inputs/ImageInput';
import SubmitButton from './inputs/SubmitButton';
import FormComment from './inputs/FormComment';
import CreateSportValidator from './validators/CreateSportValidator';
import AuthService from '../../services/AuthService';
import SportService from '../../services/SportService';
import ErrorPage from './../screens/ErrorPage';
import { SC_FORBIDDEN, SC_CONFLICT } from './../../services/constants/StatusCodesConstants';

const validate = values => {
    const errors = {}
    errors.sportName = CreateSportValidator.validateSportName(values.sportName);
    errors.displayName = CreateSportValidator.validateDisplayName(values.displayName);
    errors.playersPerTeam = CreateSportValidator.validatePlayersPerTeam(values.playersPerTeam);
    errors.sportImage = CreateSportValidator.validateRequiredSportImage(values.sportImage);
    return errors;
}

class CreateSportForm extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            image: null,
            error: null
        };
    }

    handleChange = (image) => {
        if (image && image.size > 0) {
            let reader = new FileReader();
            reader.readAsDataURL(image);
            reader.onload = (e) => {
                const data = (e.target.result);
                this.setState ({
                    image: {
                        name: image.name,
                        type: image.type,
                        size: image.size,
                        data: data
                    }
                });
            }
        }
        else {
            this.setState ({
                image: null
            });
        }
    }

    loadSport = (values, image) => {
        const sport = {
            "sportName": values.sportName,
            "displayName": values.displayName,
            "playerQuantity": values.playersPerTeam,
            "imageSport": image.data
        };
        return sport;
    }

    onSubmit = async (values) => {
        let sport = this.loadSport(values, this.state.image);
        const res = await SportService.createSport(sport);
        if (res.status && this.mounted) {
            if (res.status === SC_CONFLICT) {
                this.setState({ sportNameError: true });
            }
            else {
                this.setState({ error: res.status });
            }
        }
        else {
            this.props.history.push(`/sports`);
        }
    } 

    componentDidMount() {
        this.mounted = true;
    }

    render() {
        const { handleSubmit, submitting } = this.props; 
        const isAdmin = AuthService.isAdmin();
        let imageName = "";
        if (!isAdmin || this.state.error) {
            return <ErrorPage status={!isAdmin ? SC_FORBIDDEN : this.state.error} />
        }
        else if (this.state.image != null) {
            imageName = this.state.image.name;
        }
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                        <FormTitle />
                        <form onSubmit={handleSubmit(this.onSubmit)}>
                            <Field name="sportName" label={i18next.t('createSportForm.sportName')}
                                    id="sportName" inputType="text" required={true} component={RenderInput} />
                                    {this.state.sportNameError && 
                                    <span className="invalid-feedback d-block">
                                        {i18next.t('createSportForm.errors.sportName.alreadyExists')}
                                    </span> }
                            <Field name="displayName" label={i18next.t('createSportForm.displayName')}
                                    id="displayName" inputType="text" required={true} component={RenderInput} />
                            <Field name="playersPerTeam" label={i18next.t('createSportForm.playersPerTeam')}
                                    id="playersPerTeam" inputType="text" required={true} component={RenderInput} />
                            <Field name="sportImage" label={i18next.t('createSportForm.sportImage')} type="file"
                                imageName={imageName} acceptedFormat={i18next.t('createUserForm.imageFormat')}
                                required={true} component={ImageInput} checkOnSubmit={true} onChange={this.handleChange} />
                            <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2" text={i18next.t('forms.requiredFields')} />
                            <SubmitButton label={i18next.t('createSportForm.createSportButton')} divStyle="text-center" buttonStyle="btn btn-green mb-2" submitting={submitting} />
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

CreateSportForm = reduxForm({
    form: 'createSport',
    destroyOnUnmount: false, // set to true to remove data on refresh
    validate
})(CreateSportForm) 


export default CreateSportForm;