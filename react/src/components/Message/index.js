import React from 'react';
import PropTypes from 'prop-types';

const Message = ({ message }) => {
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

Message.propTypes = {
    message: PropTypes.string.isRequired
}

export default Message;