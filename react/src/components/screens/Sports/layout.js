import React from 'react';
import PropTypes from 'prop-types';
import InfiniteScroll from 'react-infinite-scroll-component';
import Sport from './components/Sport';
import Loader from '../../Loader';
import SportPropType from '../../../proptypes/SportPropType';
import WithError from '../../hocs/WithError';
import WithExecuting from '../../hocs/WithExecuting';
import WithLoading from '../../hocs/WithLoading';

const Sports = ({ sports, getSports, hasMore, currentUser, likes,
                    likeSport, dislikeSport }) => {
        return (
        <div className="match-container container-fluid container p-2">
            <InfiniteScroll dataLength={sports.length} style={{height: 'auto', overflow: 'visible'}} next={getSports}
                            hasMore={hasMore} loader={<Loader />}>
                {sports.map((sport) =>
                    <Sport key={sport.sportName} sport={sport}
                            isLogged={currentUser}
                            isLiked={likes[sport.sportName]}
                            likeSport={likeSport}
                            dislikeSport={dislikeSport} />)}
            </InfiniteScroll>
        </div>
    );
}

Sports.propTypes = {
    sports: PropTypes.arrayOf(SportPropType),
    getSports: PropTypes.func.isRequired,
    hasMore: PropTypes.bool.isRequired,
    currentUser: PropTypes.string,
    likes: PropTypes.object.isRequired,
    likeSport: PropTypes.func.isRequired,
    dislikeSport: PropTypes.func.isRequired
}

export default WithError(WithExecuting(WithLoading(Sports)));
