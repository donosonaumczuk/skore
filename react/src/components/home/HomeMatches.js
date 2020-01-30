import React, { Component } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import HomeMatch from './HomeMatch';
import MatchService from './../../services/MatchService';
import Loader from '../Loader';
import Utils from '../utils/Utils';
import ErrorPage from '../ErrorPage';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 5;

class HomeMatches extends Component { 
    mounted = false;
    constructor(props) {
        super(props);
        const { tab } = this.props; 
        this.state = {
                matches: [],
                offset: INITIAL_OFFSET,
                total: QUERY_QUANTITY,
                hasMore: true,
                tab: tab
        }
    }

    async componentDidMount() {
        this.mounted = true;
        this.getMatches();
    }

    updateMatches = response => {
        if (response.status) {
            this.setState({ status: response.status });
        }
        else {
            const matches = response.matches;
            const hasMore = Utils.hasMorePages(response.links);
            this.setState({
                matches: [...this.state.matches, ...matches],
                offset: this.state.offset + matches.length,
                hasMore: hasMore
            });  
        }
    }

    static getDerivedStateFromProps(nextProps, previousState) {
        if (nextProps.tab !== previousState.tab) {
            const newState = { 
                ...previousState,
                tab: nextProps.tab,
                offset: INITIAL_OFFSET,
                hasMore: true,
                matches: []
            };
            return newState;
        }
        else {
            return null;
        }
    }

    componentDidUpdate(previousProps, previousState) {
        if (previousProps.tab !== this.props.tab) {
            if (this.mounted) {
                this.getMatches();
            }
        }
    }

    getMatches = async () => {
        let response;
        const { currentUser } = this.props;
        const { offset, total, tab } = this.state;
        if (tab === 0) {
            response = await MatchService.getMatches(offset, total);
        }
        else if (tab === 1) {
            response = await MatchService.getMatchesToJoin(currentUser, offset, total);
        }
        else if (tab === 2) {
            response = await MatchService.getMatchesJoinedBy(currentUser, offset, total);
        }
        else if (tab === 3) {
            response = await MatchService.getMatchesCreatedBy(currentUser, offset, total);
        }
        if (this.mounted) {
            this.updateMatches(response);
        }
    }

    render() {
        if (this.state.status) {
            return <ErrorPage status={this.state.status} />;
        }
        else if (this.state.matches.length === 0 && this.state.hasMore) {
            return <Loader />;
        }
        return (
            <div className="match-container container-fluid">
                <InfiniteScroll dataLength={this.state.matches.length} next={this.getMatches}

                                loader={<Loader />} hasMore={this.state.hasMore}>
                    { this.state.matches.map( (match, i) => <HomeMatch key={i} currentMatch={match} />)}
                </InfiniteScroll>
            </div>
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

export default HomeMatches;