import React from 'react';

const FormComment = ({ id, textStyle, text }) => {
    return (
        <small id={id} className={textStyle}>
            {text}hola
        </small>
    );
}

export default FormComment;