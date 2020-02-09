import React from 'react';
import { Field } from 'redux-form';
import i18next from 'i18next';
import SelectInput from '../SelectInput';
import Utils from '../../../utils/Utils';

const MAX_AGE_FOR_PLAYING = 100;
const MIN_AGE_FOR_PLAYING = 5;
const MIN_MONTH = 1;
const MAX_MONTH = 12;
const MAX_DAY_FOR_MONTHS = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
const MIN_DAY = 1;

const getYearOptions = () => {
    const currentYear = new Date().getFullYear();
    const minYear = currentYear - MAX_AGE_FOR_PLAYING;
    const maxYear = currentYear - MIN_AGE_FOR_PLAYING;
    return Utils.generateOptionsForSelectBetweenValues(minYear, maxYear);
}

const getMonthOptions = () => {
    const minMonth = MIN_MONTH;
    const maxMonth = MAX_MONTH;
    return Utils.generateOptionsForSelectBetweenValues(minMonth, maxMonth);
}

const getDayOptions = (month) => {
    const minDay = MIN_DAY;
    let maxDay = MAX_DAY_FOR_MONTHS[month - 1];
    return Utils.generateOptionsForSelectBetweenValues(minDay, maxDay);
}

const BirthdayInput = () => {
    const yearOptions = getYearOptions();
    const monthOptions = getMonthOptions();
    const dayOptions = getDayOptions(1);

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