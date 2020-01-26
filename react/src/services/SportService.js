import api from './../config/Api';
import { SPORTS_ENDPOINT } from './constants/EndpointConstants';


const getSports = async () => {
    try {
        const res = await api.get(`${SPORTS_ENDPOINT}`);
        return res.data.sports;
    }
    catch(err) {
        return { status: err.ressponse.status}
    }
}

const createSport = async sport => {
    try {
        const res = await api.post(`${SPORTS_ENDPOINT}`, sport);
        return res.data.sports;
    }
    catch(err) {
        return { status: err.response.status}
    }
}

const SportService = {
    getSports: getSports,
    createSport: createSport
}

export default SportService;