import React, { Component } from 'react';
import queryString from 'query-string';
import PropTypes from 'prop-types';
import i18next from 'i18next';
import MatchService from '../../../services/MatchService';
import CancelMatch from './layout';

class CancelMatchContainer extends Component {
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
        const response = await MatchService.cancelAssistance(matchKey, id, code);
        if (response.status && this.mounted) {
            this.setState({ status: response.status, loading: false });
        }
        else {
            this.setState({ loading: false });
        }
    }

    render() {
        return (
            <CancelMatch matchKey={this.state.matchKey} error={this.state.status}
                            message={i18next.t('cancelAsistance.canceled')}
                            isLoading={this.state.loading} />
        )
    }

    componentWillUnmount = () => {
        this.mounted = false;
    }
}

CancelMatchContainer.propTypes = {
    location: PropTypes.object.isRequired
}

export default CancelMatchContainer;

