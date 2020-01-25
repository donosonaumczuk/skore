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

const SportService = {
    getSports: getSports
}

export default SportService;