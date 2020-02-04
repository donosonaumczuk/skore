import Utils  from './Utils';

test('buildUrlParam with tab and params', () => {
    //set up
    const params  = { country: "Argentina", state: "" };
    const tabNumber = 1;

    // execution
    const expectedResult = "?country=Argentina&tab=1"
    const actualResult = Utils.buildUrlFromParamQueriesAndTab(params, tabNumber);

    //after
    expect(actualResult).toBe(expectedResult);
});

test('buildUrlParam without tab and params', () => {
    //set up
    const params  = { country: "Argentina", state: "" };

    // execution
    const expectedResult = "?country=Argentina"
    const actualResult = Utils.buildUrlFromParamQueriesAndTab(params, null);

    //after
    expect(actualResult).toBe(expectedResult);
});

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

    // execution
    const expectedResult = [{ key: "first" }, { key: "third" }];
    const actualResult = Utils.deleteMatch(matches, matchToRemove);

    //after
    expect(actualResult.length).toBe(expectedResult.length);
    expect(compareMatchArrays(expectedResult, actualResult)).toBeTruthy;
});

test('deleteMatch without deleting', () => {
    //set up
    const matches  = [{ key: "first" }, { key: "second" }, { key: "third" }];
    const matchToRemove = { key: "fourth" };

    // execution
    const expectedResult = [{ key: "first" }, { key: "second" }, { key: "third" }];
    const actualResult = Utils.deleteMatch(matches, matchToRemove);

    //after
    expect(actualResult.length).toBe(expectedResult.length);
    expect(compareMatchArrays(expectedResult, actualResult)).toBeTruthy;
});

test('deleteMatch deleting more than one match', () => {
    //set up
    const matches  = [{ key: "first" }, { key: "keyToDelete" }, { key: "keyToDelete" },
                        { key: "third" }, { key: "keyToDelete" }];
    const matchToRemove = { key: "keyToDelete" };

    // execution
    const expectedResult = [{ key: "first" }, { key: "third" }];
    const actualResult = Utils.deleteMatch(matches, matchToRemove);

    //after
    expect(actualResult.length).toBe(expectedResult.length);
    expect(compareMatchArrays(expectedResult, actualResult)).toBeTruthy;
});