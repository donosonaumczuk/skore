import React from 'react';
import Proptypes from 'prop-types';


const getDateString = date => {
    //TODO add translation in spanish
    return `${date.monthNumber}/${date.dayOfMonth}/${date.year}`;
}

const getTimeString = time => `${time.hour}:${time.minute}`;

const MatchDate = ({ date, time }) => {
   const dateString = getDateString(date);
   const timeString = getTimeString(time);

   return (
        <div className="row">
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
    date: Proptypes.object.isRequired,
    time: Proptypes.object.isRequired
}

export default MatchDate;