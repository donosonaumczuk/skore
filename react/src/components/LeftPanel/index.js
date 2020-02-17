import React from 'react';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import PosterWithButton from '../panel/PosterWithButton';
import FilterMenu from './components/FilterMenu';
import Tabs from './components/Tabs';

const getTabs = (user, currentTab, handleTabChange) => {
    if (user) {
        return <Tabs currentTab={currentTab} handleTabChange={handleTabChange} />
    }
    return <React.Fragment/>;
}

const HomeLeftPanel = ({ currentTab, handleTabChange, currentUser, filters, updateFilters }) => {
    const user = currentUser;
    const tabs = getTabs(user, currentTab, handleTabChange);
    const message = user ? i18next.t('home.cantFindMatch') : i18next.t('home.wantToCreateMatch');
    return (
        <div className="container-fluid">
            <div className="row">
                <PosterWithButton posterStyle ="create-match" message={message}
                                    currentUser={user} buttonText={i18next.t('home.createMatch')}
                                    buttonUrl="/createMatch" />
            </div>
            <FilterMenu initialValues={filters} updateFilters={updateFilters} tabs={tabs}
                        currentUser={user} />
        </div>
    );
}

HomeLeftPanel.propTypes = {
    currentTab: PropTypes.number.isRequired,
    handleTabChange: PropTypes.func.isRequired,
    currentUser: PropTypes.string,
    updateFilters: PropTypes.func.isRequired,
    filters: PropTypes.object.isRequired
}

export default HomeLeftPanel;
