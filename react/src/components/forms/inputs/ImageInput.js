import React, {Component} from 'react'

class ImageInput  extends Component{
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
            <div className="form-group">
                <label htmlFor="image">{label}{required && "*"}</label>
                <small id="imgFormatHelp" className="form-text text-muted">{acceptedFormat}</small>
                <div className="input-group custom-file">
                    <input type='file' className="custom-file-input" id="image" accept='.jpg, .png, .jpeg' onChange={this.onChange} />
                    <label className="custom-file-label" aria-describedby="inputGroupFileAddon02" >{imageName}</label>
                    {showError && <span className="invalid-feedback d-block">{meta.error}</span>}
                </div>
            </div>
        )
    }
}

export default ImageInput;

