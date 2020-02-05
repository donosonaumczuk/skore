import React from 'react';
import ErrorPage from '../../screens/ErrorPage';

function WithError(Component) {
  return function Error({ error, ...props }) {
    return error !== null ? <ErrorPage status={error} /> : <Component {...props} />;
  };
}

export default WithError;