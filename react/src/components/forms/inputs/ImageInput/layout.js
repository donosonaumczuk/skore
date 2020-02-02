import React from 'react';
import PropTypes from 'prop-types';

const ImageInput = ({ label, required, acceptedFormat, onChange, imageName,
                        showError, errorMessage }) => {
    return (
            <div className="form-group">
                <label htmlFor="image">{label}{required && "*"}</label>
                <small id="imgFormatHelp" className="form-text text-muted">{acceptedFormat}</small>
                <div className="input-group custom-file">
                    <input type='file' className="custom-file-input" id="image" 
                            accept='.jpg, .png, .jpeg' onChange={onChange} />
                    <label className="custom-file-label" aria-describedby="inputGroupFileAddon02" >
                        {imageName}
                    </label>
                    {showError && <span className="invalid-feedback d-block">{errorMessage}</span>}
                </div>
            </div>
    );
}

ImageInput.propTypes = {
    label: PropTypes.string.isRequired,
    required: PropTypes.bool,
    acceptedFormat: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
    imageName: PropTypes.string,
    errorMessage: PropTypes.string,
}

export default ImageInput;