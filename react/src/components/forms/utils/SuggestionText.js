import React from 'react';

const SuggestionText = ({ suggestion, link, linkText}) => {
    
    return (
    <div className="row mt-4 text-center">
        <div className="col">
                <span className="mr-1">{suggestion}</span>
                    <a className="link" href={link}>{linkText}</a>
            </div>
        </div>
    );
}

export default SuggestionText;