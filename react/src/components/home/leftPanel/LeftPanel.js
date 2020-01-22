import React from 'react';
import i18next from 'i18next';
import PosterWithButton from '../../panel/PosterWithButton';
import FilterMenu from './FilterMenu';
import Tabs from './Tabs';

const getTabs = (user, currentTab, handleTabChange) => {
    if (user) {
        return <Tabs currentTab={currentTab} handleTabChange={handleTabChange} />
    }
    return <React.Fragment></React.Fragment>;
}

const HomeLeftPanel = ({ currentTab, handleTabChange, currentUser }) => {
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
            {tabs}
            <FilterMenu />
        </div>
    );
}

export default HomeLeftPanel;
