import React from 'react';
import PropTypes from 'prop-types';

const SportTextInfo = ({ text, isTitle }) => {
    let textElement = <p>{text}</p>
    if (isTitle) {
        textElement = <h3>{text}</h3>
    }
    
    return (
        <div className="col">
            <div className="row mb-4 pt-4">
                <div className="col-2 col-sm-3">
                    <div className="container-fluid pt-2">
                        {textElement}
                    </div>
                </div>
            </div>
        </div>
    );
}

SportTextInfo.propTypes = {
    text: PropTypes.string.isRequired
}

export default SportTextInfo;