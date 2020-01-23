import React, { Component } from 'react';
import Proptypes from 'prop-types';
import InfiniteScroll from 'react-infinite-scroll-component';
import UserService from '../../../services/UserService';
import Loader from '../../Loader';
import UserMatch from './UserMatch';
import ErrorPage from '../../ErrorPage';

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

    hasMoreMatches = links => {
        let hasMore = false;
        links.forEach(link => {
            if (link.rel === "next") {
                hasMore = true;
            }
        });
        return hasMore;
    }

    updateMatchesState = response => {
        if (response.status) {
            this.setState({
                status: response.status
            });
        }
        else {
            const hasMore = this.hasMoreMatches(response.links);
            const matches = response.matches;
            console.log("hasMore: ", hasMore);
            this.setState({
                matches: [...this.state.matches, ...matches],
                offset: this.state.offset + matches.length,
                hasMore: hasMore
            });
        }
    }

    getUserMatches = async (username) => {
        const { offset, limit } = this.state;
        const response = await UserService.getUserMatches(username, offset, limit);
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
        if (matches.length > 0 || !this.state.hasMore) {
            return (
                <div className="container-fluid mt-4 rounded-border">
                    <InfiniteScroll dataLength={this.state.matches.length} next={this.getUserMatches}
                                    loader={<Loader />} hasMore={this.state.hasMore}>
                    {
                        matches.map( (match, i) => <UserMatch key={i} currentMatch={match} username={this.props.username} />)
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