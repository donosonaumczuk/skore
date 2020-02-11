import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';
import PropTypes from 'prop-types';
import AuthService from '../../../services/AuthService';
import UserService from '../../../services/UserService';
import Loader from '../../Loader';
import EditUserInfo from './layout';

class EditUserInfoContainer extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const { username } = this.props.match.params;
        this.state = {
            username: username,
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
        this.mounted = true;
        const currentUser = AuthService.getCurrentUser();
        if (currentUser && currentUser === this.state.username) { 
            let currentUser = await UserService.getUser(this.state.username);
            if (this.mounted) {
                this.updateStateWithUser(currentUser);
            }
        }
    }

    loadHome = home => {
        if (home) {
            const { country, state, city, street } = home;
            return {
                "country": country ? country : "",
                "state": state ? state : "",
                "city": city ? city : "",
                "street": street ? street : ""
            };
        }
        return null;
    }

    loadFormInitialValues = () => {
        if (this.state.currentUser) {
            const { currentUser } = this.state;
            const home = this.loadHome(currentUser.home);
            let newUser = {
                "username": currentUser.username,
                "email": currentUser.email,
                "firstName": currentUser.firstName,
                "lastName": currentUser.lastName,
                "year": currentUser.birthday.year,
                "month": currentUser.birthday.monthNumber,
                "day": currentUser.birthday.dayOfMonth,
                "cellphone": currentUser.cellphone,
            }
            if (home) {
                newUser = { ...newUser, "home": home };
            }
            return newUser;
        }
        return {};
    }

    render() {
        const currentUser = AuthService.getCurrentUser();
        const formInitialValues = this.loadFormInitialValues();       
        if (!currentUser) {
            //TODO maybe render error page with 403 forbidden
            return <Redirect to="/" />
        }
        else if (currentUser !== this.state.username) {
            //TODO maybe render error page with 403 forbidden
            return <Redirect to={`/users/${currentUser}/editUserInfo`} />
        }
        else if (!this.state.currentUser) {
            return <Loader />
        }
        return (
            <EditUserInfo initialValues={formInitialValues} username={currentUser}
                            history={this.props.history} />
        );
    }

    componentWillUnmount() {
        this.mounted = false;
    }
}

EditUserInfoContainer.propTypes = {
    match: PropTypes.object.isRequired,
    history: PropTypes.object.isRequired
}

export default EditUserInfoContainer;