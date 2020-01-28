import PropTypes from 'prop-types';
import DatePropType from './DatePropType';
import TimePropType from './TimePropType';

const HomeMatchPropType = PropTypes.shape(
    {
        title: PropTypes.string.isRequired,
        creator: PropTypes.string.isRequired,
        sportName: PropTypes.string.isRequired,
        competitive: PropTypes.bool.isRequired,
        durationInMinutes: PropTypes.number.isRequired,
        location: PropTypes.string.isRequired,
        date: DatePropType.isRequired,
        time: TimePropType.isRequired
    }
);

export default HomeMatchPropType;