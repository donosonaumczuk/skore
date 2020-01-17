import React from 'react';

const Tab = ({ text, isActive, number, handleChange }) => {
    const activeClass = isActive ? "active" : "";
    const checkedValue= isActive ? true : false;
    return (
        <label className={`btn btn-secondary ${activeClass}`} id="to-join">
            <input type="radio" name="options" id="option1" autoComplete="off" 
                    checked={checkedValue} onChange={() => handleChange(number)} /> 
            {text}
        </label>
    )
}

export default Tab;


                // <label class="btn btn-secondary <c:if test="${section.equals(\"joined\")}">active</c:if>" id="joined">
                //     <input type="radio" name="options" id="option2" autocomplete="off"> 
                //     JOINED
                // </label>
                // <label class="btn btn-secondary <c:if test="${section.equals(\"created\")}">active</c:if>" id="created">
                //     <input type="radio" name="options" autocomplete="off"> 
                //     CREATED
                // </label>