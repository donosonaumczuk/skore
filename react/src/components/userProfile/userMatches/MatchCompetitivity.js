import React from 'react';
import Proptypes from 'prop-types';

const getClassName = isCompetitive => {
    if (isCompetitive) {
        return "competitive-icon mr-2 fas fa-medal";
    }
    else {
        return "friendly-icon mr-2 fas fa-handshake";
    }
}

const getText = isCompetitive => {
    //TODO update with i18n
    if (isCompetitive) {
        return "Competitive";
    }
    else {
        return "Friendly";
    }
}

const MatchCompetitivity = isCompetitive => {
    const className = getClassName(isCompetitive);
    const text = getText(isCompetitive);
    //TODO add onClick to tooltip as in deploy because as it is nothing appears
    return (
      <div className="row">
            <div className="col">
                <p>
                    <span className={className}></span>
                    {text}
                    <span className="tooltip-icon ml-2 far fa-question-circle"
                    onClick={(e) => e.stopPropagation()} data-toggle="tooltip" 
                    data-placement="right" data-html="true" title="" />
                </p>
            </div>
        </div>  
    );
}

MatchCompetitivity.propTypes = {
    isCompetitive: Proptypes.bool.isRequired
}

export default MatchCompetitivity;