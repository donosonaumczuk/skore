import React from 'react';
import Proptypes from 'prop-types';

const SubmitButton = ({ label, submitting }) => {
    return (
        <div>
        <button type="submit" disabled={submitting}>
          {label}
        </button>
      </div>
    )
}

SubmitButton.propTypes = {
    label: Proptypes.string.isRequired,
    submitting: Proptypes.bool
}

export default SubmitButton;