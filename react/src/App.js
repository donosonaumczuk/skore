import React, { Component } from 'react';
import { Provider } from 'react-redux';
import { Switch, Route } from 'react-router-dom';
import i18next from 'i18next';
import store from "./redux/store";
import NavBar from './components/NavBar';
import CreateUserForm from './components/forms/CreateUserForm';
import UserProfile from './components/screens/UserProfile';
import LogInForm from './components/forms/LogInForm';
import Loader from './components/Loader';
import LogOut from './components/LogOut';
import AuthService from './services/AuthService';
import ErrorPage from './components/screens/ErrorPage';
import Home from './components/screens/Home';
import Accounts from './components/screens/Accounts';
import EditUserInfo from './components/screens/EditUserInfo';
import ChangePassword from './components/screens/ChangePassword';
import ConfirmAccount from './components/screens/ConfirmAccount';
import Sports from './components/screens/Sports';
import CreateMatchForm from './components/forms/CreateMatchForm';
import CreateSportForm from './components/forms/CreateSportForm';
import EditSport from './components/screens/EditSport';
import Admin from './components/screens/Admin';
import MatchPage from './components/screens/MatchPage';
import ConfirmMatch from './components/screens/ConfirmMatch';
import CancelMatch from './components/screens/CancelMatch';
import CreatedAccount from './components/screens/CreatedAccount';
import SetMatchScoreForm from './components/forms/SetMatchScoreForm';
import AuthenticatedMatch from './components/screens/AuthenticatedMatch';
import './css/main.css';

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
        const { username } = this.state.currentUser;
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
                        <Route exact path="/" render={(props) => <Home {...props} 
                                currentUser={this.state.currentUser.username} />} />
                        <Route exact path="/sports">
                            <Sports />
                        </Route>
                        <Route exact path="/signUp" component={CreateUserForm} />
                        <Route exact path="/createdAccount">
                            <CreatedAccount />
                        </Route>
                        <Route exact path="/confirmAccount" render={(props) => 
                                <ConfirmAccount {...props} updateUser={this.updateUser} />} />
                        <Route exact path="/login">
                            <LogInForm updateUser={this.updateUser} />
                        </Route>
                        <Route exact path="/logout">
                            <LogOut updateUser={this.updateUser} />
                        </Route>
                        <Route exact path="/accounts">
                            <Accounts />
                        </Route>
                        <Route exact path="/admin">
                            <Admin />
                        </Route>
                        <Route exact path="/createMatch" component={CreateMatchForm} />
                        <Route exact path="/match/:matchKey" component={MatchPage} />
                        <Route exact path="/createSport" component={CreateSportForm} />
                        <Route exact path="/sports/:sportName/edit" component={EditSport} />
                        <Route exact path="/users/:username" component={UserProfile} />
                        <Route exact path="/users/:username/edit" component={EditUserInfo} />
                        <Route exact path="/users/:username/changePassword" component={ChangePassword} />
                        <Route exact path="/confirmMatch" component={ConfirmMatch} />
                        <Route exact path="/cancelMatch" component={CancelMatch} />
                        <Route exact path="/authenticatedJoin/:param" 
                            render={(props) =>
                                <AuthenticatedMatch updateUser={this.updateUser}
                                                    baseUrl={"/authenticatedJoin"}
                                                    {...props} needsAuthentication={!username} />
                            } />
                        <Route exact path="/setMatchScore/:creator/:matchKey" component={SetMatchScoreForm} />
                        <Route path="/" component={ErrorPage} />
                    </Switch>
                </div>
            </Provider>
        );
    }
}

export default App;
