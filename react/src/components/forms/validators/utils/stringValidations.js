const hasStringInvalidSymbols = (string, invalidSymbolsRegex) => invalidSymbolsRegex.test(string);
    
const hasStringValidSymbols = (string, invalidSymbolsRegex) => !invalidSymbolsRegex.test(string);

const isStringLengthBetween = (currentString, minLength, maxLength) => {
    return currentString.length >= minLength && currentString.length <= maxLength;
}

const isStringAlpha = string => !/[^a-zA-Z]/.test(string);

const isStringNumeric = string => !/[^0-9]/.test(string);

const isStringAlphaNumeric = string => !/[^0-9a-zA-Z]/.test(string);

const isStringValidEmail = string => /^[a-zA-Z0-9]+@[a-zA-Z0-9]+(\.[A-Za-z]+)+$/.test(string);

export {
    hasStringInvalidSymbols, 
    hasStringValidSymbols,
    isStringLengthBetween,
    isStringAlpha,
    isStringNumeric,
    isStringAlphaNumeric,
    isStringValidEmail
}