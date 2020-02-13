import api from './../config/Api';
import { SPORTS_ENDPOINT } from './constants/EndpointConstants';
import { buildUrlFromParamsWithCommas, createObjectFromFiltersAndPaging } from './Util';
import  { SC_TIME_OUT } from './constants/StatusCodesConstants';

const getSports = async (offset, limit) => {
    let paramObject = createObjectFromFiltersAndPaging(offset, limit, {});
    const paramsUrl =  buildUrlFromParamsWithCommas(paramObject);
    try {
        const res = await api.get(`${SPORTS_ENDPOINT}${paramsUrl}`);
        console.log(res);
        return res.data;
    }
    catch(err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const getSportByName = async sportName => {
    try {
        const res = await api.get(`${SPORTS_ENDPOINT}/${sportName}`);
        return res.data;
    }
    catch(err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const createSport = async sport => {
    try {
        const res = await api.post(`${SPORTS_ENDPOINT}`, sport);
        return res.data;
    }
    catch(err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const updateSport = async sport => {
    try {
        const res = await api.put(`${SPORTS_ENDPOINT}/${sport.sportName}`, sport);
        return res.data;
    }
    catch(err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const SportService = {
    getSports: getSports,
    createSport: createSport,
    getSportByName: getSportByName,
    updateSport: updateSport
}

export default SportService;