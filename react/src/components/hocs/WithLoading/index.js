import React from 'react';
import Loader from '../../Loader';

function WithLoading(Component) {
    return function Loading({ isLoading, ...props }) {
        return isLoading ? <Loader /> : <Component {...props} />;
    };
}

export default WithLoading;