import moment from 'moment';
import i18next from 'i18next';
import { hasStringValidSymbols } from './utils/StringValidators';
import { validateRequiredField, validateOnlyRequiredField } from './utils/RequiredFieldValidators';
import { isNumberBetweenValues } from './utils/NumericValidators';
import { isValidFutureDate } from './utils/DateValidators';

const ERROR_BASE_LABEL = "createMatchForm.errors.";

//title constants
const MIN_TITLE_LENGTH = 4;
const MAX_TITLE_LENGTH = 140;
const titleInvalidSymbols= /[^a-zA-Z0-9ñŃáéíóúöüÁÉÍÓÚ¿?¡!-_. ]/;

//time constants
const MIN_VALID_HOUR = 0;
const MAX_VALID_HOUR = 23;
const MIN_VALID_MINUTE = 0;
const MAX_VALID_MINUTE = 59;

//description constants
const MIN_DESCRIPTION_LENGTH = 0;
const MAX_DESCRIPTION_LENGTH = 140;

const hasTitleValidSymbols = title => hasStringValidSymbols(title, titleInvalidSymbols);

//TODO make string validation
const validateTitle = title => validateRequiredField(title, `${ERROR_BASE_LABEL}title`,
                                                        hasTitleValidSymbols, MIN_TITLE_LENGTH, MAX_TITLE_LENGTH)

const validateCompetitivity = competitivity => validateOnlyRequiredField(competitivity, `${ERROR_BASE_LABEL}competitivity`);

const validateSport = sport => {
    if (!sport || sport === "") {
        return i18next.t(`${ERROR_BASE_LABEL}sport.required`)
    }
}

const validateDate = date => {
    const errorBaseLabel = `${ERROR_BASE_LABEL}date`;
    let errorMessage = validateOnlyRequiredField(date, errorBaseLabel);
    const dateArray = moment(date).format("MM/DD/YYYY").split("/");
    const day = parseInt(dateArray[1]);
    const month = parseInt(dateArray[0]);
    const year = parseInt(dateArray[2]);
    if (!isValidFutureDate(day, month, year)) {
        errorMessage = `${errorMessage} ${i18next.t(`${errorBaseLabel}.invalidFutureDate`)}`;
    }
    return errorMessage;
}

const isValidTime = time => {
    const timeArray = moment(time).format("hh:mm").split(":");
    const hour = parseInt(timeArray[0]);
    const minutes = parseInt(timeArray[1]);
    if (!isNumberBetweenValues(hour, 0, 23) || !isNumberBetweenValues(minutes, 0, 59)) {
        return false;
    }
}

const validateTime = time => {
    const errorBaseLabel = `${ERROR_BASE_LABEL}time`
    let errorMessage = ``;
    if (!time || !time.hour || !time.minutes) {
        errorMessage = `${errorMessage} ${i18next.t(`${errorBaseLabel}.required`)}`;
    }
    else {
        const hour = time.hour;
        const minutes = time.minutes;
        if (!isNumberBetweenValues(hour, MIN_VALID_HOUR, MAX_VALID_HOUR) ||
            !isNumberBetweenValues(minutes, MIN_VALID_MINUTE, MAX_VALID_MINUTE)) {
            errorMessage = `${errorMessage} ${i18next.t(`${errorBaseLabel}.invalidTime`)}`;
        }
    }
    return errorMessage;
}

const validateDurationHours = durationHours => validateOnlyRequiredField(durationHours, 
                                                                    `${ERROR_BASE_LABEL}durationHours`);

const validateDurationMinutes = durationMinutes => validateOnlyRequiredField(durationMinutes, 
                                                                    `${ERROR_BASE_LABEL}durationMinutes`);

const validateDescription = description => {
    const errorBaseLabel = `${ERROR_BASE_LABEL}description`;
    let errorMessage = ``;
    if (description) {
        const descriptionLength = description.length;
        if (!isNumberBetweenValues(descriptionLength, MIN_DESCRIPTION_LENGTH, MAX_DESCRIPTION_LENGTH)) {
            errorMessage = `${errorMessage} ${i18next.t(`${errorBaseLabel}.invalidLength`)}`;
        }
    }
    return errorMessage;
}

const validateLocation = location => {
    const errorBaseLabel = `${ERROR_BASE_LABEL}location`;
    let errorMessage = ``;
    if (!location || !location.street || !location.number) {
        errorMessage = `${errorMessage} ${i18next.t(`${errorBaseLabel}.required`)}`;
    }
    return errorMessage;
}

const CreateMatchValidator = {
    validateTitle: validateTitle,
    validateCompetitivity: validateCompetitivity,
    validateSport: validateSport,
    validateDate: validateDate,
    isValidTime: isValidTime,
    validateTime: validateTime,
    validateDurationHours: validateDurationHours,
    validateDurationMinutes: validateDurationMinutes,
    validateDescription: validateDescription,
    validateLocation: validateLocation
};

export default CreateMatchValidator;
