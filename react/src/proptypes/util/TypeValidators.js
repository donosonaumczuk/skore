const isBoolean = value => value && typeof value === "boolean";

const isNumber = value => value && typeof value === "number";

const isString = value => value && typeof value === "string";

const isDate = value => value && isNumber(value.year) && isNumber(value.monthValue)
                         && isNumber(value.dayOfMonth);

const isTime = value => value && isNumber(value.hour) && isNumber(value.minute);

export {
    isBoolean,
    isNumber,
    isString,
    isDate,
    isTime
}

