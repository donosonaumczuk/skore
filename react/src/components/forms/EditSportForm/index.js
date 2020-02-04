import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import CreateSportValidator from '../validators/CreateSportValidator';
import SportService from '../../../services/SportService';
import EditSportForm from './layout';

const validate = values => {
    const errors = {}
    errors.displayName = CreateSportValidator.validateDisplayName(values.displayName);
    errors.playersPerTeam = CreateSportValidator.validatePlayersPerTeam(values.playersPerTeam);
    errors.sportImage = CreateSportValidator.validateSportImage(values.sportImage);
    return errors;
}

class EditSportFormContainer extends Component {
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
            <EditSportForm handleSubmit={handleSubmit} submitting={submitting}
                            onSubmit={this.onSubmit} imageName={imageName}
                            handleChange={this.handleChange} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}              

EditSportFormContainer = reduxForm({
    form: 'editSport',
    destroyOnUnmount: true,
    validate
})(EditSportFormContainer)

export default EditSportFormContainer;
