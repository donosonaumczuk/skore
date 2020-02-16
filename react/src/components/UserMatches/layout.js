import React from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import PropTypes from 'prop-types';
import Loader from '../Loader';
import UserMatchWithResult from './components/UserMatchWithResult';
import UserMatchWithResultPropType from '../../proptypes/UserMatchWithResultPropType';
import WithError from '../hocs/WithError';
import WithLoading from '../hocs/WithLoading';

const UserMatches = ({ matches, getUserMatches, hasMore, username, history }) => {
    return (
        <div className="container-fluid mt-4 rounded-border">
            <InfiniteScroll dataLength={matches.length} next={getUserMatches}
                            loader={<Loader />} hasMore={hasMore}>
            {
                matches.map( (match, i) => 
                    <UserMatchWithResult key={i} currentMatch={match} 
                                            username={username}
                                            history={history} />)
            }
            </InfiniteScroll>    
        </div>
    );
}

UserMatches.propTypes = {
    matches: PropTypes.arrayOf(UserMatchWithResultPropType).isRequired,
    getUserMatches: PropTypes.func.isRequired,
    hasMore: PropTypes.bool.isRequired,
    username: PropTypes.string.isRequired,
    history: PropTypes.object.isRequired
}

export default WithError(WithLoading(UserMatches));