import { validateOnlyRequiredField } from './utils/RequiredFieldValidators';

const ERROR_BASE_LABEL = 'login.errors.'

const validateUsername = username => validateOnlyRequiredField(username, `${ERROR_BASE_LABEL}username`);

const validatePassword = password => validateOnlyRequiredField(password, `${ERROR_BASE_LABEL}password`);

const LogInValidator = {
    validateUsername,
    validatePassword
}

export default LogInValidator;
