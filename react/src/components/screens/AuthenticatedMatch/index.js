import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AuthService from '../../../services/AuthService';
import Spinner from '../../Spinner';
import MatchService from '../../../services/MatchService';
import ErrorPage from '../ErrorPage';
import HomeMatchPropType from '../../../proptypes/HomeMatchPropType';
import WithAuthentication from '../../hocs/WithAuthenticatication';

class AuthenticatedMatch extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const { match } = this.props;
        this.state = {
            match: match,
            status: null
        }
    }

    componentDidMount = async () => {
        this.mounted = true;
        const userId = AuthService.getUserId();
        const { match } = this.state;
        const response = MatchService.joinMatchWithAccount(match.key, userId);
        if (response.status) {
            if (this.mounted) {
                this.setState({ status: response.status });
            }
        }
        else {
            this.props.history.push(`/match/${match.key}`);
        }
    }

    render() {
        if (this.state.status) {
            return <ErrorPage status = {this.state.status} />
        }
        return (
            <Spinner />
        )
    }

    componentWillUnmount = () => {
        this.mounted = false;
    }
}

AuthenticatedMatch.propTypes = {
    match: HomeMatchPropType.isRequired,
    history: PropTypes.object.isRequired,
}

export default WithAuthentication(AuthenticatedMatch);