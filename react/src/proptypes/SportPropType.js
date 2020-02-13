import PropTypes from 'prop-types';

const SportPropTypes = PropTypes.shape(
    {
        sportName: PropTypes.string.isRequired,
        playerQuantity: PropTypes.number.isRequired,
        displayName: PropTypes.string.isRequired
    }
);

export default SportPropTypes;