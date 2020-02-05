import Utils  from '../Utils';

//hasMorePages tests
test ('hasMorePages with more pages', () => {
    //set up
    const link = [{ rel: "value" }, { rel: "next" }];

    //execution
    const actualResult = Utils.hasMorePages(link);

    //postcondition
    expect(actualResult).toBeTruthy();
});

test ('hasMorePages with no more pages', () => {
    //set up
    const link = [{ rel: "value" }, { rel: "otherValue" }];

    //execution
    const actualResult = Utils.hasMorePages(link);

    //postcondition
    expect(actualResult).toBeFalsy();
});

//buildUrlParam tests
test('buildUrlParam with tab and params', () => {
    //set up
    const params  = { country: "Argentina", state: "" };
    const tabNumber = 1;

    //execution
    const expectedResult = "?country=Argentina&tab=1"
    const actualResult = Utils.buildUrlFromParamQueriesAndTab(params, tabNumber);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

test('buildUrlParam without tab and params', () => {
    //set up
    const params  = { country: "Argentina", state: "" };

    //execution
    const expectedResult = "?country=Argentina"
    const actualResult = Utils.buildUrlFromParamQueriesAndTab(params, null);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

//removeUnknownFilters tests
test('removeUnknownFilters with all known filters', () => {
    //set up
    const countryValue = "countryValue";
    const stateValue = "stateValue";
    const cityValue = "cityValue";
    const sportValue = "sportValue";
    const filters  = { country: countryValue, state: stateValue,
                        city: cityValue , "sport": sportValue };
                    
    //execution
    const actualResult = Utils.removeUnknownHomeFilters(filters);

    //postconditions
    expect(actualResult.country).toBe(countryValue);
    expect(actualResult.state).toBe(stateValue);
    expect(actualResult.city).toBe(cityValue);
    expect(actualResult.sport).toBe(sportValue);
});

test('removeUnknownFilters with  unknown filters', () => {
    //set up
    const countryValue = "countryValue";
    const stateValue = "stateValue";
    const cityValue = "cityValue";
    const sportValue = "sportValue";
    const filters  = { country: countryValue, unknownFilter1: "unknown1", 
                        state: stateValue, city: cityValue ,
                        "sport": sportValue, unknownFilter2: "unknown2" };
                    
    //execution
    const actualResult = Utils.removeUnknownHomeFilters(filters);

    //postconditions
    expect(actualResult.country).toBe(countryValue);
    expect(actualResult.state).toBe(stateValue);
    expect(actualResult.city).toBe(cityValue);
    expect(actualResult.sport).toBe(sportValue);
    expect(actualResult.unknownFilter1).toBeUndefined();
    expect(actualResult.unknownFilter2).toBeUndefined();
});

//deleteMatch tests
const compareMatchArrays = (firstArray, secondArray) => {
    const length = firstArray.length;
    if (length !== secondArray.length) {
        return false;
    }
    for (var i = 0; i < length; i++) {
        if (firstArray[i].key !== secondArray[i].key) {
            return false;
        }
    }
    return true;
}

test('deleteMatch deleting one match', () => {
    //set up
    const matches  = [{ key: "first" }, { key: "second" }, { key: "third" }];
    const matchToRemove = { key: "second" };

    //execution
    const expectedResult = [{ key: "first" }, { key: "third" }];
    const actualResult = Utils.deleteMatch(matches, matchToRemove);

    //postconditions
    expect(actualResult.length).toBe(expectedResult.length);
    expect(compareMatchArrays(expectedResult, actualResult)).toBeTruthy;
});

test('deleteMatch without deleting', () => {
    //set up
    const matches  = [{ key: "first" }, { key: "second" }, { key: "third" }];
    const matchToRemove = { key: "fourth" };

    //execution
    const expectedResult = [{ key: "first" }, { key: "second" }, { key: "third" }];
    const actualResult = Utils.deleteMatch(matches, matchToRemove);

    //postconditions
    expect(actualResult.length).toBe(expectedResult.length);
    expect(compareMatchArrays(expectedResult, actualResult)).toBeTruthy;
});

test('deleteMatch deleting more than one match', () => {
    //set up
    const matches  = [{ key: "first" }, { key: "keyToDelete" }, { key: "keyToDelete" },
                        { key: "third" }, { key: "keyToDelete" }];
    const matchToRemove = { key: "keyToDelete" };

    //execution
    const expectedResult = [{ key: "first" }, { key: "third" }];
    const actualResult = Utils.deleteMatch(matches, matchToRemove);

    //postconditions
    expect(actualResult.length).toBe(expectedResult.length);
    expect(compareMatchArrays(expectedResult, actualResult)).toBeTruthy;
});