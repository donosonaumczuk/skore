import React from 'react';
import PropTypes from 'prop-types';

const RadioInput = ({ input, meta, labelStyle, id, labelText,
                        radioValue, showError, ...rest }) => {
    const checked = input.value === radioValue;
    return (
        <React.Fragment>
            <label className={labelStyle} id={id}>
                <input {...input} type="radio" {...rest} checked={checked} />
                <i className={checked ? "fas fa-check" : "d-none fas fa-check"}></i>
                {labelText}
            </label>
            {meta.error && meta.touched && showError && <span className="invalid-feedback d-block pl-2">
                    {meta.error}
            </span>}
        </React.Fragment>
    );
}

RadioInput.propTypes = {
    input: PropTypes.object.isRequired,
    meta: PropTypes.object.isRequired,
    labelStyle: PropTypes.string.isRequired,
    id: PropTypes.string.isRequired,
    labelText: PropTypes.string.isRequired,
    radioValue: PropTypes.string.isRequired,
}

export default RadioInput;