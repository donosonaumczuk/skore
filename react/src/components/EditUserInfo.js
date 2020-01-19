import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';
import EditUserInfoForm from './forms/EditUserInfoForm';
import AuthService from '../services/AuthService';
import UserService from '../services/UserService';
import Loader from './Loader';


class EditUserInfo extends Component {
    constructor(props) {
        super(props);
        const { username } = this.props.match.params;
        this.state = {
            username: username,
            mounted: true
        };
    }

    updateStateWithUser = currentUser => {
        if (currentUser.status) {
            this.setState({ status: currentUser.status });
        }
        else {
            this.setState({ currentUser: currentUser });
        }
    }

    async componentDidMount() {
        //TODO make request to userInfo not to profile, so as to have complete info  
        if (AuthService.getCurrentUser()) { 
            let currentUser = await UserService.getProfileByUsername(this.state.username);
            if (this.state.mounted) {
                this.updateStateWithUser(currentUser);
            }
        }
    }

    loadFormInitialValues = () => {
        if (this.state.currentUser) {
            const { currentUser } = this.state;
            return {
                "username": currentUser.username,
                "email": currentUser.email,
                "firstName": currentUser.firstName,
                "lastName": currentUser.lastName,
                "birthday": currentUser.birthday,
                "cellphone": currentUser.cellphone
            }
        }
        return {};
    }


    render() {
        const currentUser = AuthService.getCurrentUser();
        const formInitialValues = this.loadFormInitialValues();       
        if (!currentUser) {
            return <Redirect to="/" />
        }
        else if (currentUser !== this.state.username) {
            return <Redirect to={`/users/${currentUser}/editUserInfo`} />
        }
        else if (!this.state.currentUser) {
            return <Loader />
        }
        return (
            <EditUserInfoForm initialValues={formInitialValues} />
        );
    }

    componentWillUnmount() {
        this.setState({ mounted: false });
    }
}

export default EditUserInfo;