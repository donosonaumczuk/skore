import { getDateWithParamFormat, addFutureMinTimeToParams } from '../Util';

//getDateWithParamFormat tests
test('getDateWithParamFormat with complete date and time', () => {
    //set up
    const year = 1994;
    const month = 11; //use month number -1
    const day = 26;
    const hours = 23;
    const minutes = 55;
    const date = new Date(year, month, day, hours, minutes);
    const expectedResult = "1994-12-26T23:55";

    //execution
    const actualResult = getDateWithParamFormat(date);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

test('getDateWithParamFormat with incomplete date and time', () => {
    //set up
    const year = 1994;
    const month = 11; //use month number -1
    const day = 26;
    const hours = 9;
    const minutes = 5;
    const date = new Date(year, month, day, hours, minutes);
    const expectedResult = "1994-12-26T09:05";

    //execution
    const actualResult = getDateWithParamFormat(date);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

test('getDateWithParamFormat with incomplete date', () => {
    //set up
    const year = 1994;
    const month = 11; //use month number -1
    const day = 26;
    const hours = 9;
    const minutes = 15;
    const date = new Date(year, month, day, hours, minutes);
    const expectedResult = "1994-12-26T09:15";

    //execution
    const actualResult = getDateWithParamFormat(date);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

test('getDateWithParamFormat with incomplete time', () => {
    //set up
    const year = 1994;
    const month = 11; //use month number -1
    const day = 26;
    const hours = 23;
    const minutes = 5;
    const date = new Date(year, month, day, hours, minutes);
    const expectedResult = "1994-12-26T23:05";

    //execution
    const actualResult = getDateWithParamFormat(date);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

//addFutureMinTimeToParams tests
test('addFutureMinTimeToParams with empty param', () => {
    //set up
    const param = {};

    //execution
    const actualResult = addFutureMinTimeToParams(param);

    //postconditions
    expect(actualResult.minStartTime).toBeDefined();
});

//addFutureMinTimeToParams tests
test('addFutureMinTimeToParams with nonempty param', () => {
    //set up
    const param = { key1: "value1", key2: "value2" };

    //execution
    const actualResult = addFutureMinTimeToParams(param);

    //postconditions
    expect(actualResult.key1).toBeDefined();
    expect(actualResult.key2).toBeDefined();
    expect(actualResult.minStartTime).toBeDefined();
});
