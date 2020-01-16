import { isNumber } from "util";

const isTime = value => value && isNumber(value.hour) && isNumber(value.minute);

export default isTime;