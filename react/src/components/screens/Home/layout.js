import React from 'react';
import PropTypes from 'prop-types';
import LeftPanel from '../../LeftPanel';
import WithAnonymous from '../../hocs/WithAnonymous';
import WithError from '../../hocs/WithError';

const Home = (props) => {
    const { currentTab, handleTabChange, filters, updateFilters,
            currentMatches, currentUser } = props;
     return (
        <div className="container-fluid">
            <div className="row">
                <div className="sidepanel col-md-4 col-lg-4 offset-xl-1 
                    col-xl-3 navbar-collapse" id="navbarSupportedContent">
                    <LeftPanel currentTab={currentTab} handleTabChange={handleTabChange}
                                currentUser={currentUser} filters={filters}
                                updateFilters={updateFilters} />
                </div>
                <div className="col-md-8 col-lg-8 col-xl-6">
                    {currentMatches}
                </div>
            </div>
        </div>
    );
} 

Home.propTypes = {
    currentTab: PropTypes.number.isRequired,
    handleTabChange: PropTypes.func.isRequired,
    filters: PropTypes.object.isRequired,
    updateFilters: PropTypes.func.isRequired,
    currentMatches: PropTypes.object.isRequired,
    currentUser: PropTypes.string,
}

export default WithError(WithAnonymous(Home));