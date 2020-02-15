import React, { Component } from 'react';
import Axios from 'axios';
import queryString from 'query-string';
import PropTypes from 'prop-types';
import UserService from '../../../services/UserService';
import Utils from '../../utils/Utils';
import Accounts from './layout';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 10;
const LIKES_OFFSET = 0;
const LIKES_LIMIT = 1;

class AccountsContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const queryParams = queryString.parse(props.location.search);
        const hasMoreLikes = this.props.currentUser ? true : false;
        this.state = {
            accounts: [],
            offset: INITIAL_OFFSET,
            limit: QUERY_QUANTITY,
            hasMore: true,
            filters: queryParams,
            likes: [],
            likesOffset: LIKES_OFFSET,
            likesLimit: LIKES_LIMIT,
            hasMoreLikes: hasMoreLikes
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

    getLikes = async () => {
        const { likesOffset, likesLimit } = this.state;
        const username = this.props.currentUser;
        this.setState({ executing: true });
        let response = await UserService.getLikedUsers(username, likesOffset, likesLimit);
        if (response.status) {
            if (this.mounted) {
                this.setState({ status: response.status, hasMore: false });
            }
        }
        else if (this.mounted) {
            const hasMore = Utils.hasMorePages(response.links);
            this.setState({
                likes: [...this.state.likes, ...response.likedUsers],
                offset: this.state.offset + response.likedUsers.length,
                hasMoreLikes: hasMore,
                executing: false
            });
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
        if (this.props.currentUser) {
            let hasMore = this.state.hasMoreLikes;
            while (hasMore) {
                if (!this.state.loading) {
                    await this.getLikes();
                    hasMore = this.state.hasMoreLikes;
                }
            }
            this.getLikes();      
        }
        console.log(this.state);
    }

    render() {
        const { accounts, hasMore, hasMoreLikes } = this.state;
        const isLoading = ((accounts.length === 0 && hasMore) || (hasMoreLikes));
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
    history: PropTypes.object.isRequired,
    currentUser: PropTypes.string
}

export default AccountsContainer;
