import React, { Component } from 'react';
import queryString from 'query-string';
import PropTypes from 'prop-types';
import MatchService from '../../../services/MatchService';
import ConfirmMatch from './layout';
import i18next from 'i18next';
import { SC_CONFLICT } from '../../../services/constants/StatusCodesConstants';
import { EC_ALREADY_JOINED } from '../../../services/constants/ErrorCodesConstants';


class ConfirmMatchContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const queryParams = queryString.parse(props.location.search);
        const { match, id, code } = queryParams;   
        const parsedCode = code.replace(/ /g, "+");
        this.state = {
            matchKey: match,
            id: id,
            code: parsedCode,
            loading: true,
            status: null   
        };
    }

    componentDidMount = async () => {
        this.mounted = true;
        const { matchKey, id, code } = this.state;
        const response = await MatchService.confirmAssistance(matchKey, id, code);
        if (response.status && this.mounted) {
            if (response.status === SC_CONFLICT) {
                if (response.data.errorCode === EC_ALREADY_JOINED) {
                    const errorMessage = i18next.t('confirmAssistance.alreadyJoined');
                    this.setState({ errorMessage: errorMessage, loading: false });
                }
                else {
                    const errorMessage = i18next.t('confirmAssistance.matchFullOrPlayed');
                    this.setState({ errorMessage: errorMessage, loading: false });
                }
            }
            else {
                this.setState({ status: response.status, loading: false });
            }
        }
        else {
            this.setState({ loading: false });
        }
    }

    render() {
        let message = i18next.t('confirmAssistance.confirmed');
        if (this.state.errorMessage) {
            message = this.state.errorMessage
        }
        return (
            <ConfirmMatch error={this.state.status} isLoading={this.state.loading} 
                            message={message} matchKey={this.state.matchKey} />
        );
    }

    componentWillUnmount = () => {
        this.mounted = false;
    }
}

ConfirmMatchContainer.propTypes = {
    location: PropTypes.object.isRequired
}

export default ConfirmMatchContainer;

