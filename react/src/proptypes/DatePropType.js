import PropTypes from 'prop-types';

const DatePropType = PropTypes.shape(
    {
        monthValue: PropTypes.number.isRequired,
        dayOfMonth: PropTypes.number.isRequired,
        year: PropTypes.number.isRequired
    }
)

export default DatePropType;