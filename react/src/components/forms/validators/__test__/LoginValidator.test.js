import i18next from 'i18next';
import LoginValidator from '../LogInValidator';

const ERROR_BASE_LABEL = 'login.errors.';

//validateUsername tests
test('validateUsername with valid value', () => {
    //setup
    const username = "username";
    const expectedResult = ""

    //execution
    const actualResult = LoginValidator.validateUsername(username);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

test('validateUsername without value', () => {
    //setup
    const username = null;
    const expectedResult = i18next.t(`${ERROR_BASE_LABEL}username.required`);

    //execution
    const actualResult = LoginValidator.validateUsername(username);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

//validatePassword tests
test('validatePassword with valid value', () => {
    //setup
    const password = "password";
    const expectedResult = "";

    //execution
    const actualResult = LoginValidator.validatePassword(password);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

test('validatePassword without value', () => {
    //setup
    const password = null;
    const expectedResult = i18next.t(`${ERROR_BASE_LABEL}password.required`);

    //execution
    const actualResult = LoginValidator.validatePassword(password);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});