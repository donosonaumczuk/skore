import React from 'react';
import { Redirect } from 'react-router-dom';

//TODO improve without redirect
function WithError(WrappedComponent) {
	return function Error({ error, ...props }) {
		if (error) {
 			return <Redirect to={`/error/${error}`} />
 		}
 		else {
 			return  <WrappedComponent {...props} />;
 		}
 	};
}

export default WithError;