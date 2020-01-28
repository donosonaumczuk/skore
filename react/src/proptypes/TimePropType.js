import PropTypes from 'prop-types';

const TimePropType = PropTypes.shape(
    {
        hour: PropTypes.number.isRequired,
        minute: PropTypes.number.isRequired
    }
)

export default TimePropType;