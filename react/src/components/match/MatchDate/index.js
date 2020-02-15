import React from 'react';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import DatePropType from '../../../proptypes/DatePropType';
import TimePropType from '../../../proptypes/TimePropType';
import { getStringWithTwoDigits } from '../../../services/Util';

const getDateString = date => {
    const monthNumberString = getStringWithTwoDigits(date.monthNumber);
    const dayOfMonthString = getStringWithTwoDigits(date.dayOfMonth);
    if (i18next.language === 'es') {
        return `${dayOfMonthString}/${monthNumberString}/${date.year}`;
    }
    else {
        return `${monthNumberString}/${dayOfMonthString}/${date.year}`;
    }
}

const getTimeString = time => {
    const hourString = getStringWithTwoDigits(time.hour);
    const minuteString = getStringWithTwoDigits(time.minute);
    return `${hourString}:${minuteString}`;
}

const MatchDate = ({ date, time, isMatchPage }) => {
    const dateString = getDateString(date);
    const timeString = getTimeString(time);
    const containerClass = isMatchPage ? "row text-center mt-5" : "row";
    return (
        <div className={containerClass}>
            <div className="col">
                <p>
                    <span className="calendar-icon mr-2 fas fa-calendar-alt"></span>
                    {dateString + " " + timeString}
                </p>
            </div>
        </div>
    );
}

MatchDate.propTypes = {
    date: DatePropType.isRequired,
    time: TimePropType.isRequired,
    isMatchPage: PropTypes.bool
}

export default MatchDate;