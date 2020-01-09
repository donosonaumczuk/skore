import React, { Component } from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import i18next from 'i18next';
import Accounts from './components/Accounts';
import NavBar from './components/NavBar';
import getAccountByUsername from './services/AccountService';
import CreateUserForm from './components/forms/CreateUserForm';
import store from "./redux/store";
import showResults from "./ShowResults";
import './css/main.css';



class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      account: {}
    }
  }

  async componentDidMount() {   
    let account = await getAccountByUsername('donosonaumczuk')
    this.setState({ account: account });
  }

  render() {

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
              <CreateUserForm onSubmit={showResults}/>
            </Route>
          </Switch>
        </Router>
      </div>
      </Provider>
      
    );
  }
}

export default App;
