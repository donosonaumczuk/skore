import { isNumber } from "util";
import isTime from "./IsTime";

const isDate = value => {
    if (value && isNumber(value.year) && isNumber(value.monthValue) &&
        isNumber(value.dayOfMonth)) {
        return true;
    }
    return false;
}

export default isDate;