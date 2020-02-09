import React from 'react';
import PropTypes from 'prop-types';

const getSelectTag = (isDisabled, input, id, defaultText, options) => {
    if (isDisabled) {
        return (
            <select {...input} id={id} className="form-control" disabled>
                <option value="">{defaultText}</option>
                {options}
            </select>
        );
    }
    else {
        return (
            <select {...input} id={id} className="form-control">
                <option value="">{defaultText}</option>
                {options}
            </select>
        );
    }
}

const SelectInput = ({ input, label, id, meta, required, defaultText, options, isDisabled }) => {
    const select = getSelectTag(isDisabled, input, id, defaultText, options);
    return (
        <div className="form-group" >
            <label htmlFor={id}>
                {label}
                <span className="text-muted">{required ? " *" : ""}</span>
            </label>
            <div>
                {select}
                {meta.error &&
                meta.touched &&
                <span className="invalid-feedback d-block">
                    {meta.error}
                </span>}
            </div>
        </div> 
    );
}

SelectInput.propTypes = {
    input: PropTypes.object.isRequired,
    label: PropTypes.string,
    id: PropTypes.string,
    meta: PropTypes.object.isRequired,
    required: PropTypes.bool,
    defaultText: PropTypes.string.isRequired,
    options: PropTypes.array.isRequired
}

export default SelectInput;