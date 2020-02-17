import React, { Component } from 'react';
import  { Redirect } from 'react-router-dom'; 
import PropTypes from 'prop-types';
import AuthService from '../../../services/AuthService';
import Spinner from '../../Spinner';
import MatchService from '../../../services/MatchService';
import WithAuthentication from '../../hocs/WithAuthenticatication';
import { SC_UNAUTHORIZED, SC_OK, SC_CONFLICT } from '../../../services/constants/StatusCodesConstants';
import { EC_ALREADY_JOINED } from '../../../services/constants/ErrorCodesConstants';
import i18next from 'i18next';
import Message from '../../Message';

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
            if (response.status === SC_UNAUTHORIZED) {
                const status = AuthService.internalLogout();
                if (status === SC_OK) {
                    this.props.history.push(`/login`);
                }
                else {
                    this.setState({ error: status });
                }
            }
            else if (this.mounted) {
                if (response.status === SC_CONFLICT) {
                    if (response.data.errorCode === EC_ALREADY_JOINED) {
                        const errorMessage = i18next.t('confirmAssistance.alreadyJoined');
                        this.setState({ errorMessage: errorMessage });
                    }
                    else {
                        const errorMessage = i18next.t('confirmAssistance.matchFullOrPlayed');
                        this.setState({ errorMessage: errorMessage });
                    }
                }
                else {
                    this.setState({ error: response.status, executing: false });
                }
            }
        }
        else {
            this.props.history.push(`/match/${matchKey}`);
        }
    }

    render() {
        if (this.state.errorMessage) {
            return <Message message={this.state.errorMessage} />;
        }
        else if (this.state.status) {
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