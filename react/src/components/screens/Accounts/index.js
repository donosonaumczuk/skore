import React, { Component } from 'react';
import UserService from '../../../services/UserService';
import Utils from '../../utils/Utils';
import Accounts from './layout';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 10;

class AccountsContainer extends Component {
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
        const isLoading = this.state.accounts.length === 0 && this.state.hasMore;
        return (
            <Accounts accounts={this.state.accounts} getUsers={this.getUsers}
                        hasMore={this.state.hasMore} isLoading={isLoading}
                        error={this.state.status} />
        )
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

export default AccountsContainer;
