import i18next from 'i18next';
import CreateSportValidator from '../CreateSportValidator';

const ERROR_BASE_LABEL = "createSportForm.errors.";

//hasSportNameValidSymbols
test('hasSportNameValidSymbols with valid value', () => {
    //set up
    const sportName = "sportName";
    const expectedResult = "";

    //execution
    const actualResult = CreateSportValidator.validateSportName(sportName);

    //postcondition
    expect(actualResult).toBe(expectedResult);
});

test('hasSportNameValidSymbols without value', () => {
    //set up
    const sportName = null;
    const expectedResult = i18next.t(`${ERROR_BASE_LABEL}sportName.required`);

    //execution
    const actualResult = CreateSportValidator.validateSportName(sportName);

    //postcondition
    expect(actualResult).toBe(expectedResult);
});