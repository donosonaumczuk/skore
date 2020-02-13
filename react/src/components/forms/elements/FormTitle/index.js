import React from 'react';
import { Link } from 'react-router-dom';

const FormTitle = () => {
    return (
         <div className="row text-center">
            <div className="col">
                <Link className="sign-in-brand" to="/">sk<i className="fas fa-bullseye"></i>re</Link>
            </div>
        </div>
    );
}

export default FormTitle;