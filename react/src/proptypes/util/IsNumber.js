const isNumber = value => {
    if (value && typeof value === "number") {
        return true;
    }
    console.log("fallo en number");

    return false;
}

export default isString;