import React from 'react';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import Tab from '../Tab';

const getTabState = currentTab => {
    let tabOne = false, tabTwo=false, tabThree=false;
    if (currentTab === 1) {
        tabOne = true;
    }
    else if(currentTab === 2) {
        tabTwo = true;
    }
    else if(currentTab === 3) {
        tabThree = true;//This is condition added to prevent invalid numbers to activate tab three
    }
    return {
        tabOne: tabOne,
        tabTwo: tabTwo,
        tabThree: tabThree
    };
}

const Tabs = ({ currentTab, handleTabChange }) => {
    const tabState = getTabState(currentTab);
    return (
        <div className="row mb-4 text-center">
            <div className="btn-group ml-5 input-group btn-group-toggle" data-toggle="buttons" id="matches">
                <Tab text={i18next.t('home.toJoinTab')} isActive={tabState.tabOne} 
                                            number={1} handleChange={handleTabChange} />
                <Tab text={i18next.t('home.joinedTab')} isActive={tabState.tabTwo}
                                             number={2} handleChange={handleTabChange} />
                <Tab text={i18next.t('home.createdTab')} isActive={tabState.tabThree}
                                             number={3} handleChange={handleTabChange} />
            </div>
        </div>
    );
}

Tabs.propTypes = {
    currentTab: PropTypes.number.isRequired,
    handleTabChange: PropTypes.func.isRequired
}

export default Tabs;