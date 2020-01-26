import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';
import Proptypes from 'prop-types';
import AuthService from './../services/AuthService';
import Loader from './Loader';

class LogOut extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const currentUser = AuthService.getCurrentUser();
        this.state = {
            currentUser: currentUser,
            loggedOut: false
        };
    }

    componentDidMount = async () => {
        this.mounted = true;
        if (this.state.currentUser) {
            await AuthService.logOutUser();
            this.props.updateUser(null);
            this.setState({ loggedOut: true });
        }
    }

    render() {
        if (!this.state.currentUser || this.state.loggedOut) {
            return (<Redirect to="/" />);
        }
        else {
            //TODO improve with better spinner on popup
            return (<Loader />);
        }
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

LogOut.propTypes = {
    updateUser: Proptypes.func.isRequired
}

export default LogOut;