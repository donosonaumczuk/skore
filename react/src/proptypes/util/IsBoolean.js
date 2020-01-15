const isBoolean = value => {
    if (value && typeof value === "boolean") {
        return true;
    }
    return false;
}

export default isBoolean;