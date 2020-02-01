import React from 'react';
import { Link } from 'react-router-dom';

const EditUserButton = ({ url, iStyle, text }) => {
    return (
        <Link className="btn btn-outline-secondary mx-1" to={url} role="button">
            <i className={iStyle}></i>
            {text}
        </Link>
    );
}

export default EditUserButton;