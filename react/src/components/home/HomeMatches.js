import React, { Component } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import HomeMatch from './HomeMatch';
import MatchService from './../../services/MatchService';
import Loader from '../Loader';


class HomeMatches extends Component { 
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
                matches: [],
                offset: 0,
                total: 5,
                hasMore: true
        }
    }
   
    async componentDidMount() {
        this.mounted = true;
        let matches = await MatchService.getMatches();
        if (this.mounted) {
            this.setState({
                matches: [...this.state.matches, ...matches]
            });
        }
    }

    //TODO remove this function when we have more matches on data base
    //this is just to generate more matches
    getNewMatches = (matches, total) => {
        let newMatches = [];
        let i;
        for (i = 0; i < total; i++) {
            newMatches[i] = matches[0];
        }
        return newMatches;
    } 

    getMoreMatches = async () => {
        let matches = await MatchService.getMatches();
        if (this.mounted) {
            const newMatches = this.getNewMatches(matches, this.state.total);
            this.setState({
                matches: [...this.state.matches, ...newMatches],
                offset: this.state.offset + this.state.total,
                hasMore: true
            });
        }
    }

    render() {
        if (this.state.matches.length === 0) {
            return <Loader />;
        }
        return (
            <div className="match-container container-fluid">
                <InfiniteScroll dataLength={this.state.matches.length} next={this.getMoreMatches}
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