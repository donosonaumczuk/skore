import api from './../config/Api';
import { MATCHES_ENDPOINT } from './constants/EndpointConstants';
import { buildUrlFromParamQueries, createObjectFromFiltersAndPaging } from './Util';
import { SC_TIME_OUT } from './constants/StatusCodesConstants';

const getMatches = async (offset, limit, filters) => {
    const paramObject = createObjectFromFiltersAndPaging(offset, limit, filters);
    const paramsUrl =  buildUrlFromParamQueries(paramObject);
    try {
        const res = await api.get(`${MATCHES_ENDPOINT}${paramsUrl}`);
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

const getMatchesCreatedBy =  async (username, offset, limit, filters) => {
    const paramObject = createObjectFromFiltersAndPaging(offset, limit, filters);
    let paramsUrl =  buildUrlFromParamQueries(paramObject);
    paramsUrl = paramsUrl.length > 0 ? `${paramsUrl}&createdBy=${username}`:
                                        `?createdBy=${username}`;
    try {
        const res = await api.get(`${MATCHES_ENDPOINT}${paramsUrl}`);
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

const getMatchesJoinedBy =  async (username, offset, limit, filters) => {
    const paramObject = createObjectFromFiltersAndPaging(offset, limit, filters);
    let paramsUrl =  buildUrlFromParamQueries(paramObject);
    paramsUrl = paramsUrl.length > 0 ? `${paramsUrl}&withPlayers=${username}`:
                                        `?withPlayers=${username}`;
    try {
        const res = await api.get(`${MATCHES_ENDPOINT}${paramsUrl}`);
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

const getMatchesToJoin =  async (username, offset, limit, filters) => {
    const paramObject = createObjectFromFiltersAndPaging(offset, limit, filters);
    let paramsUrl =  buildUrlFromParamQueries(paramObject);
    paramsUrl = paramsUrl.length > 0 ? `${paramsUrl}&withoutPlayers=${username}` :
                                        `?withoutPlayers=${username}`;
    try {
        const res = await api.get(`${MATCHES_ENDPOINT}${paramsUrl}`);
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
        console.log("in service matchKey: ", matchKey);
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
        const res = await api.post(`${MATCHES_ENDPOINT}/${matchKey}/players/requestToJoin`, user);
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
    console.log("entro");

    try {
        const config = {
            headers: {
                "x-code": code,
            }
        };
        const res = await api.delete(`${MATCHES_ENDPOINT}/${matchKey}/players/${userId}`, config);
        console.log(res);
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
};

export default MatchService;
