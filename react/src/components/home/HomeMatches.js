import React from 'react';
import HomeMatch from './HomeMatch';
import Proptypes from 'prop-types';

const HomeMatches = ({ matches }) => 
        <div className="match-container container-fluid">
            { matches.map( (match, i) => <HomeMatch key={i} currentMatch={match} />)}
        </div>

HomeMatches.propTypes = {
    matches: Proptypes.array.isRequired
}

export default HomeMatches;