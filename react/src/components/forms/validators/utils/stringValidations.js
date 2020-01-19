const hasStringInvalidSymbols = (string, invalidSymbolsRegex) => invalidSymbolsRegex.test(string);
    
const hasStringValidSymbols = (string, invalidSymbolsRegex) => !invalidSymbolsRegex.test(string);

const isStringLengthBetween = (currentString, minLength, maxLength) => {
    return currentString.length >= minLength && currentString.length <= maxLength;
}

//TODO consider if it is okay with letters with tildes with enie
const isStringAlpha = string => !/[^a-zA-ZáéíóúñÁÉÍÓÚÑ]/.test(string);

//TODO consider if it is okay with letters with tildes with enie
const isStringAlphaOrSpaces = string => !/[^a-zA-ZáéíóúñÁÉÍÓÚÑ ]/.test(string)

const isStringNumeric = string => !/[^0-9]/.test(string);

const isStringAlphaNumeric = string => !/[^0-9a-zA-ZáéíóúñÁÉÍÓÚÑ]/.test(string);

//TODO consider if adding tildes 
const isStringValidEmail = string => /^[a-zA-ZñÑ0-9]+@[a-zA-Z0-9]+(\.[A-Za-z]+)+$/.test(string);

export {
    hasStringInvalidSymbols, 
    hasStringValidSymbols,
    isStringLengthBetween,
    isStringAlpha,
    isStringAlphaOrSpaces,
    isStringNumeric,
    isStringAlphaNumeric,
    isStringValidEmail
}