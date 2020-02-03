import React, { Component } from 'react';
import PropTypes from 'prop-types';
import MatchPage from './layout';
import MatchService from '../../../services/MatchService';
import ErrorPage from '../ErrorPage';
import Loader from '../../Loader';

class MatchPageContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const { matchKey } = this.props.match.params;
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
        if (this.state.status) {
            return <ErrorPage status={this.state.status} /> //TODO improve with HOC
        }
        else if (!this.state.match) {
            return <Loader /> //TODO improve with HOC
        }
        return (
            <MatchPage currentMatch={this.state.match} />
        );
    }

    componentWillUnmount = () => {
        this.mounted = false;
    } 
}

MatchPageContainer.propTypes = {
    match: PropTypes.object.isRequired
}

export default MatchPageContainer;