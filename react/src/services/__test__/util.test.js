import { getDateWithParamFormat, addFutureMinTimeToParams,
        addMinFreePlacesToParams, buildUrlFromParamQueries,
        buildUrlFromParamsWithCommas, createObjectFromFiltersAndPaging,
        addCreatedByToParams, addWithoutPlayersToParams,
        addWithPlayersToParams } from '../Util';

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

test('getDateWithParamFormat with incomplete month', () => {
    //set up
    const year = 1994;
    const month = 8; //use month number -1
    const day = 26;
    const hours = 9;
    const minutes = 5;
    const date = new Date(year, month, day, hours, minutes);
    const expectedResult = "1994-09-26T09:05";

    //execution
    const actualResult = getDateWithParamFormat(date);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

test('getDateWithParamFormat with incomplete day', () => {
    //set up
    const year = 1994;
    const month = 11; //use month number -1
    const day = 6;
    const hours = 9;
    const minutes = 5;
    const date = new Date(year, month, day, hours, minutes);
    const expectedResult = "1994-12-06T09:05";

    //execution
    const actualResult = getDateWithParamFormat(date);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

test('getDateWithParamFormat with incomplete hour', () => {
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

test('getDateWithParamFormat with incomplete minutes', () => {
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

test('getDateWithParamFormat with incomplete date and time', () => {
    //set up
    const year = 1994;
    const month = 8; //use month number -1
    const day = 6;
    const hours = 4;
    const minutes = 5;
    const date = new Date(year, month, day, hours, minutes);
    const expectedResult = "1994-09-06T04:05";

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

//addMinFreePlacesToParams tests
test('addMinFreePlacesToParams with empty param', () => {
    //set up
    const param = {};

    //execution
    const actualResult = addMinFreePlacesToParams(param);

    //postconditions
    expect(actualResult.minFreePlaces).toBeDefined();
});

test('addMinFreePlacesToParams with nonempty param', () => {
    //set up
    const param = { key1: "value1", key2: "value2" };

    //execution
    const actualResult = addMinFreePlacesToParams(param);

    //postconditions
    expect(actualResult.key1).toBeDefined();
    expect(actualResult.key2).toBeDefined();
    expect(actualResult.minFreePlaces).toBeDefined();
});

//buildUrlFromParamQueries tests
test('buildUrlFromParamQueries with params', () => {
    //set up
    const params = { "country": "Argentina", "sport": "Padle" };
    const expectedResult = "?country=Argentina&sport=Padle";

    //execution
    const actualResult = buildUrlFromParamQueries(params);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

test('buildUrlFromParamQueries with empty params', () => {
    //set up
    const params = { };
    const expectedResult = "";

    //execution
    const actualResult = buildUrlFromParamQueries(params);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

test('buildUrlFromParamQueries with params with list', () => {
    //set up
    const params = { "country": "Argentina", "sport": "Padle Futbol" };
    const expectedResult = "?country=Argentina&sport=Padle Futbol";

    //execution
    const actualResult = buildUrlFromParamQueries(params);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

//buildUrlFromParamsWithCommas tests
test('buildUrlFromParamsWithCommas with params without list', () => {
    //set up
    const params = { "country": "Argentina", "sport": "Padle" };
    const expectedResult = "?country=Argentina&sport=Padle";

    //execution
    const actualResult = buildUrlFromParamsWithCommas(params);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

test('buildUrlFromParamsWithCommas with params with list', () => {
    //set up
    const params = { "country": "Argentina", "sport": "Padle Futbol" };
    const expectedResult = "?country=Argentina&sport=Padle,Futbol";

    //execution
    const actualResult = buildUrlFromParamsWithCommas(params);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

test('buildUrlFromParamsWithCommas with empty params', () => {
    //set up
    const params = { };
    const expectedResult = "";

    //execution
    const actualResult = buildUrlFromParamsWithCommas(params);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

//createObjectFromFiltersAndPaging tests
test('createObjectFromFiltersAndPaging without filters', () => {
    //set up
    const offset = 0;
    const limit = 10;
    const filters = {};

    //execution
    const actualResult = createObjectFromFiltersAndPaging(offset, limit, filters);

    //postconditions
    expect(actualResult.offset).toEqual(`${offset}`);
    expect(actualResult.limit).toEqual(`${limit}`);
});

test('createObjectFromFiltersAndPaging with filters', () => {
    //set up
    const offset = 0;
    const limit = 10;
    const filters = { "country": "Argentina", "sports": "Padle"};

    //execution
    const actualResult = createObjectFromFiltersAndPaging(offset, limit, filters);

    //postconditions
    expect(actualResult.offset).toEqual(`${offset}`);
    expect(actualResult.limit).toEqual(`${limit}`);
    expect(actualResult.country).toEqual("Argentina");
    expect(actualResult.sports).toEqual("Padle");
});

//addCreatedByToParams tests
test('addCreatedByToParams with empty param', () => {
    //set up
    const param = {};
    const username = "username";

    //execution
    const actualResult = addCreatedByToParams(param, username);

    //postconditions
    expect(actualResult.createdBy).toBe(username);
});

test('addCreatedByToParams with nonempty param', () => {
    //set up
    const param = { key1: "value1", key2: "value2" };
    const username = "username";

    //execution
    const actualResult = addCreatedByToParams(param, username);

    //postconditions
    expect(actualResult.key1).toBe("value1");
    expect(actualResult.key2).toBe("value2");
    expect(actualResult.createdBy).toBe(username);
});

//addWithoutPlayersToParams tests
test('addWithoutPlayersToParams with empty param', () => {
    //set up
    const param = {};
    const username = "username";

    //execution
    const actualResult = addWithoutPlayersToParams(param, username);

    //postconditions
    expect(actualResult.withoutPlayers).toBe(username);
});

test('addWithoutPlayersToParams with nonempty param', () => {
    //set up
    const param = { key1: "value1", key2: "value2" };
    const username = "username";

    //execution
    const actualResult = addWithoutPlayersToParams(param, username);

    //postconditions
    expect(actualResult.key1).toBe("value1");
    expect(actualResult.key2).toBe("value2");
    expect(actualResult.withoutPlayers).toBe(username);
});

//addWithPlayersToParams tests
test('addWithPlayersToParams with empty param', () => {
    //set up
    const param = {};
    const username = "username";

    //execution
    const actualResult = addWithPlayersToParams(param, username);

    //postconditions
    expect(actualResult.withPlayers).toBe(username);
})

test('addWithPlayersToParams with nonempty param', () => {
    //set up
    const param = { key1: "value1", key2: "value2" };
    const username = "username";

    //execution
    const actualResult = addWithPlayersToParams(param, username);

    //postconditions
    expect(actualResult.key1).toBe("value1");
    expect(actualResult.key2).toBe("value2");
    expect(actualResult.withPlayers).toBe(username);
});