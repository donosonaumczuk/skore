import api from './../config/Api';
import { MATCHES_ENDPOINT } from './constants/EndpointConstants';

const getMatches = async (offset, limit) => {
    try {
        const res = await api.get(`${MATCHES_ENDPOINT}?offset=${offset}&limit=${limit}`);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status }
    }
}

const MatchService = {
    getMatches: getMatches
};

export default MatchService;
