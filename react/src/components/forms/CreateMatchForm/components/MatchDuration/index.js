import React from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import i18next from 'i18next';
import SelectInput from '../../../inputs/SelectInput';

const MatchDuration = ({ hourOptions, minuteOptions }) => {
    return (
        <div className="form-group col-6">
            <label>{i18next.t('createMatchForm.duration')}*</label>
            <div className="form-row">
                <div className="col-6">
                    <Field name="durationHours" required={false} 
                            defaultText={i18next.t('createMatchForm.chooseHour')}
                            options={hourOptions} component={SelectInput} />  
                </div>
                <div className="col-6">
                    <Field name="durationMinutes" required={false} 
                            defaultText={i18next.t('createMatchForm.chooseMinute')}
                            options={minuteOptions} component={SelectInput} />  
                </div>
            </div>
        </div>
    );
}

MatchDuration.propTypes = {
    hourOptions: PropTypes.array.isRequired,
    minuteOptions: PropTypes.array.isRequired
}

export default MatchDuration;