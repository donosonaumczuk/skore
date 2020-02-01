import React, { Component } from 'react';
import Proptypes from 'prop-types';
import InfiniteScroll from 'react-infinite-scroll-component';
import UserService from '../../../services/UserService';
import Loader from '../../Loader';
import UserMatchWithResult from './UserMatchWithResult';
import ErrorPage from '../../ErrorPage';
import Utils from '../../utils/Utils';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 1;

class UserMatches extends Component {
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
        //TODO depending on users choice change between mach with result and only finished, and created not played
        const matches = this.state.matches;
        if (matches.length > 0 || !this.state.hasMore) {
            return (
                <div className="container-fluid mt-4 rounded-border">
                    <InfiniteScroll dataLength={this.state.matches.length} next={this.getUserMatches}
                                    loader={<Loader />} hasMore={this.state.hasMore}>
                    {
                        matches.map( (match, i) => <UserMatchWithResult key={i} currentMatch={match} username={this.props.username} />)
                    }
                    </InfiniteScroll>    
                </div>
            );
        }
        else if (this.state.status) {
            return (
                <ErrorPage status={this.state.status} />
            )
        }
        else {
            return (
                <Loader />
            );
        }
    }

    componentWillUnmount = () => {
        this.mounted = false;
        //TODO cancel fetch if still fetching matches
    }
}

UserMatches.propTypes = {
    username: Proptypes.string.isRequired
}

export default UserMatches;