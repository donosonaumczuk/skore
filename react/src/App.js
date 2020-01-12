import React, { Component } from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter as Router, Switch, Route} from 'react-router-dom';
import i18next from 'i18next';
import Accounts from './components/Accounts';
import NavBar from './components/NavBar';
// import UserService from './services/UserService'; TODO add when /users endpoint created
import CreateUserForm from './components/forms/CreateUserForm';
import store from "./redux/store";
import './css/main.css';
import UserProfile from './components/userProfile/UserProfile';



class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      account: {},
      translation: false
    }
  }


  initializeI18next = async () => {
    if(!this.setState.translation) {
      await i18next.init();
      this.setState({
        translation: true
      });
    }
  }

  async componentDidMount() {
    this.initializeI18next();   
    // let account = await UserService.getProfileByUsername('donosonaumczuk');//TODO add when /users enadpoint created
    // this.setState({ account: account }); TODO add when /users endpoint created
  }

  render() {
   
    if(!this.state.translation) {
      //TODO return a spinner os something and test what happens on change language
      return (<React.Fragment></React.Fragment>);
    }
    return (
      <Provider store={store}>
      <div>
        <NavBar />
        <Router>
          <Switch>
            <Route exact path="/">
              <Accounts account={this.state.account} />
            </Route>
            <Route path="/createUser">
              <CreateUserForm />
            </Route>
            <Route path="/user/:username" component={UserProfile} />
          </Switch>
        </Router>
      </div>
      </Provider>
      
    );
  }
}

export default App;
