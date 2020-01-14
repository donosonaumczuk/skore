import React from 'react';

const EditUserButton = ({ url, iStyle, text }) => {
    return (
        <a className="btn btn-outline-secondary mx-1" href={url} role="button">
            <i className={iStyle}></i>
            {text}
        </a>
    );
}

export default EditUserButton;