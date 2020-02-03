import React from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import PropTypes from 'prop-types';
import Loader from '../Loader';
import UserMatchWithResult from './components/UserMatchWithResult';
import UserMatchWithResultPropType from '../../proptypes/UserMatchWithResultPropType';

const UserMatches = ({ matches, getUserMatches, hasMore, username }) => {
    return (
        <div className="container-fluid mt-4 rounded-border">
            <InfiniteScroll dataLength={matches.length} next={getUserMatches}
                            loader={<Loader />} hasMore={hasMore}>
            {
                matches.map( (match, i) => 
                    <UserMatchWithResult key={i} currentMatch={match} username={username} />)
            }
            </InfiniteScroll>    
        </div>
    );
}

UserMatches.propTypes = {
    matches: PropTypes.arrayOf(UserMatchWithResultPropType).isRequired,
    getUserMatches: PropTypes.func.isRequired,
    hasMore: PropTypes.bool.isRequired,
    username: PropTypes.string.isRequired
}

export default UserMatches;