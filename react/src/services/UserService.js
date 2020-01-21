import api from './../config/Api';

const getProfileByUsername = async username => {
    try {
        const res = await api.get(`users/${username}/profile`);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status }
    }

}

const getUserImage = async username => {
    try {
        const res = await api.get(`users/${username}/image`);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status }
    }
}
   
const getUserMatches = async username => {
    try {
        const res = await api.get(`users/${username}/matches`);
        return res.data.matches;
    }
    catch (err) {
        return { status: err.response.status }
    }
}

const createUser = async user => {
    try {
        const res = await api.post("users", user);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status };
    }
}

const UserService = {
    getProfileByUsername: getProfileByUsername,
    getUserImage: getUserImage,
    getUserMatches: getUserMatches,
    createUser: createUser
};

export default UserService;
