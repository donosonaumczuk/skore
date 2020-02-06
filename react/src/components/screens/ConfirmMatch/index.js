import React, { Component } from 'react';
import queryString from 'query-string';
import PropTypes from 'prop-types';
import MatchService from '../../../services/MatchService';
import ConfirmMatch from './layout';
import i18next from 'i18next';

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
            this.setState({ status: response.status, loading: false });
        }
        else {
            this.setState({ loading: false });
        }
    }

    render() {
        return (
            <ConfirmMatch error={this.state.status} isLoading={this.state.loading} 
                            message={i18next.t('confirmAsistance.confirmed')}
                            matchKey={this.state.matchKey} />
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

