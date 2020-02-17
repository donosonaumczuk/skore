import PropTypes from 'prop-types';
import DatePropType from './DatePropType';
import TimePropType from './TimePropType';
import TeamPropType from './util/TeamPropType';
import LocationPropType from './LocationPropType';

const MatchPagePropType = PropTypes.shape(
    {
        title: PropTypes.string.isRequired,
        creator: PropTypes.string.isRequired,
        sportName: PropTypes.string.isRequired,
        results: PropTypes.string,
        competitive: PropTypes.bool.isRequired,
        minutesOfDuration: PropTypes.number.isRequired,
        location: LocationPropType.isRequired,
        date: DatePropType.isRequired,
        time: TimePropType.isRequired,
        team1: TeamPropType.isRequired,
        team2: TeamPropType.isRequired
    }
);

export default MatchPagePropType;