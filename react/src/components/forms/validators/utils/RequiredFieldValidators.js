import i18next from 'i18next';
import { isStringLengthBetween } from './StringValidators';

const validateRequiredField = (fieldValue, errorLabel, invalidSymbols, minLength, maxLength) => {
    //TODO make breakline appear between errors, i do not know why it is not working
    let errorLabelBase = `${errorLabel}`;
    let errorMessage = ``;
    if (!fieldValue) {
        errorMessage = i18next.t(`${errorLabelBase}.required`);
    }
    if (fieldValue && !invalidSymbols(fieldValue)) {
        errorMessage = `${errorMessage} ${i18next.t(`${errorLabelBase}.invalidSymbols`)}`;
    }
    if (fieldValue && !isStringLengthBetween(fieldValue, minLength, maxLength)) {
        errorMessage =`${errorMessage} ${i18next.t(`${errorLabelBase}.invalidLength`)}`;
    }
    return errorMessage;
}

export {
    validateRequiredField
};