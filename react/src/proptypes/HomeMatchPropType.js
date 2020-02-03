import PropTypes from 'prop-types';
import DatePropType from './DatePropType';
import TimePropType from './TimePropType';
import LocationPropType from './LocationPropType';

const HomeMatchPropType = PropTypes.shape(
    {
        title: PropTypes.string.isRequired,
        creator: PropTypes.string.isRequired,
        sportName: PropTypes.string.isRequired,
        competitive: PropTypes.bool.isRequired,
        durationInMinutes: PropTypes.number.isRequired,
        location: LocationPropType.isRequired,
        date: DatePropType.isRequired,
        time: TimePropType.isRequired,
        key: PropTypes.string.isRequired
    }
);

export default HomeMatchPropType;