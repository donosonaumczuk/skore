import React, { Component } from 'react';
import Proptypes from 'prop-types';
import i18next from 'i18next';
import UserService from '../../services/UserService';
import UserData from './UserData';
import UserImage from './UserImage';
import UserMatches from './userMatches/UserMatches';
import Loader from '../Loader';
import AuthService from '../../services/AuthService';
import EditUserButton from './EditUserButton';
import ErrorPage from '../ErrorPage';

class UserProfile extends Component {
    mounted = false;
    constructor(props) {
        super(props);
        const { username } = this.props.match.params;
        this.state = {
            username: username,
            currentUser: {},
            imageUrl: null,
        }
    }

    updateStateWithUser = currentUser => {
        if (currentUser.status) {
            this.setState({ status: currentUser.status });
        }
        else {
            const imageUrl = this.getImageUrl(currentUser.links);
            this.setState({ currentUser: currentUser, 
                            imageUrl: imageUrl 
            });
        }
    }

    async componentDidMount() { 
        this.mounted = true  ;
        let currentUser = await UserService.getProfileByUsername(this.state.username);
        if (this.mounted) {
            this.updateStateWithUser(currentUser);
        }
    }

    locationData = (location) => {
        return (
                <span>
                    <i className="fas fa-map-marker-alt"></i>
                    {" " + location}
                </span>
        );
    }

    winRateAndAge = (winrate, age) => {
        const winValue = winrate ? winrate : " -- ";
        let winClass = "";
        if (winrate) {
            winClass = winrate >= 50 ? "winnerWinRate" : "loserWinRate";
        } 
        return (
            <span>
                    <i className="fas mr-1 fa-percentage"></i>
                    <span className={winClass}>{" " + winValue + " "}</span>
                    <i className="fas ml-4 mr-1 fa-birthday-cake"></i>
                    {" " + age}
            </span>      
        );
    }
    
    getImageUrl = links => {
        let imageUrl;
        links.map(link => {
            if (link.rel === "image") {
                imageUrl = link.href;
            }
            return link;
        })
        return imageUrl;
    }

    getEditButtons = (loggedUser, username) => {
        if (loggedUser && loggedUser === username) {
            return (
                <div className="row text-center">
                    <div className="col">
                        <EditUserButton url={`/users/${username}/edit`} 
                            iStyle="mr-1 fas fa-edit" text={i18next.t('profile.editInfo')} />
                        <EditUserButton url={`/users/${username}/changePassword`} 
                            iStyle="mr-1 fas fa-key" text={i18next.t('profile.changePassword')} />
                    </div>
                </div>
            )
        }
        else {
            return <React.Fragment></React.Fragment>
        }
    }

    async UNSAFE_componentWillReceiveProps(nextProps) {
        const { username } = nextProps.match.params;
        if (username !== this.state.username && this.mounted) {
            this.setState({ username: username,
                            currentUser: {},
                            imageUrl: null,
            });
        }
        let currentUser = await UserService.getProfileByUsername(username);
        if (this.mounted) {
            this.updateStateWithUser(currentUser);
        }
    }

    render() {
        const currentUser = this.state.currentUser;
        const imageUrl = this.state.imageUrl;
        const loggedUser = AuthService.getCurrentUser();
        let editButtons = this.getEditButtons(loggedUser, this.state.username);
        //TODO check when winrate is negative if it is a valid value
        if (this.state.status && this.state.staus !== 200) {
            return <ErrorPage status={this.state.status} />
        }
        else if (imageUrl == null) {
            return <Loader />;
        }
        
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                        <UserImage styleClass="profile-pic" imageUrl={imageUrl} />    
                        <div className="container-fluid profile-container bg-white rounded-border">
                            <UserData styleClass="profile-name" value={currentUser.firstName + " " + currentUser.lastName} />
                            <UserData styleClass="profile-username" value={currentUser.username} />
                            <UserData styleClass="profile-data" tag={this.locationData(currentUser.location)} />
                            <UserData styleClass="profile-data" tag={this.winRateAndAge(currentUser.winRate, currentUser.age)} />
                            {editButtons}
                        </div>
                        <UserMatches username={this.state.username} />
                    </div>
                </div>
            </div>
        );
    }
   

    componentWillUnmount = () => {
        this.mounted = false;
    }
}

//TODO update with custom proptype
UserProfile.propTypes = {
    match: Proptypes.object.isRequired
}

export default UserProfile;
