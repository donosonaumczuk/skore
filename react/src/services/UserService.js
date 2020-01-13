import api from './../config/Api';

const getProfileByUsername = async username => {
    const res = await api.get(`users/${username}/profile`);
    // console.log(res)//TODO validate error
    return res.data;
}

const getUserImage = async username => {
    const res = await api.get(`users/${username}/image`);
    // console.log(res)//TODO validate error
    return res.data;
}
   
const getUserMatches = async username => {
    const res = await api.get(`users/${username}/matches`);
    console.log(res)//TODO validate error
    return res.data;
}
const UserService = {
    getProfileByUsername: getProfileByUsername,
    getUserImage: getUserImage
};

export default UserService;
