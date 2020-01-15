const isString = value => {
    if (value && typeof value === "string") {
        return true;
    }
    return false;
}

export default isString;