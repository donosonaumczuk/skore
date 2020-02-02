import React from 'react';
import Proptypes from 'prop-types';

const SubmitButton = ({ label, divStyle, buttonStyle, submitting }) => {
    return (
        <div className={divStyle}>
        <button type="submit" className={buttonStyle} disabled={submitting}>
          {label}
        </button>
      </div>
    )
}

SubmitButton.propTypes = {
    label: Proptypes.string.isRequired,
    divStyle: Proptypes.string.isRequired,
    buttonStyle: Proptypes.string.isRequired,
    submitting: Proptypes.bool
}

export default SubmitButton;