import React from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import PropTypes from 'prop-types';
import SportPropType from '../../../proptypes/SportPropType';
import Loader from '../../Loader';
import AdminSport from './components/AdminSport';
import AdminSportTitle from './components/AdminSportTitle';

const Admin = ({ sports, getSports, hasMore }) => {
    return (
        <div className="match-container container-fluid">
            <AdminSportTitle />
            <InfiniteScroll dataLength={sports.length} style={{height: 'auto', overflow: 'visible'}} next={getSports}
                            hasMore={hasMore} loader={<Loader />}>
                {sports.map((sport) => 
                    <AdminSport key={sport.sportName} sport={sport} />)}
            </InfiniteScroll>
        </div>
    );
};

Admin.propTypes = {
    sports: PropTypes.arrayOf(SportPropType),
    getSports: PropTypes.func.isRequired,
    hasMore: PropTypes.bool.isRequired
};

export default Admin;