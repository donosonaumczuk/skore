import React, { Component } from 'react';
import i18next from 'i18next';
import InfiniteScroll from 'react-infinite-scroll-component';
import UserService from '../services/UserService';
import Loader from './Loader';
import Account from './Account';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 10;

//TODO replace with real accounts when endpoint created
class Accounts extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            accounts: [],
            offset: INITIAL_OFFSET,
            limit: QUERY_QUANTITY,
            hasMore: true
        }
    }
    
    createArray = (account, offset, limit) => {
        let i;
        let index = 0;
        let accounts = [];
        for(i = offset; i < limit; i++) {
            accounts[index] = {
                "username": account.username,
                "index": i
            };
            index++;
        }
        return accounts;
    }

    async componentDidMount() {
        this.mounted = true;
        const account = await UserService.getProfileByUsername("donosonaumczuk");
        const newAccounts = this.createArray(account, this.state.offset, this.state.limit);
        this.setState({
            accounts: [...this.state.accounts, ...newAccounts],
            offset: this.state.offset + this.state.limit,
            hasMore: true
        });
    }

    getMoreAccounts = async () => {
        //TODO replace with real endpoint when endpoint created
        const account = await UserService.getProfileByUsername("donosonaumczuk");
        const newAccounts = this.createArray(account, this.state.offset, this.state.limit + this.state.offset);
        this.setState({
            accounts: [...this.state.accounts, ...newAccounts],
            offset: this.state.offset + this.state.limit,
            hasMore: true
        });
    }

    render() {
        if (this.state.accounts.length === 0) {
            return <Loader />
        }
        return (
            <div>
                <center>
                    <h1>
                        {i18next.t('createUserForm.accounts')}
                    </h1>
                </center>
                <InfiniteScroll dataLength={this.state.accounts.length} next={this.getMoreAccounts}
                                hasMore={this.state.hasMore} loader={<Loader />} >
                    {this.state.accounts.map((account, i) => <Account key={i} account={account} />)}
                </InfiniteScroll> 
            </div>
        )
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

export default Accounts;
