import React from 'react';
import PropTypes from 'prop-types';

const SportImage = ({ sportImageUrl }) => {
    return (
        <div className="col">
            <div className="row mb-4 pt-4">
                <div className="col-2 col-sm-3">
                    <div className="container-fluid pt-2">
                        <img src={sportImageUrl} className="sport-img" alt="sport-pic" />
                    </div>
                </div>
            </div>
        </div>
    );
}

SportImage.propTypes = {
    sportImageUrl: PropTypes.string.isRequired
}

export default SportImage;