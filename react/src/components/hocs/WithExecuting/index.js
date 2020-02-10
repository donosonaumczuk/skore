import React from 'react';
import Spinner from '../../Spinner';

function WithExecuting(Component) {
    return function Executing({ isExecuting, ...props }) {
        return isExecuting ? <Spinner /> : <Component {...props} />;
    };
}

export default WithExecuting;