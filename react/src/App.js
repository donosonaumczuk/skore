import React, { Component } from 'react';
import { Provider } from 'react-redux';
import { Switch, Route } from 'react-router-dom';
import i18next from 'i18next';
import NavBar from './components/navBar/NavBar';
import CreateUserForm from './components/forms/CreateUserForm';
import store from "./redux/store";
import UserProfile from './components/userProfile/UserProfile';
import LogInForm from './components/forms/LogInForm';
import Loader from './components/Loader';
import LogOut from './components/LogOut';
import AuthService from './services/AuthService';
import ErrorPage from './components/ErrorPage';
import Home from './components/home/Home';
import Accounts from './components/Accounts';
import EditUserInfo from './components/EditUserInfo';
import ChangePassword from './components/ChangePassword';
import ConfirmAccount from './components/ConfirmAccount';
import Sports from './components/Sports/Sports';
import CreateMatchForm from './components/forms/CreateMatchForm';
import './css/main.css';
import CreateSportForm from './components/forms/CreateSportForm';
import EditSport from './components/EditSport';


class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            account: {},
            translation: false,
            currentUser: {
                username: null,
                isAdmin: false
            }
        }
    }

    initializeI18next = async () => {
        if (!this.setState.translation) {
            await i18next.init();
            this.setState({
                translation: true
            });
        }
    }

    updateUser = currentUser => {
        if (currentUser) {
            this.setState({
                currentUser: {
                    username: currentUser.username,
                    isAdmin: currentUser.isAdmin ? currentUser.isAdmin : false
                }
            });
        }
        else {
            this.setState({ 
                currentUser: {
                    username: null,
                    isAdmin: false
                }
            });
        }
    }

    async componentDidMount() {
        const currentUser = AuthService.getCurrentUser();
        const isAdmin = AuthService.isAdmin();
        if (currentUser) {
            this.updateUser({ 
                username: currentUser,
                isAdmin: isAdmin
            });
        }
        this.initializeI18next();   
    }

    render() {
        if (!this.state.translation) {
            //TODO test what happens on change language
            return (
                <Loader />
            );
        }
        return (
            <Provider store={store}>
                <div>
                    <NavBar currentUser={this.state.currentUser} />
                    <Switch>
                        <Route exact path="/">
                            <Home currentUser={this.state.currentUser.username} />
                        </Route>
                        <Route exact path="/sports">
                            <Sports />
                        </Route>
                        <Route path="/signUp" component={CreateUserForm} />
                        <Route path="/confirmAccount">
                            <ConfirmAccount />
                        </Route>
                        <Route path="/login">
                            <LogInForm updateUser={this.updateUser} />
                        </Route>
                        <Route path="/logout">
                            <LogOut updateUser={this.updateUser} />
                        </Route>
                        <Route path="/accounts">
                            <Accounts />
                        </Route>
                        <Route path="/createMatch">
                            <CreateMatchForm />
                        </Route>
                        <Route path="/createSport" component={CreateSportForm} />
                        <Route exact path="/sports/:sportName/edit" component={EditSport} />
                        <Route exact path="/users/:username" component={UserProfile} />
                        <Route exact path="/users/:username/edit" component={EditUserInfo} />
                        <Route exact path="/users/:username/changePassword" component={ChangePassword} />
                        <Route path="/" component={ErrorPage} />
                    </Switch>
                </div>
            </Provider>
        );
    }
}

export default App;
