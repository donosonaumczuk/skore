import React from 'react';
import TimePicker from 'rc-time-picker';
import PropTypes from 'prop-types';
import CreateMatchValidator from '../../validators/CreateMatchValidator';

const RenderTimePicker = ({ input, meta, label, updateTime,
                            currentValue, ...rest }) => {
    const errorMessage = CreateMatchValidator.validateTime(currentValue);
    return (
        <div className="form-group">
            <label htmlFor="timepicker-from">{label}<span className="text-muted">*</span></label>
                <div className="input-group date pt-4" id="timepicker-from" data-target-input="nearest">
                    <TimePicker showSecond={false} onChange={(time) => updateTime(time)} />    
                </div>
                {meta.submitFailed && errorMessage &&
                    <span className="invalid-feedback d-block">
                        {errorMessage}
                    </span>
                }
        </div>
    );
}

RenderTimePicker.propTypes = {
    input: PropTypes.object.isRequired,
    meta: PropTypes.object.isRequired,
    label: PropTypes.string.isRequired,
    updateTime: PropTypes.func.isRequired,
    currentValue: PropTypes.object
}

export default RenderTimePicker;