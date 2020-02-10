import React from 'react';
import ErrorPage from '../../screens/ErrorPage';
import { SC_FORBIDDEN } from '../../../services/constants/StatusCodesConstants';

function WithPermission(Component) {
	return function Permission({ needsPermission, ...props }) {
		return needsPermission ? <ErrorPage status={SC_FORBIDDEN} /> : <Component {...props} />;
	};
}

export default WithPermission;