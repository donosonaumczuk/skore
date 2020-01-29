import React from 'react';
import DatePicker from 'react-datepicker';

const RenderMatchDatePicker = ({ input, label, id, required, smallText, meta }) => (
    <div className="form-group">
        <label htmlFor={id}>
            {label}
            <span className="text-muted">{required ? "*" : ""}</span>
        </label>
        <small id="dateFormatHelp" className="form-text text-muted">
            {smallText}
        </small>
        <DatePicker id={id} selected={input.value || null} onChange={input.onChange}
                    autoComplete="new-password"/>
        {meta.touched && meta.error && 
            <span className="invalid-feedback d-block">
                {meta.error}
        </span>}
    </div>
);  

export default RenderMatchDatePicker;