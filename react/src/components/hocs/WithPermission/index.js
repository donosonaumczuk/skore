import React from 'react';
import { Redirect } from 'react-router-dom';
import { SC_FORBIDDEN } from '../../../services/constants/StatusCodesConstants';

function WithPermission(Component) {
	return function Permission({ needsPermission, ...props }) {
		return needsPermission ? <Redirect to={`/error/${SC_FORBIDDEN}`} /> :
									<Component {...props} />;
	};
}

export default WithPermission;