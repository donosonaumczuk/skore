import React, { Component } from 'react';
import { Provider } from 'react-redux';
import { Switch, Route} from 'react-router-dom';
import i18next from 'i18next';
import NavBar from './components/navBar/NavBar';
// import UserService from './services/UserService'; TODO import when used on componentDidMount
import CreateUserForm from './components/forms/CreateUserForm';
import store from "./redux/store";
import './css/main.css';
import UserProfile from './components/userProfile/UserProfile';
import LogInForm from './components/forms/LogInForm';
import Loader from './components/Loader';
import LogOut from './components/LogOut';
import AuthService from './services/AuthService';
import ErrorPage from './components/ErrorPage';
import Home from './components/home/Home';
import EditUserInfo from './components/EditUserInfo';
import ChangePassword from './components/ChangePassword';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      account: {},
      translation: false,
      currentUser: null
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
    this.setState({
      currentUser: currentUser
    });
  }

  async componentDidMount() {
    const currentUser = AuthService.getCurrentUser();
    if (currentUser) {
      this.updateUser(currentUser);
    }
    this.initializeI18next();   
    // let account = await UserService.getProfileByUsername('donosonaumczuk');//TODO add when /users enadpoint created
    // this.setState({ account: account }); TODO add when /users endpoint created
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
        <NavBar currentUser={this.state.currentUser}/>
          <Switch>
            <Route exact path="/">
              <Home />
            </Route>
            <Route path="/signUp">
              <CreateUserForm />
            </Route>
            <Route path="/login">
              <LogInForm updateUser={this.updateUser}/>
            </Route>
            <Route path="/logout">
              <LogOut updateUser={this.updateUser}/>
            </Route>
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
