import React, {Component} from 'react';
import Accounts from './components/Accounts';
import axios from 'axios';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      account: {}
    }
  }

  componentDidMount() {
    axios.get('/api/test/donosonaumczuk')
    .then(res => {
      const account = res.data;
      this.setState({ account: account });
    })
    .catch(console.log)
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
