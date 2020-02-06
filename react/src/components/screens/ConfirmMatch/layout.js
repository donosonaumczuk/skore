import React from 'react';
import PropTypes from 'prop-types';
import WithLoading from '../../hocs/WithLoading';
import WithError from '../../hocs/WithError';

const CancelMatch = ({ message }) => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid profile-container bg-white 
                                rounded-border alert alert-info alert-dismissible
                                fade show mt-1">
                    <h1>{message}</h1>
                </div>
            </div>
        </div>
    );
}

CancelMatch.propTypes = {
    message: PropTypes.string.isRequired
}


export default WithError(WithLoading(CancelMatch));