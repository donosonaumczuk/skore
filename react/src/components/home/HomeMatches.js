import React, { Component } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import HomeMatch from './HomeMatch';
import MatchService from './../../services/MatchService';
import Loader from '../Loader';

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
   
    hasMoreMatches = links => {
        let hasMore = false;
        links.forEach(link => {
            if (link.rel === "next") {
                hasMore = true;
            }
        })
        return hasMore;
    }

    async componentDidMount() {
        this.mounted = true;
        this.getMatches();
    }

    getMatches = async () => {
        let response = await MatchService.getMatches(this.state.offset, this.state.total);
        if (this.mounted) {
            const matches = response.matches;
            const hasMore = this.hasMoreMatches(response.links);
            this.setState({
                matches: [...this.state.matches, ...matches],
                offset: this.state.offset + matches.length,
                hasMore: hasMore
            });  
        }
    }

    render() {
        if (this.state.matches.length === 0) {
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