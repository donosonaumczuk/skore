import React from 'react';
import PropTypes from 'prop-types';
import TimePropType from '../../../../../proptypes/TimePropType';
import { getStringWithTwoDigits } from '../../../../../services/Util';


const MINUTES_PER_HOUR = 60;
const HOURS_PER_DAY = 24;

const getDuration = duration => {
    let hours, minutes;
    if (duration < MINUTES_PER_HOUR) {
        return `${duration} m`;
    } 
    else {
        hours = duration / MINUTES_PER_HOUR;
        minutes = duration % MINUTES_PER_HOUR;
        return `${hours} h ${minutes} m`;
    }
}

const getEndTimeString = (startTime, durationInMinutes) => {
    const minutes = startTime.minute;
    const hour = startTime.hour;
    const endingMinutes = (minutes + durationInMinutes) % MINUTES_PER_HOUR;
    const extraHours = parseInt((minutes + durationInMinutes) / MINUTES_PER_HOUR);
    const endingHour = (hour + extraHours) % HOURS_PER_DAY;
    return `${getStringWithTwoDigits(endingHour)}:${getStringWithTwoDigits(endingMinutes)}`;
}

const MatchPageDuration = ({ durationInMinutes, startTime }) => {
    const duration = getDuration(durationInMinutes);
    const stringEndTime = getEndTimeString(startTime, durationInMinutes);
    return (
         <div className="row text-center">
            <div className="col" onClick={(e) => e.stopPropagation()} data-toggle="tooltip"
                    data-placement="right" data-html="true" title={stringEndTime}>
                <p>
                    <span className="calendar-icon mr-2 fas fa-clock"></span>
                    {duration}
                </p>
            </div>
        </div>
    );
}

MatchPageDuration.propTypes = {
    durationInMinutes: PropTypes.number.isRequired,
    startTime: TimePropType.isRequired
}

export default MatchPageDuration;