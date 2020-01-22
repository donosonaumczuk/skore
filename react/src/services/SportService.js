import api from './../config/Api';

const getSports = async () => {
    try {
        const res = await api.get("/sports");
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