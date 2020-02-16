import React, { Component } from 'react';
import Axios from 'axios';
import queryString from 'query-string';
import PropTypes from 'prop-types';
import UserService from '../../../services/UserService';
import Utils from '../../utils/Utils';
import Accounts from './layout';
import AuthService from '../../../services/AuthService';
import { SC_CONFLICT, SC_UNAUTHORIZED, SC_OK } from '../../../services/constants/StatusCodesConstants';

const INITIAL_OFFSET = 0;
const QUERY_QUANTITY = 10;
const LIKES_OFFSET = 0;
const LIKES_LIMIT = 100;

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
            likes: {},
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

    updateLikes = likedUsers => {
        let newLikes = { ...this.state.likes };
        likedUsers.forEach(likedUser => {
            newLikes[likedUser.username] = true;
        });
        return newLikes;
    }

    getLikes = async () => {
        const { likesOffset, likesLimit } = this.state;
        const username = this.props.currentUser;
        if (this.mounted) {
            this.setState({ executing: true });
        }
        let response = await UserService.getLikedUsers(username, likesOffset, likesLimit);
        if (response.status) {
            if (this.mounted) {
                this.setState({ status: response.status, hasMoreLikes: false });
            }
        }
        else if (this.mounted) {
            const hasMore = Utils.hasMorePages(response.links);
            const newLikes = this.updateLikes(response.likedUsers);
            this.setState({
                likes: newLikes,
                likesOffset: this.state.likesOffset + response.likedUsers.length,
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

    likeUser = async (e, username) => {
        e.stopPropagation();
        const { currentUser } = this.props;
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await UserService.likeUser(currentUser, username);
        if (response.status === SC_UNAUTHORIZED) {
            const status = AuthService.internalLogout();
            if (status === SC_OK) {
                this.props.history.push(`/login`);
            }
            else {
                this.setState({ error: status });
            }
        }
        else if (response.status && response.status !== SC_CONFLICT) {
            if (this.mounted) {
                this.setState({ error: response.status, executing: false });
            }
        }
        else if (this.mounted) {
            const newLikes = { ...this.state.likes};
            newLikes[username] = true;
            this.setState({ likes: newLikes, executing: false });
        }
    }

    dislikeUser = async (e, username) => {
        e.stopPropagation();
        const { currentUser } = this.props;
        if (this.mounted) {
            this.setState({ executing: true });
        }
        const response = await UserService.dislikeUser(currentUser, username);
        if (response.status === SC_UNAUTHORIZED) {
            const status = AuthService.internalLogout();
            if (status === SC_OK) {
                this.props.history.push(`/login`);
            }
            else {
                this.setState({ error: status });
            }
        }
        else if (response.status && response.status !== SC_CONFLICT) {
            if (this.mounted) {
                this.setState({ error: response.status, executing: false });
            }
        }
        else if (this.mounted) {
            const newLikes = { ...this.state.likes};
            newLikes[username] = false;
            this.setState({ likes: newLikes, executing: false });
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
                    hasMore = this.mounted ? this.state.hasMoreLikes : false;
                }
            }
        }
    }

    render() {
        const { accounts, hasMore, hasMoreLikes, likes, executing } = this.state;
        const { currentUser, history } = this.props;
        const isLoading = ((accounts.length === 0 && hasMore) || (hasMoreLikes));
        return (
            <Accounts accounts={this.state.accounts} getUsers={this.getUsers}
                        hasMore={this.state.hasMore} likes={likes}
                        likeUser={this.likeUser} dislikeUser={this.dislikeUser}
                        currentUser={currentUser} isLoading={isLoading}
                        error={this.state.status} onSubmit={this.onSubmit}
                        filters={this.state.filters} history={history}
                        isExecuting={executing} />
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
