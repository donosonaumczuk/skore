import React, { useEffect } from 'react';
import { useHistory } from 'react-router-dom';

function WithError(WrappedComponent) {
	return function Error({ error, ...props }) {
		const history = useHistory();
		useEffect(() => {
			if (error) {
				history.push(`/error/${error}`);
			}
		}, [error, history]);
 		return  <WrappedComponent {...props} />;
 	};
}

export default WithError;