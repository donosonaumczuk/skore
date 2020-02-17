import React from 'react';
import Spinner from 'react-spinkit'

const CustomSpinner = () => {
    return (
        //TODO center sometimes, maybe receive a parameter to center or with fix position
        <div className="row p-2 spinner-margin" id="loader">
            <div className="offset-5 col-2">
                <Spinner name="ball-spin-fade-loader" />
            </div>
        </div>
    );
}

export default CustomSpinner;