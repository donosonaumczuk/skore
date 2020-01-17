import React from 'react';
import i18next from 'i18next';
import Tab from './Tab';

const Tabs = () => {
    return (
        <div className="row mb-4 text-center">
            <label>{i18next.t('home.matches')}</label>
            <div className="btn-group ml-5 input-group btn-group-toggle" data-toggle="buttons" id="matches">
                <Tab text={i18next.t('home.toJoinTab')} isActive={true} />
                <Tab text={i18next.t('home.joinedTab')} isActive={false} />
                <Tab text={i18next.t('home.createdTab')} isActive={false} />
            </div>
        </div>
    );
}

export default Tabs;