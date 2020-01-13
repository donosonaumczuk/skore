import api from './../config/Api';

const setToken = token => localStorage.setItem('jwt', token);

const getToken = () => localStorage.getItem('jwt');

const removeToken = () => localStorage.removeItem('jwt');

const loadUser = user => localStorage.setItem('currentUser', user);

const removeUser = localStorage.removeItem('currentUser');

const getUser = () => localStorage.getItem('currentUser');

const logInUser = async user => {
    const response = await api.post("login", user);
    setToken(response.headers['x-token']);
    loadUser(user.username);
    console.log(getToken());
    console.log(getUser());
    //TODO get token, load token, load user add interceptor to post catch error if any
}

const logOutUser = () => {
    if(getToken) {
        removeToken();
        removeUser(); 
        //TODO disable interceptor   
    }
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