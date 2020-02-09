import React from 'react';
import { Field } from 'redux-form';
import PropTypes from 'prop-types';
import i18next from 'i18next';
import SelectInput from '../SelectInput';
import Utils from '../../../utils/Utils';
import { isLeapYear } from '../../validators/utils/DateValidators';

const MAX_AGE_FOR_PLAYING = 100;
const MIN_AGE_FOR_PLAYING = 5;
const MIN_MONTH = 1;
const MAX_MONTH = 12;
const MAX_DAY_FOR_MONTHS = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
const MIN_DAY = 1;
const FEBRAURY = 2;
const FEBRAURY_LEAP_YEAR_DAYS = 29;
const DEFAULT_MONTH_NUMBER = 1;

const getYearOptions = () => {
    const currentYear = new Date().getFullYear();
    const minYear = currentYear - MAX_AGE_FOR_PLAYING;
    const maxYear = currentYear - MIN_AGE_FOR_PLAYING;
    return Utils.generateOptionsForSelectBetweenValues(minYear, maxYear).reverse();
}

const getMonthOptions = () => {
    const minMonth = MIN_MONTH;
    const maxMonth = MAX_MONTH;
    return Utils.generateOptionsForSelectBetweenValues(minMonth, maxMonth);
}

const getDayOptions = (month, year) => {
    const minDay = MIN_DAY;
    let monthNumber = parseInt(month);
    if (!monthNumber) {
        monthNumber = DEFAULT_MONTH_NUMBER;
    }
    let maxDay = MAX_DAY_FOR_MONTHS[monthNumber - 1];
    if (year && isLeapYear(year) && monthNumber === FEBRAURY) {
        maxDay = FEBRAURY_LEAP_YEAR_DAYS;
    }
    return Utils.generateOptionsForSelectBetweenValues(minDay, maxDay);
}

const getIsDayDisabled = (isMonthDisabled, birthday) => {
    if (isMonthDisabled) {
        return true;
    }
    return birthday.month ? false : true;
}

const BirthdayInput = ({ required, birthday, changeFieldValues }) => {
    const yearOptions = getYearOptions();
    const monthOptions = getMonthOptions();
    const dayOptions = getDayOptions(birthday.month, birthday.year);
    const isMonthDisabled = birthday.year ? false : true;
    const isDayDisabled = getIsDayDisabled(isMonthDisabled, birthday);
    if (birthday.day > dayOptions.length) {
        changeFieldValues('day', undefined);
    }
    return (
        <div>
            <label>
                {i18next.t('createUserForm.birthday')}
                <span className="text-muted">{required ? " *" : ""}</span>
            </label>
            <div className="row">
                <div className="col">
                    <Field name="year" label={''} 
                            required={false} options={yearOptions}
                            defaultText={i18next.t('createUserForm.year')}
                            component={SelectInput} />
                </div>
                <div className="col">
                    <Field name="month" label={''} 
                            required={false} options={monthOptions}
                            defaultText={i18next.t('createUserForm.month')}
                            component={SelectInput} isDisabled={isMonthDisabled} />
                </div>
                <div className="col">
                    <Field name="day" label={''} 
                            required={false} options={dayOptions}
                            defaultText={i18next.t('createUserForm.day')}
                            component={SelectInput} isDisabled={isDayDisabled} />
                </div>
            </div>
        </div>
    );
}

BirthdayInput.propTypes = {
    required: PropTypes.bool,
    birthday: PropTypes.object.isRequired,
    changeFieldValues: PropTypes.func.isRequired
}

export default BirthdayInput;