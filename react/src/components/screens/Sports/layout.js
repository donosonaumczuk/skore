import React from 'react';
import PropTypes from 'prop-types';
import InfiniteScroll from 'react-infinite-scroll-component';
import Sport from './components/Sport';
import Loader from '../../Loader';
import SportPropType from '../../../proptypes/SportPropType';
import WithError from '../../hocs/WithError';
import WithLoading from '../../hocs/WithLoading';

const Sports = (props) => {
    const { sports, getSports, hasMore } = props;
    return (
        <div className="match-container container-fluid">
            {/* TODO sport Filter */}
            <InfiniteScroll dataLength={sports.length} style={{height: 'auto', overflow: 'visible'}} next={getSports}
                            hasMore={hasMore} loader={<Loader />}>
                {sports.map((sport) => 
                    <Sport key={sport.sportName} sport={sport} />)}
            </InfiniteScroll>
        </div>
    );
};

Sports.propTypes = {
    sports: PropTypes.arrayOf(SportPropType),
    getSports: PropTypes.func.isRequired,
    hasMore: PropTypes.bool.isRequired
};

export default WithError(WithLoading(Sports));