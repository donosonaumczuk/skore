import React from 'react';

const SubLocationInput = ({ label, id, path })  => {
    return (
        <div className="form-group">
            <label htmlFor={id}>{label}</label>
            <input type="text" path={path} className="form-control" id={id} readOnly={true} />
        </div>
    );
}

export default SubLocationInput;