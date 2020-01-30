const buildUrlFromParamQueries = params => {
    if (!params) {
        return '';
    }
    let paramsUrl = ``;
    let paramsQuantity = 0;
    Object.entries(params).forEach(param => {
        if (paramsQuantity === 0) {
            paramsUrl = `${paramsUrl}?`;
            paramsQuantity++;
        }
        else {
            paramsUrl = `${paramsUrl}&`
        }
        paramsUrl = `${paramsUrl}${param[0]}=${param[1]}`;
    });
    return paramsUrl;
}

const createObjectFromFiltersAndPaging = (offset, limit, filters) => {
    let paramObject = {
        "offset": offset,
        "limit": limit,
    }
    if (filters) {
        paramObject = {
            ...paramObject,
            ...filters
        };
    }
    return paramObject;
}

export {
    buildUrlFromParamQueries,
    createObjectFromFiltersAndPaging
};