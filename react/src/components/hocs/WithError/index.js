import React from 'react';
import { Redirect } from 'react-router-dom';

function WithError(Component) {
	return function Error({ error, ...props }) {
		return error ? <Redirect to={`/error/${error}`} /> : <Component {...props} />;
	};
}

export default WithError;