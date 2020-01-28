import React from 'react';

const SubLocationInput = ({ divStyle, label, id, path, value })  => {
    return (
        <div className={divStyle}>
            <label htmlFor={id}>{label}</label>
            <input type="text" path={path} className="form-control" id={id} 
                    value={value} readOnly={true} />
        </div>
    );
}

export default SubLocationInput;