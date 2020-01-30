import api from './../config/Api';
import { USERS_ENDPOINT } from './constants/EndpointConstants';


const getUsers = async (offset, limit) => {
    try {
        const res = await api.get(`${USERS_ENDPOINT}?offset=${offset}&limit=${limit}`);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status }
    }
}

const getUser = async username => {
    try {
        const res = await api.get(`${USERS_ENDPOINT}/${username}`);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status }
    }
}

const getProfileByUsername = async username => {
    try {
        const res = await api.get(`${USERS_ENDPOINT}/${username}/profile`);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status }
    }
}

const getUserImage = async username => {
    try {
        const res = await api.get(`${USERS_ENDPOINT}/${username}/image`);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status }
    }
}
   
const getUserMatches = async (username, offset, limit) => {
    try {
        const res = await api.get(`${USERS_ENDPOINT}/${username}/matches?offset=${offset}&limit=${limit}`);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status }
    }
}

const getUserMatchesWithResults = async (username, offset, limit) => {
    try {
        const res = await api.get(`${USERS_ENDPOINT}/${username}/matches?hasResult=${true}&offset=${offset}&limit=${limit}`);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status }
    }
}

const createUser = async user => {
    try {
        const res = await api.post(`${USERS_ENDPOINT}`, user);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status };
    }
}

const updateUser = async (user, username) => {
    try {
        const res = await api.put(`${USERS_ENDPOINT}/${username}`, user);
        return res.data;
    }
    catch (err) {
        return { status: err.response.status };
    }
}

const UserService = {
    getUsers: getUsers,
    getUser: getUser,
    getProfileByUsername: getProfileByUsername,
    getUserImage: getUserImage,
    getUserMatches: getUserMatches,
    getUserMatchesWithResults: getUserMatchesWithResults,
    createUser: createUser,
    updateUser: updateUser
};

export default UserService;
