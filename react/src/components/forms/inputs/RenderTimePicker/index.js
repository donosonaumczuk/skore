import React from 'react';
import TimePicker from 'rc-time-picker';
import PropTypes from 'prop-types';
import moment from 'moment';

const updateNewTime = (newTime, changeFieldsValue, touchField) => {
    let time = {}
    if (!newTime) {
        time = {
                hour: null,
                minutes:null
        }
    }
    else {
        const timeArray = moment(newTime).format("HH:mm").split(":");
        time = {
            hour: parseInt(timeArray[0]),
            minutes: parseInt(timeArray[1])
        }
    }
    changeFieldsValue('matchTime', time);
    touchField('matchTime');
}

const RenderTimePicker = ({ input, meta, label, changeFieldsValue, touchField, ...rest }) => {
    return (
        <div className="form-group">
            <label htmlFor="timepicker-from">{label}
                <span className="text-muted">*</span>
            </label>
            <div className="input-group date pt-4" id="timepicker-from" data-target-input="nearest">
                <TimePicker showSecond={false} onChange={(time) => 
                                                updateNewTime(time, changeFieldsValue, touchField)} />    
            </div>
            {meta.touched && meta.error &&
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
    label: PropTypes.string.isRequired
}

export default RenderTimePicker;