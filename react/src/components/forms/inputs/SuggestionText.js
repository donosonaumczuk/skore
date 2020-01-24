import React from 'react';
import { Link } from 'react-router-dom';

const SuggestionText = ({ suggestion, link, linkText}) => {
    
    return (
        <div className="row mt-4 text-center">
            <div className="col">
                <span className="mr-1">{suggestion}</span>
                <Link className="link" to={link}>{linkText}</Link>
            </div>
        </div>
    );
}

export default SuggestionText;