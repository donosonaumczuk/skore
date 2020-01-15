const isNumber = value => {
    if (value && typeof value === "number") {
        return true;
    }

    return false;
}

export default isNumber;