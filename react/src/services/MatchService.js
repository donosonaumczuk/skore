import api from './../config/Api';
   
const getMatches = async () => {
    //TODO replace endpoint with /matches when endpoint is created
    const res = await api.get(`users/donosonaumczuk/matches`);
    console.log(res)//TODO validate error
    return res.data.matches;
}

const MatchService = {
    getMatches: getMatches
};

export default MatchService;
