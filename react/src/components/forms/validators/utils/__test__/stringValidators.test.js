import { hasStringInvalidSymbols, isStringAlpha, isStringAlphaOrSpaces, isStringNumeric, isStringAlphaNumeric, isStringAlphaNumericOrSpaces, isStringValidEmail } from '../StringValidators';

//hasStringInvalidSymbols tests
test('hasStringInvalidSymbols with all valid Symbols', () => {
    //set up
    const invalidSymbolsRegex = /[^a-z]/;
    const text = "test";

    //execution
    const actualResult = hasStringInvalidSymbols(text, invalidSymbolsRegex);

    //postconditions
    expect(actualResult).toBeFalsy();
});

test('hasStringInvalidSymbols with invalid Symbols', () => {
    //set up
    const invalidSymbolsRegex = /[^a-z]/;
    const text = "Test";

    //execution
    const actualResult = hasStringInvalidSymbols(text, invalidSymbolsRegex);

    //postconditions
    expect(actualResult).toBeTruthy();
});

//isStringAlpha tests
test('isStringAlpha with valid values', () => {
    //set up
    const text1 = "test";
    const text2 = "Test";

    //execution
    const actualResult1 = isStringAlpha(text1);
    const actualResult2 = isStringAlpha(text2);

    //postconditions
    expect(actualResult1).toBeTruthy();
    expect(actualResult2).toBeTruthy();
});

test('isStringAlpha with invalid values', () => {
    //set up
    const text1 = "test1";
    const text2 = "Test-";
    const text3 = "Test/";

    //execution
    const actualResult1 = isStringAlpha(text1);
    const actualResult2 = isStringAlpha(text2);
    const actualResult3 = isStringAlpha(text3);

    //postconditions
    expect(actualResult1).toBeFalsy();
    expect(actualResult2).toBeFalsy();
    expect(actualResult3).toBeFalsy();
});

//isStringAlphaOrSpaces tests
test('isStringAlphaOrSpaces with valid values', () => {
    //set up
    const text1 = "test one";
    const text2 = "Test two";

    //execution
    const actualResult1 = isStringAlphaOrSpaces(text1);
    const actualResult2 = isStringAlphaOrSpaces(text2);

    //postconditions
    expect(actualResult1).toBeTruthy();
    expect(actualResult2).toBeTruthy();
});

test('isStringAlphaOrSpaces with invalid values', () => {
    //set up
    const text1 = "test 1";
    const text2 = "Test -";

    //execution
    const actualResult1 = isStringAlphaOrSpaces(text1);
    const actualResult2 = isStringAlphaOrSpaces(text2);

    //postconditions
    expect(actualResult1).toBeFalsy();
    expect(actualResult2).toBeFalsy();
});

//isStringNumeric tests
test('isStringNumeric with valid values', () => {
    //set up
    const numericText = "1234";

    //execution
    const actualResult = isStringNumeric(numericText);

    //postconditions
    expect(actualResult).toBeTruthy();
});

test('isStringNumeric with invalid values', () => {
    //set up
    const numericText = "A1234";

    //execution
    const actualResult = isStringNumeric(numericText);

    //postconditions
    expect(actualResult).toBeFalsy();
});

//isStringAlphaNumeric
test('isStringAlphaNumeric with valid values', () => {
    //set up
    const alphaNumericText = "test1";

    //execution
    const actualResult = isStringAlphaNumeric(alphaNumericText);

    //postconditions
    expect(actualResult).toBeTruthy();
});

test('isStringAlphaNumeric with invalid values', () => {
    //set up
    const alphaNumericText = "test 1";

    //execution
    const actualResult = isStringAlphaNumeric(alphaNumericText);

    //postconditions
    expect(actualResult).toBeFalsy();
});

//isStringAlphaNumericOrSpaces
test('isStringAlphaNumericOrSpaces with valid values', () => {
    //set up
    const alphaNumericText = "test 1";

    //execution
    const actualResult = isStringAlphaNumericOrSpaces(alphaNumericText);

    //postconditions
    expect(actualResult).toBeTruthy();
});

test('isStringAlphaNumericOrSpaces with valid values', () => {
    //set up
    const alphaNumericText = "test 1-";

    //execution
    const actualResult = isStringAlphaNumericOrSpaces(alphaNumericText);

    //postconditions
    expect(actualResult).toBeFalsy();
});

//isStringValidEmail
test('isStringValidEmail with valid value', () => {
    //set up
    const email = "Aizaguirre@itba.edu.ar";

    //execution
    const actualResult = isStringValidEmail(email);

    //postconditions
    expect(actualResult).toBeTruthy();
});

test('isStringValidEmail without at', () => {
    //set up
    const email = "Aizaguirreitba.edu.ar";

    //execution
    const actualResult = isStringValidEmail(email);

    //postconditions
    expect(actualResult).toBeFalsy();
});

test('isStringValidEmail with two ats', () => {
    //set up
    const email = "Aizaguirre@itba.ed@u.ar";

    //execution
    const actualResult = isStringValidEmail(email);

    //postconditions
    expect(actualResult).toBeFalsy();
});

test('isStringValidEmail ending with at', () => {
    //set up
    const email = "Aizaguirreitba.edu.ar@";

    //execution
    const actualResult = isStringValidEmail(email);

    //postconditions
    expect(actualResult).toBeFalsy();
});

test('isStringValidEmail ending without dot', () => {
    //set up
    const email = "Aizaguirre@itbaeduar";

    //execution
    const actualResult = isStringValidEmail(email);

    //postconditions
    expect(actualResult).toBeFalsy();
});

test('isStringValidEmail ending with invalid symbols', () => {
    //set up
    const email1 = "Aizaguirre(@itba.edu.ar@";
    const email2 = "Aizaguirre`@itba.edu.ar@";
    const email3 = "Aizaguirré@itba.edu.ar@";


    //execution
    const actualResult1 = isStringValidEmail(email1);
    const actualResult2 = isStringValidEmail(email2);
    const actualResult3 = isStringValidEmail(email3);

    //postconditions
    expect(actualResult1).toBeFalsy();
    expect(actualResult2).toBeFalsy();
    expect(actualResult3).toBeFalsy();
});