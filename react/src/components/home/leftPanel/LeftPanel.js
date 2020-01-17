import React from 'react';
import i18next from 'i18next';
import PosterWithButton from '../../panel/PosterWithButton';
import FilterMenu from './FilterMenu';

const HomeLeftPanel = () => {
    return (
        <div className="container-fluid">
            <div className="row">
                <PosterWithButton posterStyle ="create-match" message={i18next.t('home.cantFindMatch')}
                    buttonText={i18next.t('home.createMatch')} buttonUrl="/createMatch" />
            </div>
            <FilterMenu />
        </div>
    );
}

export default HomeLeftPanel;