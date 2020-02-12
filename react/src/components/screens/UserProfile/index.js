import React, { Component } from 'react';
import Proptypes from 'prop-types';
import i18next from 'i18next';
import UserService from '../../../services/UserService';
import AuthService from '../../../services/AuthService';
import EditUserButton from './components/EditUserButton';
import UserProfile from './layout';
import { SC_OK } from '../../../services/constants/StatusCodesConstants';

class UserProfileContainer extends Component {
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

    static getDerivedStateFromProps(nextProps, prevState) {
        const { username } = nextProps.match.params;
        if (username !== prevState.username) {
            return { 
                username: username,
                currentUser: {},
                imageUrl: null,
                status: null
            };
        }
        else return null;
    }
     
    async componentDidUpdate(prevProps, prevState) {
        const oldUsername = prevState.username;
        const newUsername = this.props.match.params.username;
        if (oldUsername !== newUsername) {
            let currentUser = await UserService.getProfileByUsername(newUsername);
            if (this.mounted) {
                this.updateStateWithUser(currentUser);
            }
        }
    }

    render() {
        const currentUser = this.state.currentUser;
        const imageUrl = this.state.imageUrl;
        const loggedUser = AuthService.getCurrentUser();
        let editButtons = this.getEditButtons(loggedUser, this.state.username);
        const isLoading = imageUrl == null;
        let error = null;
        //TODO check when winrate is negative if it is a valid value
        if (this.state.status && this.state.staus !== SC_OK) {
            error = this.state.status;
        }
        
        return (
            <UserProfile imageUrl={imageUrl} currentUser={currentUser} 
                            editButtons={editButtons}
                            locationData={this.locationData}
                            winRateAndAge={this.winRateAndAge}
                            isLoading={isLoading} error={error} />
        );
    }
   

    componentWillUnmount = () => {
        this.mounted = false;
    }
}

UserProfileContainer.propTypes = {
    match: Proptypes.object.isRequired
}

export default UserProfileContainer;
