import React from 'react';
import PropTypes from 'prop-types';

const SelectInput = ({ input, label, id, meta, required, defaultText, options }) => {
    return (
        <div className="form-group" >
            <label htmlFor={id}>
                {label}
                <span className="text-muted">{required ? " *" : ""}</span>
            </label>
            <div>
                <select {...input} id={id} className="form-control">
                    <option value="">{defaultText}</option>
                    {options}
                </select>
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