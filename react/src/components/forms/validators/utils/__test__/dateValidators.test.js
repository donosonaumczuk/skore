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

//TODO more tests