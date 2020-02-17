import React from 'react';

const createRenderer = render => ({ input, meta, label, inputType, id, required,
                                    isDisabled, ...rest }) => {
return (
    <div className="form-group" >
        <label htmlFor={id}>
            {label}
            <span className="text-muted">{required ? " *" : ""}</span>
        </label>
        <div>
            {render(input, label, inputType, id, isDisabled, meta, rest)}
            {meta.error &&
            meta.touched &&
            <span className="invalid-feedback d-block">
                {meta.error}
            </span>}
        </div>
    </div>
);}
                                    
export default createRenderer;
