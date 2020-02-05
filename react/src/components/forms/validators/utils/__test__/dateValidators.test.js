import { isValidDate, isValidPastDate,
            isValidFutureDate } from '../DateValidators';

//isValidDateTests
test('isValidDate with valid date', () => {
    //set up
    const day = 26, month = 12, year = 1994;

    //execution
    const asctualResult = isValidDate(day, month, year);

    //postconditions
    expect(asctualResult).toBeTruthy();
});

test('isValidDate with inValid year', () => {
    //set up
    const day = 26, month = 12, year = -1;

    //execution
    const asctualResult = isValidDate(day, month, year);

    //postconditions
    expect(asctualResult).toBeFalsy();
});

test('isValidDate with inValid month', () => {
    //set up
    const day1 = 26, month1 = 0, year1 = 1994;
    const day2 = 26, month2 = 13, year2 = 1994;


    //execution
    const asctualResult1 = isValidDate(day1, month1, year1);
    const asctualResult2 = isValidDate(day2, month2, year2);

    //postconditions
    expect(asctualResult1).toBeFalsy();
    expect(asctualResult2).toBeFalsy();
});

test('isValidDate with inValid day', () => {
    //set up
    const day1 = 0, month1 = 12, year1 = 1994;
    const day2 = 32, month2 = 12, year2 = 1994;


    //execution
    const asctualResult1 = isValidDate(day1, month1, year1);
    const asctualResult2 = isValidDate(day2, month2, year2);

    //postconditions
    expect(asctualResult1).toBeFalsy();
    expect(asctualResult2).toBeFalsy();
});

test('isValidDate with valid leap year', () => {
    //set up
    const day = 29, month = 2, year = 2016;


    //execution
    const asctualResult = isValidDate(day, month, year);

    //postconditions
    expect(asctualResult).toBeTruthy();
});

test('isValidDate with invalid leap year', () => {
    //set up
    const day = 29, month = 2, year = 2015;


    //execution
    const asctualResult = isValidDate(day, month, year);

    //postconditions
    expect(asctualResult).toBeFalsy();
});

//isValidPastDate tests
test('isValidPastDate with valid past date', () => {
    //set up
    const day = 4, month = 2, year = 2015;

    //execution
    const asctualResult = isValidPastDate(day, month, year);

    //postconditions
    expect(asctualResult).toBeTruthy();
});

test('isValidPastDate with invalid past date', () => {
    //set up
    const currentDate = new Date();
    const day = 4, month = 2, year = currentDate.getFullYear() + 1;

    //execution
    const asctualResult = isValidPastDate(day, month, year);

    //postconditions
    expect(asctualResult).toBeFalsy();
});

test('isValidPastDate with invalid current date', () => {
    //set up
    const currentDate = new Date();
    const day = currentDate.getDate();
    const month = currentDate.getMonth() + 1;
    const year = currentDate.getFullYear();

    //execution
    const asctualResult = isValidPastDate(day, month, year);

    //postconditions
    expect(asctualResult).toBeFalsy();
});

//isValidFutureDate tests
test('isValidFutureDate with valid future date', () => {
    //set up
    const currentDate = new Date();
    const day = 26, month = 12, year = currentDate.getFullYear() + 1;

    //execution
    const asctualResult = isValidFutureDate(day, month, year);

    //postconditions
    expect(asctualResult).toBeTruthy();
});

test('isValidFutureDate with invalid future date', () => {
    //set up
    const day = 26, month = 12, year = 1994;

    //execution
    const asctualResult = isValidFutureDate(day, month, year);

    //postconditions
    expect(asctualResult).toBeFalsy();
});