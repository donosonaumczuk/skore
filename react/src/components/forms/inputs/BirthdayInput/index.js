import React from 'react';
import { Field } from 'redux-form';
import i18next from 'i18next';
import SelectInput from '../SelectInput';

const BirthdayInput = () => {
    const yearOptions = null, monthOptions = null, dayOptions = null;
    return (
        <div className="row">
            <div className="col">
                <Field name="year" label={i18next.t('createUserForm.year')} 
                        required={true} options={yearOptions}
                        defaultText={i18next.t('createUserForm.chooseSport')}
                        component={SelectInput} />
            </div>
            <div className="col">
                <Field name="month" label={i18next.t('createUserForm.month')} 
                        required={true} options={monthOptions}
                        defaultText={i18next.t('createUserForm.month')}
                        component={SelectInput} />
            </div>
            <div className="col">
                <Field name="day" label={i18next.t('createUserForm.day')} 
                        required={true} options={dayOptions}
                        defaultText={i18next.t('createUserForm.day')}
                        component={SelectInput} />
            </div>
        </div>
    );
}

export default BirthdayInput;