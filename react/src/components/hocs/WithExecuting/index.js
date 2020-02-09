import React from 'react';
import Spinner from 'react-spinkit'

const Executing = () => {
    return (
        //TODO center sometimes, maybe receive a parameter to center or with fix position
        <div className="row p-2 mt-2" id="loader">
            <div className="offset-5 col-2">
                <Spinner name="ball-spin-fade-loader" />
            </div>
        </div>
    );
}

function WithExecuting(Component) {
    return function Loading({ isExecuting, ...props }) {
        return isExecuting ? <Executing /> : <Component {...props} />;
    };
}

export default WithExecuting;