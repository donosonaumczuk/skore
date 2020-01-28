import React from 'react';
import TimePicker from 'rc-time-picker';
import moment from 'moment';

const handleTime = time => {
    const newTime = moment(time).format("hh:mm");
    return newTime;
    //TODO update state here
}

const RenderTimePicker = ({ input, meta, label, ...rest }) => {
    return (
        <div className="form-group">
            <label htmlFor="timepicker-from">{label}<span className="text-muted">*</span></label>
                <div className="input-group date pt-4" id="timepicker-from" data-target-input="nearest">
                    <TimePicker showSecond={false} onChange={(time) => handleTime(time)} />    
                </div>
        </div>
    );
}

export default RenderTimePicker;