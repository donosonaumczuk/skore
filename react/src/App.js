import React, {Component} from 'react';
import './App.css';
import Accounts from './components/Accounts';

class App extends Component {

  state = {
    account: {}
  }

  componentDidMount() {
    console.log("executing");

    console.log(this.state);
    // fetch('http://localhost:8080/api/test/donosonaumczuk')
    fetch('/api/test/donosonaumczuk')
    .then(res => res.json())
    .then((data) => {
      this.setState({ account: data })
    })
    .catch(console.log)
    console.log(this.state);
  }

  render() {
    return (
      <div>
        <Accounts account={this.state.account} />
      </div>
    );
  }
}

export default App;
