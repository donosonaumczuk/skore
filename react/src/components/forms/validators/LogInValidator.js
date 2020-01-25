import i18next from 'i18next';

const validateOnlyRequiredField = (fieldValue, errorLabel) => {
    //TODO make breakline appear between errors, i do not know why it is not working
    let errorMessage = ``;
    if (!fieldValue) {
        errorMessage = i18next.t(`${errorLabel}`);
    }
    return errorMessage;
}

const validateUsername = username => validateOnlyRequiredField(username, 'login.errors.requiredUsername');

const validatePassword = password => validateOnlyRequiredField(password, 'login.errors.requiredPassword');

const LogInValidator = {
    validateUsername,
    validatePassword
}

export default LogInValidator;
