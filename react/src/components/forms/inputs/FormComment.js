import React from 'react';

const FormComment = ({ id, textStyle, text }) => {
    return (
        <small id={id} className={textStyle}>
            {text}
        </small>
    );
}

export default FormComment;