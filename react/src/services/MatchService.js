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

const createMatch = async match => {
    try {
        const res = await api.post(`${MATCHES_ENDPOINT}`, match);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status }
    }
}

const MatchService = {
    getMatches: getMatches,
    getMatchesCreatedBy: getMatchesCreatedBy,
    getMatchesJoinedBy: getMatchesJoinedBy,
    getMatchesToJoin: getMatchesToJoin,
    getMatchByKey: getMatchByKey,
    createMatch: createMatch
};

export default MatchService;
