import React, { Component } from 'react';
import Proptypes from 'prop-types';
import UserService from '../../services/UserService';
import UserMatches from './layout';
import Utils from '../utils/Utils';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 10;

class UserMatchesContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            matches: [],
            offset: INITIAL_OFFSET,
            limit: QUERY_QUANTITY,
            hasMore: true
        }
    }

    updateMatchesState = response => {
        if (response.status) {
            this.setState({
                status: response.status
            });
        }
        else {
            const hasMore = Utils.hasMorePages(response.links);
            const matches = response.matches;
            this.setState({
                matches: [...this.state.matches, ...matches],
                offset: this.state.offset + matches.length,
                hasMore: hasMore
            });
        }
    }

    getUserMatches = async (username) => {
        const { offset, limit } = this.state;
        const response = await UserService.getUserMatchesWithResults(username, offset, limit);
        if (this.mounted) {
            this.updateMatchesState(response)
        }
    }

    async componentDidMount() {
        this.mounted = true;
        this.getUserMatches(this.props.username);
        
    }

    render() {
        const matches = this.state.matches;
        const isLoading = matches.length === 0 && this.state.hasMore;
        const error = this.state.status;
        const { history } = this.props;
        return (
            <UserMatches matches={matches} getUserMatches={this.getUserMatches}
                            hasMore={this.state.hasMore} username={this.props.username}
                            isLoading={isLoading} error={error} history={history} />
        );
    }

    componentWillUnmount = () => {
        this.mounted = false;
    }
}

UserMatchesContainer.propTypes = {
    username: Proptypes.string.isRequired,
    history: Proptypes.object.isRequired
}

export default UserMatchesContainer;