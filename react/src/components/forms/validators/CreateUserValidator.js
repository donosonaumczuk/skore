import i18next from 'i18next';
import { isStringLengthBetween, hasStringValidSymbols, 
        isStringAlphaNumeric, isStringValidEmail,
        isStringNumeric, 
        isStringAlphaOrSpaces} from './utils/stringValidations';

//username constants
const MIN_USERNAME_LENGTH = 4;
const MAX_USERNAME_LENGTH = 100;
const usernameInvalidSymbols= /[^a-zA-Z0-9_.]/;

//password constants
const MIN_PASSWORD_LENGTH = 5;
const MAX_PASSWORD_LENGTH = 100;

//names constants
const MIN_NAME_LENGTH = 2;
const MAX_NAME_LENGTH = 100;

//image constants
const MAX_IMAGE_SIZE_IN_BYTES = 1048576;

//cellphone constants
const CELLPHONE_LENGTH = 10;

const validateRequiredField = (fieldValue, errorLabel, invalidSymbols, minLength, maxLength) => {
    //TODO make breakline appear between errors, i do not know why it is not working
    let errorLabelBase = `createUserForm.errors.${errorLabel}`;
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

const hasUsernameValidSymbols = username => hasStringValidSymbols(username, usernameInvalidSymbols);

const validateUsername = username => validateRequiredField(username, "username", hasUsernameValidSymbols,
                                                                MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH);
const validatePassword = password => validateRequiredField(password, "password", isStringAlphaNumeric,
                                                                MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);

const validateRepeatedPassword = (repeatedPassword, password) => {
    let errorMessage = validateRequiredField(repeatedPassword, "repeatedPassword", isStringAlphaNumeric,
                                                                MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);
    if (repeatedPassword !== password) {
        errorMessage =`${errorMessage} ${i18next.t('createUserForm.errors.repeatedPassword.passwordDoesNotMatch')}`;
    }
    return errorMessage;
}

//TODO add tildes
const validateFirstName = firstName => validateRequiredField(firstName, "firstName", isStringAlphaOrSpaces,
                                                                    MIN_NAME_LENGTH, MAX_NAME_LENGTH);
//TODO add tildes
const validateLastName = lastName => validateRequiredField(lastName, "lastName", isStringAlphaOrSpaces,
                                                                    MIN_NAME_LENGTH, MAX_NAME_LENGTH);

const validateEmail = email => {
    let errorLabelBase = `createUserForm.errors.email`;
    let errorMessage = ``;
    if (!email) {
        errorMessage = i18next.t(`${errorLabelBase}.required`);
    }
    if (email && !isStringValidEmail(email)) {
        errorMessage =`${errorMessage} ${i18next.t(`${errorLabelBase}.invalidEmail`)}`;
    }
    return errorMessage;
}

const validImageFormats = imageFormat => imageFormat !== "image/png" && imageFormat !== "image/jpeg"
                                                 && imageFormat !== "image/jpg";

const validateImage = image => {
    let errorLabelBase = `createUserForm.errors.image`;
    let errorMessage = ``;
    if (image && !validImageFormats) {
        errorMessage = i18next.t(`${errorLabelBase}.invalidImageFormat`);
    }
    if (image && image.size > MAX_IMAGE_SIZE_IN_BYTES) {
        errorMessage =`${errorMessage} ${i18next.t(`${errorLabelBase}.invalidImageSize`)}`;
    }
    return errorMessage;
}

const validateCellphone = cellphone => {
    if (cellphone && (cellphone.length !== CELLPHONE_LENGTH || !isStringNumeric(cellphone))) {
        return i18next.t(`createUserForm.errors.cellphone.invalidCellphone`);
    }
}

const CreateUserFormValidator = {
    validateUsername: validateUsername,
    validatePassword: validatePassword,
    validateRepeatedPassword: validateRepeatedPassword,
    validateFirstName: validateFirstName,
    validateLastName: validateLastName,
    validateEmail: validateEmail,
    validateImage: validateImage,
    validateCellphone: validateCellphone
}

export default CreateUserFormValidator; 