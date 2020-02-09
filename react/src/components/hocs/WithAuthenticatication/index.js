import React from 'react';
import PropTypes from 'prop-types';
import LoginForm from '../../forms/LogInForm';

function WithAuthentication(Component) {
	return function Authentication({ needsAuthentication, url, updateUser, ...props }) {
		return needsAuthentication ? <LoginForm url={url} updateUser={updateUser}/>
										: <Component {...props} />;
	};
}

WithAuthentication.propTypes = {
	needsAuthentication: PropTypes.bool.isRequired,
	url: PropTypes.string.isRequired,
	updateUser: PropTypes.func.isRequired
}

export default WithAuthentication;