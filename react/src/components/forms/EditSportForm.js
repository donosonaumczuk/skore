import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import i18next from 'i18next';
import RenderInput from './inputs/RenderInput';
import ImageInput from './inputs/ImageInput';
import SubmitButton from './elements/SubmitButton';
import FormTitle from './elements/FormTitle';
import FormComment from './elements/FormComment';
import CreateSportValidator from './../forms/validators/CreateSportValidator';
import SportService from '../../services/SportService';

const validate = values => {
    const errors = {}
    errors.displayName = CreateSportValidator.validateDisplayName(values.displayName);
    errors.playersPerTeam = CreateSportValidator.validatePlayersPerTeam(values.playersPerTeam);
    errors.sportImage = CreateSportValidator.validateSportImage(values.sportImage);
    return errors;
}

class EditSportForm extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            image: null,
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
            "imageSport": image ? image.data : null
        };
        return sport;
    }
    
    onSubmit = async (values) => {
        let sport = this.loadSport(values, this.state.image);
        const res = await SportService.updateSport(sport);
        if (res.status && this.mounted) {
            //TODO handle errors
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
        let imageName = "";
        if (this.state.image != null) {
            imageName = this.state.image.name;
        }
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 
                                    col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                        <FormTitle />
                        <form onSubmit={handleSubmit(this.onSubmit)} >
                            <Field name="sportName" label={i18next.t('createSportForm.sportName')}
                                    id="sportName" inputType="text" required={true}
                                    component={RenderInput} isDisabled={true}/>
                            <Field name="displayName" label={i18next.t('createSportForm.displayName')}
                                    id="displayName" inputType="text" required={true} component={RenderInput} />
                            <Field name="playersPerTeam" label={i18next.t('createSportForm.playersPerTeam')}
                                    id="playersPerTeam" inputType="text" required={true} component={RenderInput} />
                            <Field name="sportImage" label={i18next.t('createSportForm.sportImage')} 
                                    type="file" imageName={imageName} acceptedFormat={i18next.t('createUserForm.imageFormat')}
                                    required={false} component={ImageInput} onChange={this.handleChange} />
                            <FormComment id="requiredHelp" textStyle="form-text text-muted mb-2"
                                            text={i18next.t('forms.requiredFields')} />
                            <SubmitButton label={i18next.t('editUserInfoForm.updateInfoButton')} 
                                            divStyle="text-center" buttonStyle="btn btn-green mb-2" 
                                            submitting={submitting} />
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

EditSportForm = reduxForm({
    form: 'editSport',
    destroyOnUnmount: true,
    validate
})(EditSportForm)

export default EditSportForm;
