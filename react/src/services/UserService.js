import api from './../config/Api';

const getUsers = async (offset, limit) => {
    try {
        const res = await api.get(`users?offset=${offset}&limit=${limit}`);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status }
    }
}

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
   
const getUserMatches = async (username, offset, limit) => {
    try {
        const res = await api.get(`users/${username}/matches?offset=${offset}&limit=${limit}`);
        return res.data;
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
    getUsers: getUsers,
    getProfileByUsername: getProfileByUsername,
    getUserImage: getUserImage,
    getUserMatches: getUserMatches,
    createUser: createUser
};

export default UserService;
