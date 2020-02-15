import api from './../config/Api';
import i18next from 'i18next';
import { MATCHES_ENDPOINT } from './constants/EndpointConstants';
import { buildUrlFromParamsWithCommas, createObjectFromFiltersAndPaging,
            addFutureMinTimeToParams, addMinFreePlacesToParams,
            addCreatedByToParams, addWithoutPlayersToParams,
            addWithPlayersToParams } from './Util';
import { SC_TIME_OUT, SC_CLIENT_CLOSED_REQUEST } from './constants/StatusCodesConstants';

const getMatches = async (offset, limit, filters, token) => {
    const config = { cancelToken: token };
    let paramObject = createObjectFromFiltersAndPaging(offset, limit, filters);
    paramObject = addFutureMinTimeToParams(paramObject);
    paramObject = addMinFreePlacesToParams(paramObject);
    const paramsUrl =  buildUrlFromParamsWithCommas(paramObject);
    try {
        const res = await api.get(`${MATCHES_ENDPOINT}${paramsUrl}`, config);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };  
        }
        else {
            if (err.isAxiosError) {
                return { status: SC_TIME_OUT };
            }
            return { status: SC_CLIENT_CLOSED_REQUEST };
        }
    }
}

const getMatchesCreatedBy =  async (username, offset, limit, filters, token) => {
    const config = { cancelToken: token };
    let paramObject = createObjectFromFiltersAndPaging(offset, limit, filters);
    paramObject = addCreatedByToParams(paramObject, username);
    const paramsUrl =  buildUrlFromParamsWithCommas(paramObject);
    try {
        const res = await api.get(`${MATCHES_ENDPOINT}${paramsUrl}`, config);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
                
        }
        else {
            if (err.isAxiosError) {
                return { status: SC_TIME_OUT };
            }
            return { status: SC_CLIENT_CLOSED_REQUEST };
        }
    }
}

const getMatchesJoinedBy =  async (username, offset, limit, filters, token) => {
    const config = { cancelToken: token };
    let paramObject = createObjectFromFiltersAndPaging(offset, limit, filters);
    paramObject = addFutureMinTimeToParams(paramObject);
    paramObject = addWithPlayersToParams(paramObject, username);
    const paramsUrl =  buildUrlFromParamsWithCommas(paramObject);
    try {
        const res = await api.get(`${MATCHES_ENDPOINT}${paramsUrl}`, config);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
                
        }
        else {
            if (err.isAxiosError) {
                return { status: SC_TIME_OUT };
            }
            return { status: SC_CLIENT_CLOSED_REQUEST };
        }
    }
}

const getMatchesToJoin =  async (username, offset, limit, filters, token) => {
    const config = { cancelToken: token };
    let paramObject = createObjectFromFiltersAndPaging(offset, limit, filters);
    paramObject = addFutureMinTimeToParams(paramObject);
    paramObject = addMinFreePlacesToParams(paramObject);
    paramObject = addWithoutPlayersToParams(paramObject, username);
    const paramsUrl =  buildUrlFromParamsWithCommas(paramObject);
    try {
        const res = await api.get(`${MATCHES_ENDPOINT}${paramsUrl}`, config);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
                
        }
        else {
            if (err.isAxiosError) {
                return { status: SC_TIME_OUT };
            }
            return { status: SC_CLIENT_CLOSED_REQUEST };
        }
    }
}

const getMatchByKey = async matchKey => {
    try {
        const res = await api.get(`${MATCHES_ENDPOINT}/${matchKey}`);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const createMatch = async match => {
    try {
        const res = await api.post(`${MATCHES_ENDPOINT}`, match);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const joinMatchWithAccount = async (matchKey, userId) => {
    try {
        const user = { "userId": userId };
        const res = await api.post(`${MATCHES_ENDPOINT}/${matchKey}/players`, user);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const joinMatchAnonymous = async (matchKey, user) => {
    try {
        const language = i18next.language;
        let config = {
            headers: {
                "Accept-Language": language
            }
        };
        const res = await api.post(`${MATCHES_ENDPOINT}/${matchKey}/players/requestToJoin`, user, config);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const confirmAssistance = async (matchKey, userId, code) => {
    try {
        const user = { "userId": userId };
        const config = {
            headers: {
                "x-code": code,
            }
        };
        const res = await api.post(`${MATCHES_ENDPOINT}/${matchKey}/players/`,
                                    user, config);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const cancelMatchWithAccount = async (matchKey, userId) => {
    try {
        const res = await api.delete(`${MATCHES_ENDPOINT}/${matchKey}/players/${userId}`);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const cancelAssistance = async (matchKey, userId, code) => {
    try {
        const language = i18next.language;
        const config = {
            headers: {
                "x-code": code,
                "Accept-Language": language
            }
        };
        const res = await api.delete(`${MATCHES_ENDPOINT}/${matchKey}/players/${userId}`, config);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const deleteMatch = async matchKey => {
    try {
        const res = await api.delete(`${MATCHES_ENDPOINT}/${matchKey}`);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const setScore = async (matchKey, score) => {
    try {
        const res = await api.post(`${MATCHES_ENDPOINT}/${matchKey}/result`, score);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const MatchService = {
    getMatches: getMatches,
    getMatchesCreatedBy: getMatchesCreatedBy,
    getMatchesJoinedBy: getMatchesJoinedBy,
    getMatchesToJoin: getMatchesToJoin,
    getMatchByKey: getMatchByKey,
    createMatch: createMatch,
    joinMatchWithAccount: joinMatchWithAccount,
    joinMatchAnonymous: joinMatchAnonymous,
    confirmAssistance: confirmAssistance,
    cancelMatchWithAccount: cancelMatchWithAccount,
    cancelAssistance: cancelAssistance,
    deleteMatch: deleteMatch,
    setScore: setScore
};

export default MatchService;
