import api from './../config/Api';

const getAccountByUsername = async username => {
    const res = await api.get(`test/${username}`);
    console.log(res)//TODO validate error
    return res.data;
}
   


export default getAccountByUsername;