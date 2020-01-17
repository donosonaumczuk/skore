import React from 'react';
import i18next from 'i18next';
import PosterWithButton from '../../panel/PosterWithButton';
import FilterMenu from './FilterMenu';
import AuthService from '../../../services/AuthService';
import Tabs from './Tabs';

const HomeLeftPanel = () => {
    const user = AuthService.getCurrentUser();
    const tabs = user ? <Tabs></Tabs> : <React.Fragment></React.Fragment>;
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