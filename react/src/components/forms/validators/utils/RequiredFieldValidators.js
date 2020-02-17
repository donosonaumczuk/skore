import i18next from 'i18next';
import { isStringLengthBetween } from './StringValidators';

const validateOnlyRequiredField = (fieldValue, errorLabel) => {
    let errorLabelBase = `${errorLabel}`;
    let errorMessage = ``;
    if (!fieldValue) {
        errorMessage = i18next.t(`${errorLabelBase}.required`);
    }
    return errorMessage;
}

const validateRequiredField = (fieldValue, errorLabel, invalidSymbols, minLength, maxLength) => {
    let errorLabelBase = `${errorLabel}`;
    let errorMessage = validateOnlyRequiredField(fieldValue, errorLabel);
    if (fieldValue && !invalidSymbols(fieldValue)) {
        errorMessage = `${errorMessage} ${i18next.t(`${errorLabelBase}.invalidSymbols`)}`;
    }
    if (fieldValue && !isStringLengthBetween(fieldValue, minLength, maxLength)) {
        errorMessage =`${errorMessage} ${i18next.t(`${errorLabelBase}.invalidLength`)}`;
    }
    return errorMessage;
}

export {
    validateOnlyRequiredField,
    validateRequiredField,
};