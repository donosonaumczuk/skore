import React from 'react';
import PropTypes from 'prop-types';

const Tab = ({ text, isActive, number, handleChange }) => {
    const activeClass = isActive ? "active" : "";
    const checkedValue= isActive ? true : false;
    return (
        <label className={`btn btn-secondary ${activeClass}`} id="to-join">
            <input type="radio" name="options" id={`option${number}`} autoComplete="off" 
                    checked={checkedValue} onChange={() => handleChange(number)} /> 
            {text}
        </label>
    )
}

Tab.propTypes = {
    text: PropTypes.string.isRequired,
    isActive: PropTypes.bool.isRequired,
    number: PropTypes.number.isRequired,
    handleChange: PropTypes.func.isRequired
}

export default Tab;