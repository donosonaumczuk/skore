import React, { Component } from 'react';
import Proptypes from 'prop-types';
import UserService from '../../../services/UserService';
import Loader from '../../Loader';
import UserMatch from './UserMatch';

class UserMatches extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        this.state = {
            matches: null,
        }
    }

    updateMatchesState = matches => {
        if (matches.status) {
            this.setState({
                status: matches.status
            });
        }
        else {
            this.setState({
                matches: matches
            });
        }
    }
    async componentDidMount() {
        this.mounted = true;
        let matches = await UserService.getUserMatches(this.props.username);
        if (this.mounted) {
            this.updateMatchesState(matches)
        }
    }

    render() {
        const matches = this.state.matches;
        if (matches) {
            return (
                <div className="container-fluid mt-4 rounded-border">
                    {
                        matches.map( (match, i) => <UserMatch key={i} currentMatch={match} username={this.props.username} />)
                    }    
                </div>
            );
        }
        else {
            return (
                <Loader />
            );
        }
    }

    componentWillUnmount = () => {
        this.mounted = false;
        //TODO cancel fetch if still fetching matches
    }
}

UserMatches.propTypes = {
    username: Proptypes.string.isRequired
}

export default UserMatches;