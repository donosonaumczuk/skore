import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import CreateSportForm from './layout';
import CreateSportValidator from '../validators/CreateSportValidator';
import AuthService from '../../../services/AuthService';
import SportService from '../../../services/SportService';
import { SC_FORBIDDEN, SC_CONFLICT, SC_UNAUTHORIZED, SC_OK } from '../../../services/constants/StatusCodesConstants';

const validate = values => {
    const errors = {}
    errors.sportName = CreateSportValidator.validateSportName(values.sportName);
    errors.displayName = CreateSportValidator.validateDisplayName(values.displayName);
    errors.playersPerTeam = CreateSportValidator.validatePlayersPerTeam(values.playersPerTeam);
    errors.sportImage = CreateSportValidator.validateRequiredSportImage(values.sportImage);
    return errors;
    
}

class CreateSportFormContainer extends Component {
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
        const response = await SportService.createSport(sport);
        if (response.status && this.mounted) {
            if (response.status === SC_CONFLICT) {
                this.setState({ sportNameError: true });
            }
            if (response.status === SC_UNAUTHORIZED) {
                const status = AuthService.internalLogout();
                if (status === SC_OK) {
                    this.props.history.push(`/login`);
                }
                else {
                    this.setState({ error: status });
                }
            }
            else {
                this.setState({ error: response.status, executing: false });
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
        const { sportNameError } = this.state;
        const isAdmin = AuthService.isAdmin();
        let imageName = "";
        let error;
        if (!isAdmin || this.state.error) {
            error = !isAdmin ? SC_FORBIDDEN : this.state.error;
        }
        if (this.state.image != null) {
            imageName = this.state.image.name;
        }
        return (
            <CreateSportForm handleSubmit={handleSubmit} submitting={submitting}
                                onSubmit={this.onSubmit} sportNameError={sportNameError}
                                imageName={imageName} handleChange={this.handleChange}
                                error={error} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

CreateSportFormContainer = reduxForm({
    form: 'createSport',
    destroyOnUnmount: true,
    validate
})(CreateSportFormContainer) 

export default CreateSportFormContainer;