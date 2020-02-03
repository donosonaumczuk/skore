import PropTypes from 'prop-types';

const UserProfilePropType = PropTypes.shape(
    {
        username: PropTypes.string.isRequired,
        firstName: PropTypes.string.isRequired,
        lastName: PropTypes.string.isRequired,
        winRate: PropTypes.number.isRequired,
        age: PropTypes.number.isRequired,
        location: PropTypes.string,
    }
)

export default UserProfilePropType;