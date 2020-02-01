import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';

const EditUserButton = ({ url, iStyle, text }) => {
    return (
        <Link className="btn btn-outline-secondary mx-1" to={url} role="button">
            <i className={iStyle}></i>
            {text}
        </Link>
    );
}

EditUserButton.propTypes = {
    url: PropTypes.string.isRequired,
    iStyle: PropTypes.string.isRequired,
    text: PropTypes.string.isRequired
}

export default EditUserButton;