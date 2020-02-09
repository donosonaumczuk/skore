const MIN_THREE_DIGIT_NUMBER = 100;
const ZERO = 0;
const MIN_TWO_DIGIT_NUMBER = 10;

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

const getStringWithTwoDigits = number => {
    if (number >= MIN_THREE_DIGIT_NUMBER) {
        return `${number % MIN_THREE_DIGIT_NUMBER}`;
    }
    else if (number <= ZERO) {
        return "00";
    }
    else if (number < MIN_TWO_DIGIT_NUMBER) {
        return `0${number}`;
    }
    else {
        return `${number}`;
    }
}

const getDateWithParamFormat = date => {
    const year = date.getFullYear()
    const month = getStringWithTwoDigits(date.getMonth() + 1);
    const day = getStringWithTwoDigits(date.getDate());
    const hours = getStringWithTwoDigits(date.getHours());
    const minutes = getStringWithTwoDigits(date.getMinutes());
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}

const addCreatedByToParams = (params, username) => {
    return {
        ...params,
        "createdBy": `${username}`
    };
}

const addWithPlayersToParams = (params, username) => {
    return {
        ...params,
        "withPlayers": `${username}`
    }
}

const addWithoutPlayersToParams = (params, username) => {
    return {
        ...params,
        "withoutPlayers": `${username}`
    }
}

const addFutureMinTimeToParams = params => {
    const minStartTime = getDateWithParamFormat(new Date());
    return {
        ...params,
        "minStartTime": minStartTime
    };
}

const addMinFreePlacesToParams = params => {
    return { 
        ...params,
        "minFreePlaces": "1"
    };
}

const createObjectFromFiltersAndPaging = (offset, limit, filters) => {
    let paramObject = {
        "offset": `${offset}`,
        "limit": `${limit}`,
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
    createObjectFromFiltersAndPaging,
    getDateWithParamFormat,
    addCreatedByToParams,
    addWithPlayersToParams,
    addWithoutPlayersToParams,
    addFutureMinTimeToParams,
    addMinFreePlacesToParams,
};