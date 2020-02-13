import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AuthService from '../../../services/AuthService';
import SportService from '../../../services/SportService';
import EditSport from './layout';

class EditSportContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const { sportName } = this.props.match.params;
        this.state = {
            sportName: sportName,
        };
    }

    updateStateWithSport = sport => {
        if (sport.status) {
            this.setState({ status: sport.status });
        }
        else {
            this.setState({ sport: sport });
        }
    }

    async componentDidMount() {
        this.mounted = true;
        if (AuthService.isAdmin()) { 
            let sport = await SportService.getSportByName(this.state.sportName);
            if (this.mounted) {
                this.updateStateWithSport(sport);
            }
        }
    }

    loadFormInitialValues = () => {
        const { sport } = this.state;
        if (sport) {
            return {
                "sportName": sport.sportName,
                "displayName": sport.displayName,
                "playersPerTeam": sport.playerQuantity
            };
        }
        return {};
    }

    render() {
        const isAdmin = AuthService.isAdmin();
        const formInitialValues = this.loadFormInitialValues();
        const isLoading = !this.state.sport;
        let error = this.state.status;
        const needsPermission = !isAdmin;
        return (
            <EditSport initialValues={formInitialValues}
                        history={this.props.history}
                        isLoading={isLoading} error={error}
                        needsPermission={needsPermission} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

EditSportContainer.propTypes = {
    match: PropTypes.object.isRequired,
    history: PropTypes.object.isRequired
}

export default EditSportContainer;