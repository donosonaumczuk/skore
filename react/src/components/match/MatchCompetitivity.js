import React from 'react';
import Proptypes from 'prop-types';
import i18next from 'i18next';

const getClassName = isCompetitive => {
    if (isCompetitive) {
        return "competitive-icon mr-2 fas fa-medal";
    }
    else {
        return "friendly-icon mr-2 fas fa-handshake";
    }
}

const getText = isCompetitive => {
    if (isCompetitive) {
        return i18next.t('profile.match.competitive');
    }
    else {
        return i18next.t('profile.match.friendly');;
    }
}

const getTooltipTitle = isCompetitive => {
    if (isCompetitive) {
        return i18next.t('profile.match.competitiveTooltip');
    }
    else {
        return i18next.t('profile.match.friendlyTooltip');;
    }
}

const MatchCompetitivity = isCompetitive => {
    const className = getClassName(isCompetitive);
    const text = getText(isCompetitive);
    const tooltipTitle = getTooltipTitle(isCompetitive);
    //TODO add onClick to tooltip as in deploy because as it is with white background
    return (
      <div className="row">
            <div className="col">
                <p>
                    <span className={className}></span>
                    {text}
                    <span className="tooltip-icon ml-2 far fa-question-circle"
                    onClick={(e) => e.stopPropagation()} data-toggle="tooltip" 
                    data-placement="right" data-html="true" title={tooltipTitle} />
                </p>
            </div>
        </div>  
    );
}

MatchCompetitivity.propTypes = {
    isCompetitive: Proptypes.bool.isRequired
}

export default MatchCompetitivity;