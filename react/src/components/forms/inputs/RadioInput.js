import React from 'react';

const RadioInput = ({ input, meta, labelStyle, id, labelText, radioValue, ...rest }) => {
    const checked = input.value === radioValue;
    //TODO update state with selected tab to implement future form with competitive and individual
    return (
        <label className={labelStyle} id={id}>
            <input {...input} type="radio" {...rest} checked={checked} />
            <i className={checked ? "fas fa-check" : "d-none fas fa-check"}></i>
            {labelText}
        </label>
    );
}

export default RadioInput