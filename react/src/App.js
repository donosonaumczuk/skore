import React, {Component} from 'react';
import Accounts from './components/Accounts';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      account: {}
    }
  }

  componentDidMount() {
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
