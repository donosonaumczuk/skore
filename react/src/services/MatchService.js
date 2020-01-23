import api from './../config/Api';

const getMatches = async (offset, limit) => {
    const res = await api.get(`matches?offset=${offset}&limit=${limit}`);
    // console.log(res)//TODO validate error
    return res.data;
}

const MatchService = {
    getMatches: getMatches
};

export default MatchService;
