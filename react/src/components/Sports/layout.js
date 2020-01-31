import React from 'react';
import PropTypes from 'prop-types';
import InfiniteScroll from 'react-infinite-scroll-component';
import Sport from './components/Sport';
import Loader from '../Loader';

const Sports = (props) => {
    const { sports, getSports, hasMore } = props;
    return (
        <div className="match-container container-fluid">
            {/* TODO sport Filter */}
            <InfiniteScroll dataLength={sports.length} next={getSports}
                            hasMore={hasMore} loader={<Loader />}>
                {sports.map((sport) => 
                    <Sport key={sport.sportName} sport={sport} />)}
            </InfiniteScroll>
        </div>
    );
}

Sports.propTypes = {
    sports: PropTypes.array.isRequired,//TODO make sport array custom proptype
    getSports: PropTypes.func.isRequired,
    hasMore: PropTypes.bool.isRequired
}

export default Sports;