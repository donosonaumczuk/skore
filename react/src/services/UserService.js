import api from './../config/Api';

const getProfileByUsername = async username => {
    const res = await api.get(`user/${username}/profile`);
    console.log(res)//TODO validate error
    return res.data;
}
   
export default getProfileByUsername;
