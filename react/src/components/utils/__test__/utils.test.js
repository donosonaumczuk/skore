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

const compareMatchArrays = (firstArray, secondArray) => {
    const length = firstArray.length;
    if (length !== secondArray.length) {
        return false;
    }
    for (var i = 0; i < length; i++) {
        if (firstArray[i].key !== secondArray[i].key ||
            firstArray[i].value !== secondArray[i].value) {
            return false;
        }
    }
    return true;
}

//replaceWithNewMatch tests
test('replaceWithNewMatch with a valid newMatch', () => {
    //set up
    const matches  = [{ key: "first" }, { key: "second" }, { key: "third" }];
    const matchToReplace = { key: "second", value: "newValue" };

    //execution
    const expectedResult = [{ key: "first" }, 
                            { key: "second", value: "newValue" },
                            { key: "third" }];
    const actualResult = Utils.replaceWithNewMatch(matches, matchToReplace);

    //postconditions
    expect(actualResult.length).toBe(expectedResult.length);
    expect(compareMatchArrays(expectedResult, actualResult)).toBeTruthy();
});

test('replaceWithNewMatch with invalid newMatch', () => {
    //set up
    const matches  = [{ key: "first" }, { key: "second" }, { key: "third" }];
    const matchToReplace = { key: "fourth", value: "newValue" };

    //execution
    const expectedResult = [{ key: "first" }, { key: "second" }, { key: "third" }];
    const actualResult = Utils.replaceWithNewMatch(matches, matchToReplace);

    //postconditions
    expect(actualResult.length).toBe(expectedResult.length);
    expect(compareMatchArrays(expectedResult, actualResult)).toBeTruthy();
});

//deleteMatch tests
test('deleteMatch deleting one match', () => {
    //set up
    const matches  = [{ key: "first" }, { key: "second" }, { key: "third" }];
    const matchToRemove = { key: "second" };

    //execution
    const expectedResult = [{ key: "first" }, { key: "third" }];
    const actualResult = Utils.deleteMatch(matches, matchToRemove);

    //postconditions
    expect(actualResult.length).toBe(expectedResult.length);
    expect(compareMatchArrays(expectedResult, actualResult)).toBeTruthy();
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

//removeLastSpaceFromString tests
test('removeLastSpaceFromString with a valid string', () => {
    //set up
    const string1 = "test ";
    const string2 = "test";
    const string3 = "test three";
    const expectedResult1 = "test";
    const expectedResult2 = "test";
    const expectedResult3 = "test three";

    //execution
    const actualResult1 = Utils.removeLastSpaceFromString(string1);
    const actualResult2 = Utils.removeLastSpaceFromString(string2);
    const actualResult3 = Utils.removeLastSpaceFromString(string3);

    //postconditions
    expect(actualResult1).toBe(expectedResult1);
    expect(actualResult2).toBe(expectedResult2);
    expect(actualResult3).toBe(expectedResult3);
});

test('removeLastSpaceFromString with empty string', () => {
    //set up
    const string = "";
    const expectedResult = "";
    

    //execution
    const actualResult = Utils.removeLastSpaceFromString(string);

    //postconditions
    expect(actualResult).toBe(expectedResult);
});

//addressToString tests
test('addressToString with a valid address', () => {
    //set up
    const street = "street ";
    const number = "1234";
    const city = "city";
    const state = "state";
    const country = "country";
    const address1 = { street, number, city, state, country};
    const address2 = { street, city, state, country};
    const address3 = { street, number, state, country};
    const address4 = { street, number, city, country};
    const expectedResult1 = "street 1234, city, state, country";
    const expectedResult2 = "street, city, state, country";
    const expectedResult3 = "street 1234, state, country";
    const expectedResult4 = "street 1234, city, country";

    //execution
    const actualResult1 = Utils.addressToString(address1);
    const actualResult2 = Utils.addressToString(address2);
    const actualResult3 = Utils.addressToString(address3);
    const actualResult4 = Utils.addressToString(address4);

    //postconditions
    expect(actualResult1).toBe(expectedResult1);
    expect(actualResult2).toBe(expectedResult2);
    expect(actualResult3).toBe(expectedResult3);
    expect(actualResult4).toBe(expectedResult4);
});

test('addressToString with a null address', () => {
    //set up
    
    const address1 = null
    const address2 = { };
    const expectedResult1 = "";
    const expectedResult2 = "";

    //execution
    const actualResult1 = Utils.addressToString(address1);
    const actualResult2 = Utils.addressToString(address2);

    //postconditions
    expect(actualResult1).toBe(expectedResult1);
    expect(actualResult2).toBe(expectedResult2);
});

