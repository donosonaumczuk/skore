import React from 'react';
import Spinner from 'react-spinkit'

const CustomSpinner = () => {
    return (
        <div className="row p-2 spinner-margin" id="loader">
            <div className="offset-5 col-2">
                <Spinner name="ball-spin-fade-loader" />
            </div>
        </div>
    );
}

export default CustomSpinner;