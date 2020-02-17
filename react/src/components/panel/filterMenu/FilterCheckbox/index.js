import React from 'react';
import Proptypes from 'prop-types';
import {Link} from "react-router-dom";

const FilterCheckbox = ({ containerId, prefixLabelText, linkedLabelText, suffixLabelText, link,
                            inputStyle, inputId, input }) => {
    const checked = input.value === "true" || input.value === true;
    return (
        <div className="form-check form-check-inline" id={containerId}>
            <input {...input} className={inputStyle} type="checkbox"
                    id={inputId} checked={checked} />
            <label className="form-check-label ml-2" htmlFor={inputId}>
                {prefixLabelText}
                <Link className="link" to={link}>{linkedLabelText}</Link>
                {suffixLabelText}
            </label>
         </div>
    );
}

FilterCheckbox.propTypes = {
    containerId: Proptypes.string.isRequired,
    prefixLabelText: Proptypes.string.isRequired,
    linkedLabelText: Proptypes.string.isRequired,
    suffixLabelText: Proptypes.string.isRequired,
    link: Proptypes.string.isRequired,
    inputStyle: Proptypes.string.isRequired,
    inputId: Proptypes.string.isRequired,
}

export default FilterCheckbox;