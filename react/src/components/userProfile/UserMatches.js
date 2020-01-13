import React, { Component } from 'react';
import Proptypes from 'prop-types';
import UserService from './../../services/UserService';
import Loader from '../Loader';

class UserMatches extends Component {
    constructor(props) {
        super(props);
        this.state = {
            matches: null
        }
    }

    async componentDidMount() {
        let matches = await UserService.getUserMatches(this.props.username);
        console.log(matches);

    }

    render() {
        if(this.state.matches) {
            return (
                <React.Fragment></React.Fragment>
            );
        }
        else {
            return (
                <Loader />
            );
        }
    }
}

UserMatches.propTypes = {
    username: Proptypes.string.isRequired
}

export default UserMatches;