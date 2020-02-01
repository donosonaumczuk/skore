import React, {Component} from 'react';
import PropTypes from 'prop-types';
import ImageInput from './layout';

class ImageInputContainer extends Component{
    constructor(props) {
        super(props)
        this.onChange = this.onChange.bind(this)
    }

    onChange(e) {
        const { input: { onChange } } = this.props
        onChange(e.target.files[0])
    }

    render(){
        const { label, acceptedFormat, imageName, meta, required, checkOnSubmit } = this.props
        let showError = meta.error;
        if (checkOnSubmit) {
            showError = showError && meta.submitFailed;
        }
        return (
            <ImageInput label={label} required={required} acceptedFormat={acceptedFormat}
                        onChange={this.onChange} imageName={imageName} showError={showError}
                        errorMessage={meta.error} />
        );
    }
}

ImageInputContainer.propTypes = {
    label: PropTypes.string.isRequired,
    acceptedFormat: PropTypes.string.isRequired,
    imageName: PropTypes.string,
    meta: PropTypes.object.isRequired,
    required: PropTypes.bool,
    checkOnSubmit: PropTypes.bool,
}

export default ImageInputContainer;

