import api from './../config/Api';
import i18next from 'i18next';
import { USERS_ENDPOINT } from './constants/EndpointConstants';
import  { SC_TIME_OUT } from './constants/StatusCodesConstants';
import AuthService from './AuthService';
import { buildUrlFromParamsWithCommas, createObjectFromFiltersAndPaging } from './Util';

const getUsers = async (offset, limit, filters, token) => {
    const config = { cancelToken: token };
    let paramObject = createObjectFromFiltersAndPaging(offset, limit, filters);
    const paramsUrl =  buildUrlFromParamsWithCommas(paramObject);
    try {
        const res = await api.get(`${USERS_ENDPOINT}${paramsUrl}`, config);
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
        const language = i18next.language;
        let config = {
            headers: {
                "Accept-Language": language
            }
        };
        const res = await api.post(`${USERS_ENDPOINT}`, user, config);
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

const verifyUser = async (username, code) => {
    try {
        const res = await api.post(`${USERS_ENDPOINT}/${username}/verification`, code);
        const token = res.headers['x-token']; 
        const userId = res.data.userId;
        AuthService.autoLogin(token, username, userId);
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

const getLikedUsers = async (username, offset, limit) => {
    let paramObject = createObjectFromFiltersAndPaging(offset, limit, null);
    const paramsUrl =  buildUrlFromParamsWithCommas(paramObject);
    try {
        const res = await api.get(`${USERS_ENDPOINT}/${username}/likedUsers${paramsUrl}`);
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

const likeUser = async (username, likedUser) => {
    const userLike = { username: likedUser };
    try {
        const res = await api.post(`${USERS_ENDPOINT}/${username}/likedUsers`, userLike);
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

const dislikeUser = async (username, likedUser) => {
    try {
        const res = await api.delete(`${USERS_ENDPOINT}/${username}/likedUsers/${likedUser}`);
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
    updateUser: updateUser,
    getLikedUsers: getLikedUsers,
    likeUser: likeUser,
    dislikeUser: dislikeUser
};

export default UserService;
