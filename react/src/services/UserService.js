import api from './../config/Api';
import { USERS_ENDPOINT } from './constants/EndpointConstants';
import  { SC_TIME_OUT } from './constants/StatusCodesConstants';

const getUsers = async (offset, limit) => {
    try {
        const res = await api.get(`${USERS_ENDPOINT}?offset=${offset}&limit=${limit}`);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const getUser = async username => {
    try {
        const res = await api.get(`${USERS_ENDPOINT}/${username}`);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const getProfileByUsername = async username => {
    try {
        const res = await api.get(`${USERS_ENDPOINT}/${username}/profile`);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const getUserImage = async username => {
    try {
        const res = await api.get(`${USERS_ENDPOINT}/${username}/image`);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}
   
const getUserMatches = async (username, offset, limit) => {
    try {
        const res = await api.get(`${USERS_ENDPOINT}/${username}/matches?offset=${offset}&limit=${limit}`);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const getUserMatchesWithResults = async (username, offset, limit) => {
    try {
        const res = await api.get(`${USERS_ENDPOINT}/${username}/matches?hasResult=${true}&offset=${offset}&limit=${limit}`);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const createUser = async user => {
    try {
        const res = await api.post(`${USERS_ENDPOINT}`, user);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}

const verifyUser = async code => {
    try {
        const res = await api.post(`${USERS_ENDPOINT}/verification`, code);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
    }
}


const updateUser = async (user, username) => {
    try {
        const res = await api.put(`${USERS_ENDPOINT}/${username}`, user);
        return res.data;
    }
    catch (err) {
        if (err.response) {
            return { status: err.response.status };
        }
        else {
            return { status: SC_TIME_OUT };
        }
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
    verifyUser: verifyUser,
    updateUser: updateUser
};

export default UserService;
