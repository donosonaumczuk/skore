const buildUrlFromParamQueries = params => {
    if (!params) {
        return '';
    }
    let paramsUrl = ``;
    let paramsQuantity = 0;
    Object.entries(params).forEach(param => {
        if (param[1] && param[1].length > 0) {
            if (paramsQuantity === 0) {
                paramsUrl = `${paramsUrl}?`;
                paramsQuantity++;
            }
            else {
                paramsUrl = `${paramsUrl}&`
            }
            paramsUrl = `${paramsUrl}${param[0]}=${param[1]}`;
        }
    });
    return paramsUrl;
}

const buildUrlFromParamsWithCommas = params => {
    let url = buildUrlFromParamQueries(params);
    return url = url.replace(/ /g, ",");
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
    buildUrlFromParamsWithCommas,
    createObjectFromFiltersAndPaging
};