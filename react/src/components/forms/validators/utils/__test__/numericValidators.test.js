import { isNumberBetweenValues,
            isStringBetweenValues } from '../NumericValidators';

//isNumberBetweenValues tests
test('isNumberBetweenValues with valid number in open interval', () => {
    //set up
    const min = 10;
    const max = 14;
    const number = 12;

    //execution
    const actualResult = isNumberBetweenValues(number, min, max);

    //postconditions
    expect(actualResult).toBeTruthy();
});

test('isNumberBetweenValues with valid number on interval limits', () => {
    //set up
    const min = 10;
    const max = 14;
    const number1 = 10;
    const number2 = 14;

    //execution
    const actualResult1 = isNumberBetweenValues(number1, min, max);
    const actualResult2 = isNumberBetweenValues(number2, min, max);

    //postconditions
    expect(actualResult1).toBeTruthy();
    expect(actualResult2).toBeTruthy();
});

test('isNumberBetweenValues with number outside interval', () => {
    //set up
    const min = 10;
    const max = 14;
    const number1 = 9;
    const number2 = 15;

    //execution
    const actualResult1 = isNumberBetweenValues(number1, min, max);
    const actualResult2 = isNumberBetweenValues(number2, min, max);

    //postconditions
    expect(actualResult1).toBeFalsy();
    expect(actualResult2).toBeFalsy();
});

//isStringBetweenValues tests
test('isStringBetweenValues with valid string in open interval', () => {
    //set up
    const min = 10;
    const max = 14;
    const stringValue = "12";

    //execution
    const actualResult = isStringBetweenValues(stringValue, min, max);

    //postconditions
    expect(actualResult).toBeTruthy();
});

test('isStringBetweenValues with valid string on interval limits', () => {
    //set up
    const min = 10;
    const max = 14;
    const stringValue1 = "10";
    const stringValue2 = "14";


    //execution
    const actualResult1 = isStringBetweenValues(stringValue1, min, max);
    const actualResult2 = isStringBetweenValues(stringValue2, min, max);

    //postconditions
    expect(actualResult1).toBeTruthy();
    expect(actualResult2).toBeTruthy();

});

test('isStringBetweenValues with string outside interval', () => {
    //set up
    const min = 10;
    const max = 14;
    const stringValue1 = "9";
    const stringValue2 = "15";


    //execution
    const actualResult1 = isStringBetweenValues(stringValue1, min, max);
    const actualResult2 = isStringBetweenValues(stringValue2, min, max);

    //postconditions
    expect(actualResult1).toBeFalsy();
    expect(actualResult2).toBeFalsy();
});

test('isStringBetweenValues with invalid string', () => {
    //set up
    const min = 10;
    const max = 14;
    const stringValue1 = "stringValue";
    const stringValue2 = "10.5";
    const stringValue3 = "12,5";
    const stringValue4 = "11-5";

    //execution
    const actualResult1 = isStringBetweenValues(stringValue1, min, max);
    const actualResult2 = isStringBetweenValues(stringValue2, min, max);
    const actualResult3 = isStringBetweenValues(stringValue3, min, max);
    const actualResult4 = isStringBetweenValues(stringValue4, min, max);

    //postconditions
    expect(actualResult1).toBeFalsy();
    expect(actualResult2).toBeFalsy();
    expect(actualResult3).toBeFalsy();
    expect(actualResult4).toBeFalsy();
});