import React from 'react';
import Proptypes from 'prop-types';

const getDuration = duration => {
    let hours, minutes;
    if (duration < 60) {
        return `${duration} m`;
    } 
    else {
        hours = duration / 60;
        minutes = duration % 60;
        return `${hours} h ${minutes} m`;
    }
}

const MatchDuration = ({ durationInMinutes }) => {
    const duration = getDuration(durationInMinutes);
    return (
         <div className="row">
            <div className="col">
                <p>
                    <span className="name-label mr-2 fas fa-clock"></span>
                    {duration}
                </p>
            </div>
        </div>
    );
}

MatchDuration.propTypes = {
    durationInMinutes: Proptypes.number.isRequired
}

export default MatchDuration;