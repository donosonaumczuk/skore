import { isNumber } from "util";

const isDate = value => {
    if (value && isNumber(value.year) && isNumber(value.monthValue) &&
        isNumber(value.dayOfMonth)) {
        return true;
    }
    return false;
}

export default isDate;