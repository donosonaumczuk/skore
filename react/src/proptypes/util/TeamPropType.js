import PropTypes from 'prop-types';

//TODO make further validation of the type of array of players
const TeamPropType = PropTypes.shape(
    {
        players: PropTypes.array.isRequired
    }
);

export default TeamPropType;