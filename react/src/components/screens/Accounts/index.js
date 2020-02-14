import React, { Component } from 'react';
import Axios from 'axios';
import queryString from 'query-string';
import PropTypes from 'prop-types';
import UserService from '../../../services/UserService';
import Utils from '../../utils/Utils';
import Accounts from './layout';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 10;

class AccountsContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const queryParams = queryString.parse(props.location.search);
        this.state = {
            accounts: [],
            offset: INITIAL_OFFSET,
            limit: QUERY_QUANTITY,
            hasMore: true,
            filters: queryParams
        }
    }

    getSourceToken = () => {
        const newSource = Axios.CancelToken.source();
        if (this.mounted) {
            this.setState({ currentSource: newSource });
        }
        return newSource.token;
    }

    cancelRequestIfPending = () => {
        if (this.state.currentSource) {
            this.state.currentSource.cancel();
            if (this.mounted) {
                this.setState({ currentSource: null });
            }
        }
    }

    onSubmit = values => {
        const newUrl = Utils.buildUrlFromParamQueriesAndTab(values, null);
        if (this.mounted) {
            this.setState({
                accounts: [],
                offset: INITIAL_OFFSET,
                hasMore: true,
                filters: values
            }, () => { this.getUsers(); }); 
            this.props.history.push(`/accounts${newUrl}`);
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
        const { offset, limit, filters } = this.state;
        this.cancelRequestIfPending();
        const token = this.getSourceToken();
        const response = await UserService.getUsers(offset, limit, filters, token);
        if (this.mounted) {
            this.updateUsers(response);
        }
        //TODO validate error
    }

    componentDidMount = async () => {
        this.mounted = true;
        this.getUsers();        
    }

    render() {
        const isLoading = this.state.accounts.length === 0 && this.state.hasMore;
        return (
            <Accounts accounts={this.state.accounts} getUsers={this.getUsers}
                        hasMore={this.state.hasMore} isLoading={isLoading}
                        error={this.state.status} onSubmit={this.onSubmit}
                        filters={this.state.filters} />
        )
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

AccountsContainer.propTypes = {
    location: PropTypes.object.isRequired,
    history: PropTypes.object.isRequired
}

export default AccountsContainer;
