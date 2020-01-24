import { isStringNumeric } from "./StringValidators";

const isNumberBetweenValues = (number, minValue, maxValue) => {
    return number >= minValue && number <= maxValue;
}

const isStringBetweenValues = (numberString, minValue, maxValue) => {
    if (!isStringNumeric(numberString)) {
        return false;
    }
    let number = parseInt(numberString);
    return isNumberBetweenValues(number, minValue, maxValue);
}

export {
    isNumberBetweenValues,
    isStringBetweenValues
};