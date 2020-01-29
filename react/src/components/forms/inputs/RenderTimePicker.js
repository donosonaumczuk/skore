import React from 'react';
import TimePicker from 'rc-time-picker';

const RenderTimePicker = ({ input, meta, label, updateTime, ...rest }) => {
    return (
        <div className="form-group">
            <label htmlFor="timepicker-from">{label}<span className="text-muted">*</span></label>
                <div className="input-group date pt-4" id="timepicker-from" data-target-input="nearest">
                    <TimePicker showSecond={false} onChange={(time) => updateTime(time)} />    
                </div>
        </div>
    );
}

export default RenderTimePicker;