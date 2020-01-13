import api from './../config/Api';

const setToken = token => localStorage.setItem('jwt', token);

const getToken = () => localStorage.getItem('jwt');

const removeToken = () => localStorage.removeItem('jwt');

const loadUser = user => localStorage.setItem('currentUser', user);

const removeUser = () => localStorage.removeItem('currentUser');

const getUser = () => localStorage.getItem('currentUser');

const logInUser = async user => {
    const response = await api.post("login", user);
    setToken(response.headers['x-token']);
    loadUser(user.username);

    //TODO if error of token expiry remove token and user and catch other errors
    //TODO token expiry should be controlled on every get or post that uses authentication
}

const logOutUser = async () => {
    const response = await api.post("logout");
    if(getToken) {
        removeToken();
        removeUser();  
    }
    console.log(response);
    //TODO handle errors using response
}

const getCurrentUser = () => {
    if(getToken()) {
        return getUser();
    }
    return null;
}

const AuthService = {
    getToken: getToken,
    logInUser: logInUser,
    logOutUser: logOutUser,
    getCurrentUser: getCurrentUser
};

export default AuthService;