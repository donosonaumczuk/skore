import React, { Component } from 'react';
import Proptypes from 'prop-types';
import i18next from 'i18next';
import UserService from '../../../services/UserService';
import Loader from '../../Loader';
import AuthService from '../../../services/AuthService';
import EditUserButton from './components/EditUserButton';
import ErrorPage from '../ErrorPage';
import UserProfile from './layout';

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

    //TODO replace with getDeriveProperties and component DidMount
    // https://hackernoon.com/replacing-componentwillreceiveprops-with-getderivedstatefromprops-c3956f7ce607
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
            return <Loader />; //TODO replace with HOC
        }
        return (
            <UserProfile imageUrl={imageUrl} currentUser={currentUser} editButtons={editButtons}
            locationData={this.locationData} winRateAndAge={this.winRateAndAge} />
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
