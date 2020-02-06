import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';
import queryString from 'query-string';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import AuthService from '../../../services/AuthService';
import UserService from '../../../services/UserService';

class ConfirmAccountContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const queryParams = queryString.parse(props.location.search);
        const { username, code } = queryParams;   
        console.log("username: ", username);
        console.log("code: ", code);
        this.state = {
            status: null,
            isLoading: true,
            username: username,
            code: code
        };
    }

    componentDidMount = async () => {
        this.mounted = true;
        const response = await UserService.verifyUser(this.state.code);
        if (response.status && this.mounted) {
            this.setState({ status: response.status });
        }
        else {
            console.log(response);
        }
    }

    render() {
        //TODO improve layout
        if (AuthService.getCurrentUser()) {
            return <Redirect to="/" />
        }
        //login
        return (
            //redirect to userprofile
            <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid profile-container bg-white rounded-border alert alert-info alert-dismissible fade show mt-1">
                        <h1>{i18next.t('confirmAccount.confirmed')}</h1>
                        <h2>{i18next.t('confirmAccount.confirmEmail')}</h2>
                    </div>
                </div>
            </div>
        );
    }

    componentWillUnmount = () => {
        this.mounted =false;
    }
}

ConfirmAccountContainer.propTypes = {
    location: PropTypes.object.isRequired
}

export default ConfirmAccountContainer;