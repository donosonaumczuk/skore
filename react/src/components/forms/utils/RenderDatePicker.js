import React from 'react';
import i18next from 'i18next';
import createRenderer from './CreateRenderer';

const RenderDatePicker = createRenderer((input, label, inputType) => {
    //TODO enable birthday button
    return (
        <React.Fragment>
            <small id="dateFormatHelp" className="form-text text-muted">{i18next.t('createUserForm.dateFormat')}</small>
            <div className="input-group date" id="datepicker" data-target-input="nearest">
                <input {...input} type={inputType} className="form-control datetimepicker-input" data-target="#datepicker"/>
                <div className="input-group-append" data-target="#datepicker" data-toggle="datetimepicker">
                    <div className="input-group-text"><i className="fas fa-calendar-alt"></i></div>
                </div>
            </div>
        </React.Fragment>
    );
});


export default RenderDatePicker;