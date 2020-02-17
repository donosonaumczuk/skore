import PropTypes from 'prop-types';
import DatePropType from './DatePropType';
import TimePropType from './TimePropType';
import LocationPropType from './LocationPropType';

const UserMatchWithResultPropType = PropTypes.shape(
    {
        title: PropTypes.string.isRequired,
        creator: PropTypes.string.isRequired,
        sportName: PropTypes.string.isRequired,
        results: PropTypes.string.isRequired,
        competitive: PropTypes.bool.isRequired,
        minutesOfDuration: PropTypes.number.isRequired,
        location: LocationPropType.isRequired,
        date: DatePropType.isRequired,
        time: TimePropType.isRequired,
        inTeam1: PropTypes.bool.isRequired,
        inTeam2: PropTypes.bool.isRequired,
        team1: PropTypes.array,
        team2: PropTypes.array
    }
);

export default UserMatchWithResultPropType;