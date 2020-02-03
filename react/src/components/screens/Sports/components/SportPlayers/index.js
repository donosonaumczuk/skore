import React from 'react';
import Proptypes from 'prop-types';
import i18next from 'i18next';

const SportPlayers = ({ playersQuantity }) => {
    return (
        <div className="row">
            <div className="col">
                <i className="name-label fas fa-users mr-2"></i>
                <label>{i18next.t('sports.playersPerTeam')}</label>
                 <span>{` ${playersQuantity}`}</span>
            </div>
        </div>
    );
}

SportPlayers.propTypes = {
    playersQuantity: Proptypes.number.isRequired
}

export default SportPlayers;