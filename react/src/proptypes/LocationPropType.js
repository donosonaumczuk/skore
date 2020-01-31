import PropTypes from 'prop-types';

//TODO make further validation of the type of array of players
const LocationPropType = PropTypes.shape(
    {
        country: PropTypes.string,
        state: PropTypes.string,
        city: PropTypes.string,
        street: PropTypes.string.isRequired,
    }
);

export default LocationPropType;