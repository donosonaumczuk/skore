import React from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import PropTypes from 'prop-types';
import HomeMatch from '../HomeMatch';
import Loader from '../../../../Loader';

const HomeMatches = (props) => { 
    const { matches, getMatches, hasMore, handleMatchClick, joinMatch,
            cancelMatch, deleteMatch } = props;
    return (
        <div className="match-container container-fluid">
            <InfiniteScroll dataLength={matches.length} style={{height: 'auto', overflow: 'visible'}} next={getMatches}
                            loader={<Loader />} hasMore={hasMore}>
                {matches.map( (match, i) => <HomeMatch key={i} currentMatch={match}
                                                        handleMatchClick={handleMatchClick}
                                                        joinMatch={joinMatch}
                                                        cancelMatch={cancelMatch}
                                                        deleteMatch={deleteMatch} />)}
            </InfiniteScroll>
        </div>
    );
}

HomeMatches.propTypes = {
    matches: PropTypes.array.isRequired,//TODO validate array of HomeMatchProptype
    getMatches: PropTypes.func.isRequired,
    hasMore: PropTypes.bool.isRequired,
    handleMatchClick: PropTypes.func.isRequired,
    joinMatch: PropTypes.func.isRequired,
    cancelMatch: PropTypes.func.isRequired,
    deleteMatch: PropTypes.func.isRequired
}

export default HomeMatches;