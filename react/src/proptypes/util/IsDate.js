import { isNumber } from "util";
import isTime from "./IsTime";

const isDate = value => value && isNumber(value.year) && isNumber(value.monthValue)
                         && isNumber(value.dayOfMonth);

export default isDate;