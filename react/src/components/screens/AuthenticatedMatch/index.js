import React, { Component } from 'react';
import  { Redirect } from 'react-router-dom'; 
import PropTypes from 'prop-types';
import AuthService from '../../../services/AuthService';
import Spinner from '../../Spinner';
import MatchService from '../../../services/MatchService';
import WithAuthentication from '../../hocs/WithAuthenticatication';

class AuthenticatedMatch extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const { param } = this.props;
        this.state = {
            matchKey: param,
            status: null
        }
    }

    componentDidMount = async () => {
        this.mounted = true;
        const userId = AuthService.getUserId();
        const { matchKey } = this.state;
        const response = await MatchService.joinMatchWithAccount(matchKey, userId);
        if (response.status) {
            if (this.mounted) {
                this.setState({ status: response.status });
            }
        }
        else {
            this.props.history.push(`/match/${matchKey}`);
        }
    }

    render() {
        if (this.state.status) {
            return <Redirect to={`/error/${this.state.status}`} />;
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
    param: PropTypes.string.isRequired,
    history: PropTypes.object.isRequired,
}

export default WithAuthentication(AuthenticatedMatch);