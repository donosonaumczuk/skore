import React from 'react';
import PropTypes from 'prop-types';

const MINUTES_PER_HOUR = 60;

const getDuration = duration => {
    let hours, minutes;
    if (duration < MINUTES_PER_HOUR) {
        return `${duration} m`;
    } 
    else {
        hours = parseInt(duration / MINUTES_PER_HOUR);
        minutes = duration % MINUTES_PER_HOUR;
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
    durationInMinutes: PropTypes.number.isRequired
}

export default MatchDuration;