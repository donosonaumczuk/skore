import api from './../config/Api';

const getMatches = async (offset, limit) => {
    try {
        const res = await api.get(`matches?offset=${offset}&limit=${limit}`);
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
