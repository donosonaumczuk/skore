import React from 'react';
import TimePicker from 'rc-time-picker';
import PropTypes from 'prop-types';

const RenderTimePicker = ({ input, meta, label, updateTime, ...rest }) => {
    return (
        <div className="form-group">
            <label htmlFor="timepicker-from">{label}<span className="text-muted">*</span></label>
                <div className="input-group date pt-4" id="timepicker-from" data-target-input="nearest">
                    <TimePicker showSecond={false} onChange={(time) => updateTime(time)} />    
                </div>
                {meta.error && meta.submitFailed &&
                    <span className="invalid-feedback d-block">
                        {meta.error}
                    </span>
                }
        </div>
    );
}

RenderTimePicker.propTypes = {
    input: PropTypes.object.isRequired,
    meta: PropTypes.object.isRequired,
    label: PropTypes.string.isRequired,
    updateTime: PropTypes.func.isRequired
}

export default RenderTimePicker;