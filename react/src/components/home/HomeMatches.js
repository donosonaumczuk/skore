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
        this.state = {
                matches: [],
                offset: INITIAL_OFFSET,
                total: QUERY_QUANTITY,
                hasMore: true
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

    getMatches = async () => {
        let response = await MatchService.getMatches(this.state.offset, this.state.total);
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