import Utils  from './Utils';
//  hasMorePages,
// buildUrlFromParamQueriesAndTab: buildUrlFromParamQueriesAndTab,
// removeUnknownHomeFilters: removeUnknownHomeFilters

test('buildUrlParam with tab and params', () => {
    //set up
    const params  = { country: "Argentina", state: "" };
    const tabNumber = 1;

    // execution
    const expectedResult = "?country=Argentina&tab=1"
    const actualResult = Utils.buildUrlFromParamQueriesAndTab(params, tabNumber);

    //after
    expect(actualResult).toBe(expectedResult);
    expect(true).toBe(true);
});

test('buildUrlParam without tab and params', () => {
    //set up
    const params  = { country: "Argentina", state: "" };

    // execution
    const expectedResult = "?country=Argentina"
    const actualResult = Utils.buildUrlFromParamQueriesAndTab(params, null);

    //after
    expect(actualResult).toBe(expectedResult);
    expect(true).toBe(true);
});