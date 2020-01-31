import React from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import HomeMatch from './HomeMatch';
import Loader from '../Loader';


const HomeMatches = (props) => { 
    const { matches, getMatches, hasMore} = props;
    return (
        <div className="match-container container-fluid">
            <InfiniteScroll dataLength={matches.length} next={getMatches} loader={<Loader />}
                            hasMore={hasMore}>
                {matches.map( (match, i) => <HomeMatch key={i} currentMatch={match} />)}
            </InfiniteScroll>
        </div>
    );
}

export default HomeMatches;