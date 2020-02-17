import PropTypes from 'prop-types';

const TeamPropType = PropTypes.shape(
    {
        players: PropTypes.array.isRequired
    }
);

export default TeamPropType;