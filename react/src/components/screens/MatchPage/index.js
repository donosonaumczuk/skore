import React, { Component } from 'react';
import PropTypes from 'prop-types';
import MatchPage from './layout';
import MatchService from '../../../services/MatchService';

class MatchPageContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        let matchKey;
        if (this.props && this.props.match && this.props.match.params) {
            matchKey = this.props.match.params;
        }
        else {
            matchKey = this.props.matchKey;
        }
        this.state = {
            matchKey: matchKey,
            match: null
        };
    }

    componentDidMount = async () => {
        this.mounted = true;
        const response = await MatchService.getMatchByKey(this.state.matchKey);
        if (this.mounted) {
            if (response.status) {
                this.setState({ status: response.status });
            }
            else if (this.mounted) {
                this.setState({ match: response });
            }
        } 
    }

    render() {
        const { message } = this.props; 
        return (
            <MatchPage currentMatch={this.state.match} error={this.state.status}
                        isLoading={!this.state.match} message={message} />
        );
    }

    componentWillUnmount = () => {
        this.mounted = false;
    } 
}

MatchPageContainer.propTypes = {
    match: PropTypes.object,
    message: PropTypes.string,
    matchKey: PropTypes.string,
}

export default MatchPageContainer;