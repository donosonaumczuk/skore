import PropTypes from 'prop-types';

const AccountPropType = PropTypes.shape(
    {
        username: PropTypes.string.isRequired
    }
);

export default AccountPropType;