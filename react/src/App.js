import React, { Component } from 'react';
import Accounts from './components/Accounts';
import NavBar from './components/NavBar';
import getAccountByUsername from './services/AccountService';
import './css/test.css';

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
      <div>
        <NavBar />
        <Accounts account={this.state.account} />
      </div>
    );
  }
}

export default App;
