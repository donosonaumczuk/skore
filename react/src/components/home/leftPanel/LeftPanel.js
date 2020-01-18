import React from 'react';
import i18next from 'i18next';
import PosterWithButton from '../../panel/PosterWithButton';
import FilterMenu from './FilterMenu';
import AuthService from '../../../services/AuthService';
import Tabs from './Tabs';

const getTabs = (user, currentTab, handleTabChange) => {
    if (user) {
        return <Tabs currentTab={currentTab} handleTabChange={handleTabChange} />
    }
    return <React.Fragment></React.Fragment>;
}

const HomeLeftPanel = ({ currentTab, handleTabChange }) => {
    const user = AuthService.getCurrentUser();
    const tabs = getTabs(user, currentTab, handleTabChange);
    return (
        <div className="container-fluid">
            <div className="row">
                <PosterWithButton posterStyle ="create-match" message={i18next.t('home.cantFindMatch')}
                    buttonText={i18next.t('home.createMatch')} buttonUrl="/createMatch" />
            </div>
            {tabs}
            <FilterMenu />
        </div>
    );
}

export default HomeLeftPanel;
