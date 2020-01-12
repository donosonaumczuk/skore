import React, { Component } from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import i18next from 'i18next';
import Accounts from './components/Accounts';
import NavBar from './components/NavBar';
// import getProfileByUsername from './services/UserService';
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
    // let account = await getProfileByUsername('donosonaumczuk');//TODO remove
    // this.setState({ account: account });
  }

  render() {
   
    if(!this.state.translation) {
      //TODO return a spinner os something
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
            <Route path="/user">
              <UserProfile username="donosonaumczuk"/>{/* TODO obtain user from url or click event */} 
            </Route>
          </Switch>
        </Router>
      </div>
      </Provider>
      
    );
  }
}

export default App;
