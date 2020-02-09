import React from 'react';
import LoginForm from '../../forms/LogInForm';

function WithAuthentication(Component) {
	return function Authentication({ needsAuthentication, url, ...props }) {
		return needsAuthentication ? <LoginForm url={url} /> : <Component {...props} />;
	};
}

export default WithAuthentication;