import React, { Component } from 'react';
import queryString from 'query-string';
import PropTypes from 'prop-types';
import MatchService from '../../../services/MatchService';
import ConfirmMatch from './layout';

class ConfirmMatchContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const queryParams = queryString.parse(props.location.search);
        const { match, id, code } = queryParams;    
        this.state = {
            matchKey: match,
            id: id,
            code: code,
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
            <ConfirmMatch error={this.state.status} isLoading={this.state.loading} />
        )
    }
}

ConfirmMatchContainer.propTypes = {
    location: PropTypes.object.isRequired
}

export default ConfirmMatchContainer;

