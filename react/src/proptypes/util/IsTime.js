import { isNumber } from "util";

const isTime = value => {
    if (value && isNumber(value.hour) && isNumber(value.minute)) {
        return true;
    }
    return false;
}

export default isTime;