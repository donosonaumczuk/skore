import React from 'react';

const RadioInput = (labelStyle, id,  radioValue, labelText) => {
    return (
        <label className={labelStyle} id={id}>
            <input type="radio"  name="options" value={radioValue} autocomplete="false"/>{labelText}
        </label>
    );
}

export default RadioInput