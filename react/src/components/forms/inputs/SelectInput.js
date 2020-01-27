import React from 'react';

const SelectInput = ({ input, label, id, meta, required, defaultText, options, }) => {
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
                    {/* TODO receive the array of options in option an mapper function in mapper */}
                    {/* {options.map( option => mapper(option))} */}
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

    
export default SelectInput;