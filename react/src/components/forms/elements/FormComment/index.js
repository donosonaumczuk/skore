import React from 'react';
import PropTypes from 'prop-types';

const FormComment = ({ id, textStyle, text }) => {
    return (
        <small id={id} className={textStyle}>
            {text}
        </small>
    );
}

FormComment.propTypes = {
    id: PropTypes.string.isRequired,
    textStyle: PropTypes.string.isRequired,
    text: PropTypes.string.isRequired
}

export default FormComment;