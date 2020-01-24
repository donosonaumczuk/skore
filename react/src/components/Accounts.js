import React, { Component } from 'react';
import i18next from 'i18next';
import InfiniteScroll from 'react-infinite-scroll-component';
import UserService from '../services/UserService';
import Loader from './Loader';
import Account from './Account';
import Utils from './utils/Utils';
import ErrorPage from './ErrorPage';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 7;

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

    updateUsers = response => {
        if (response.status) {
            this.setState({ status: response.status });
        }
        else {
            const hasMore = Utils.hasMorePages(response.links);
            const accounts = response.users;
            this.setState({
                accounts: [...this.state.accounts, ...accounts],
                offset: this.state.offset + accounts.length,
                hasMore: hasMore
            });
        }
    }

    getUsers = async () => {
        const { offset, limit } = this.state;
        const response = await UserService.getUsers(offset, limit);
        if (this.mounted) {
            this.updateUsers(response);
        }
    }

    async componentDidMount() {
        this.mounted = true;
        this.getUsers();        
    }

    render() {
        if (this.state.status) {
            return <ErrorPage status={this.state.status} />;
        }
        else if (this.state.accounts.length === 0 && this.state.hasMore) {
            return <Loader />;
        }
        return (
            <div>
                <center>
                    <h1>
                        {i18next.t('createUserForm.accounts')}
                    </h1>
                </center>
                <div className="container">
                    <InfiniteScroll dataLength={this.state.accounts.length} next={this.getUsers}
                                    hasMore={this.state.hasMore} loader={<Loader />} >
                        {this.state.accounts.map((account, i) => <Account key={i} account={account} />)}
                    </InfiniteScroll> 
                </div>
            </div>
        )
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

export default Accounts;
