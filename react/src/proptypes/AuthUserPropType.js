import PropTypes from 'prop-types';

const AuthUserPropType = PropTypes.shape(
    {
        username: PropTypes.string,
        isAdmin: PropTypes.bool.isRequired
    }
);

export default AuthUserPropType;