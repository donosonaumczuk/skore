const daysInMonths = [31,28,31,30,31,30,31,31,30,31,30,31];

const isLeapYear = year => {
    if (!year || year % 4 !== 0) {
        return false;
    }
    if (year % 100 === 0 && year % 400 !== 0) {
        return false;
    }
    return true;
}

const isDayValid = (day, month, year) => {
    let monthMaxDays = daysInMonths[1];
    if (month !== 2 && month <=12 && month > 0) {
        monthMaxDays = daysInMonths[month - 1];
        return day <= monthMaxDays && day > 0;
    }
    else {
        if (isLeapYear(year)) {
            monthMaxDays = 29;
        }
        return day <= monthMaxDays && day > 0;
    }
}

const isValidDate = (day, month, year) => {
    if (month <= 0 || month > 12 || day <= 0 || day > 31 || year <= 0) {
        return false;
    }
    return isDayValid(day, month, year);
}

const isDateBeforeCurrentDate = (day, month, year) => {
    const currentDate = new Date();
    var currentDay = currentDate.getDate();
    var currentMonth = currentDate.getMonth() + 1;
    var currentYear = currentDate.getFullYear();
    if (year < currentYear) {
        return true;
    }
    else if (year === currentYear ) {
        if (month < currentMonth) {
            return true;
        }
        else if (month === currentMonth) {
            return day < currentDay;
        }
    }
    return false;
}

const isDateAfterCurrentDate = (day, month, year) => {
    const currentDate = new Date();
    var currentDay = currentDate.getDate();
    var currentMonth = currentDate.getMonth() + 1;
    var currentYear = currentDate.getFullYear();
    if (year > currentYear) {
        return true;
    }
    else if (year === currentYear ) {
        if (month > currentMonth) {
            return true;
        }
        else if (month === currentMonth) {
            return day >= currentDay;
        }
    }
    return false;
}

const isValidPastDate = (day, month, year) => {
    if (!isValidDate(day, month, year)) {
        return false;
    }
    return isDateBeforeCurrentDate(day, month, year);
}

const isValidFutureDate = (day, month, year) => {
    if (!isValidDate(day, month, year)) {
        return false;
    }
    return isDateAfterCurrentDate(day, month, year);
}

export {
    isValidDate,
    isValidPastDate,
    isValidFutureDate,
    isLeapYear
}
