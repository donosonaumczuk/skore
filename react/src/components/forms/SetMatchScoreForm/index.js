import React, { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import AuthService from '../../../services/AuthService';
import MatchService from '../../../services/MatchService';
import SetMatchScoreForm from './layout';
import SetMatchScoreValidator from '../validators/SetMatchScoreValidator';

const validate = values => {
    const errors = {}
    errors.teamOneScore = SetMatchScoreValidator.validateTeamScore(values.teamOneScore);
    errors.teamTwoScore = SetMatchScoreValidator.validateTeamScore(values.teamTwoScore);
    return errors;
}

class SetMatchScoreFormContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const { creator, matchKey } = props.match.params;
        this.state = {
            creator: creator,
            matchKey: matchKey
        };
    }

    onSubmit = async (values) => {
        const score = {
            "scoreTeam1": parseInt(values.teamOneScore),
            "scoreTeam2": parseInt(values.teamTwoScore)
        }
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await MatchService.setScore(this.state.matchKey, score);
        console.log(response);
        if (response.status) {
            if (this.mounted) {
                this.setState({ status: response.status, executing: false })
            }
        }
        else {
            this.props.history.push(`/match/${this.state.matchKey}`);
        }
    }

    componentDidMount() {
        this.mounted = true;
    }

    render() {
        const { handleSubmit, submitting } = this.props; 
        const currentUser = AuthService.getCurrentUser();
        let needsPermission = currentUser !== this.state.creator;
        needsPermission = false;
        return (
            <SetMatchScoreForm onSubmit={this.onSubmit} handleSubmit={handleSubmit}
                                submitting={submitting} isExecuting={this.state.executing}
                                error={this.state.status} needsPermission={needsPermission} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

SetMatchScoreFormContainer.propTypes = {
    history: PropTypes.object.isRequired,
    match: PropTypes.object.isRequired
}

SetMatchScoreFormContainer = reduxForm({
    form: 'setMatchScore',
    destroyOnUnmount: true,
    validate
})(SetMatchScoreFormContainer) 

export default SetMatchScoreFormContainer;