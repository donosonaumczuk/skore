import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';
import queryString from 'query-string';
import PropTypes from 'prop-types';
import AuthService from '../../../services/AuthService';
import UserService from '../../../services/UserService';
import Loader from '../../Loader';

class ConfirmAccountContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const queryParams = queryString.parse(props.location.search);
        const { username, code } = queryParams;   
        this.state = {
            status: null,
            isLoading: true,
            username: username,
            code: code
        };
    }

    componentDidMount = async () => {
        this.mounted = true;
        const { username, code } = this.state;
        const response = await UserService.verifyUser(username, code);
        if (response.status && this.mounted) {
            this.setState({ status: response.status, isLoading: false });
        }
        else {
            this.setState({ status: response.status, isLoading: false });
            this.props.updateUser({ username: username });
            this.props.history.push(`/users/${username}`);
        }
    }

    render() {
        if (AuthService.getCurrentUser()) {
            return <Redirect to="/" />
        }
        return <Loader />;
        
    }

    componentWillUnmount = () => {
        this.mounted =false;
    }
}

ConfirmAccountContainer.propTypes = {
    location: PropTypes.object.isRequired,
    history: PropTypes.object.isRequired,
    updateUser: PropTypes.func.isRequired
}

export default ConfirmAccountContainer;