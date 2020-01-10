import React, { Component } from 'react';
import Proptypes from 'prop-types';
import getProfileByUsername from '../../services/UserService';
import UserData from './UserData';

class UserProfile extends Component {
    constructor(props) {
        super(props);
        const { username } = this.props;
        this.state = {
            username: username,
            currentUser: {}
        }
    }

    async componentDidMount() {   
        let currentUser = await getProfileByUsername(this.state.username)
        this.setState({ currentUser: currentUser });
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
        
        if(winrate) {
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
    
    render() {
        const currentUser = this.state.currentUser;
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                        <div className="container-fluid profile-container bg-white rounded-border">
                            <UserData styleClass="profile-name" value={currentUser.firstName + " " + currentUser.lastName} />
                            <UserData styleClass="profile-username" value={currentUser.username} />
                            <UserData styleClass="profile-data" tag={this.locationData(currentUser.location)} />
                            <UserData styleClass="profile-data" tag={this.winRateAndAge(currentUser.winRate, currentUser.age)} />
                            {/* <Matches /> TODO*/}
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

UserProfile.propTypes = {
    username: Proptypes.string.isRequired
}

export default UserProfile;
