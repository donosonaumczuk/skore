import React from 'react';
import DatePicker from 'react-datepicker';
import PropTypes from 'prop-types';

const RenderMatchDatePicker = ({ input, label, id, required, smallText, meta }) => {
    return (
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
            {meta.dirty && meta.error && 
                <span className="invalid-feedback d-block">
                    {meta.error}
                </span>}
        </div>
    );  
}

RenderMatchDatePicker.propTypes = {
    input: PropTypes.object.isRequired,
    label: PropTypes.string.isRequired,
    id: PropTypes.string.isRequired,
    required: PropTypes.bool,
    smallText: PropTypes.string.isRequired,
    meta: PropTypes.object.isRequired
}

export default RenderMatchDatePicker;