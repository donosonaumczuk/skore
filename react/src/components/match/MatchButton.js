import React from 'react';


const MatchButton = ({ buttonStyle, handleClick, buttonText, fontAwesome }) => {
    return (
        <button className={buttonStyle} to="" onClick={() => handleClick()} >
            <i className={fontAwesome}></i>
            {buttonText}
        </button>
    );
}

export default MatchButton;