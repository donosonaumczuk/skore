import React from 'react';

const SubLocationInput = ({ label, id, path, value })  => {
    return (
        <div className="form-group">
            <label htmlFor={id}>{label}</label>
            <input type="text" path={path} className="form-control" id={id} 
                    value={value} readOnly={true} />
        </div>
    );
}

export default SubLocationInput;