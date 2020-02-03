import PropTypes from 'prop-types';

const LocationPropType = PropTypes.shape(
    {
        country: PropTypes.string,
        state: PropTypes.string,
        city: PropTypes.string,
        street: PropTypes.string.isRequired,
    }
);

export default LocationPropType;