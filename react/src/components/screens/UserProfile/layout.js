import React from 'react';
import PropTypes from 'prop-types';
import UserProfilePropType from '../../../proptypes/UserProfilePropType';
import UserData from './components/UserData';
import UserImage from './components/UserImage';
import UserMatches from '../../UserMatches';
import WithLoading from '../../hocs/WithLoading';
import WithError from '../../hocs/WithError';

const UserProfile = ({ imageUrl, currentUser, editButtons, locationData, winRateAndAge, history }) => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 
                                col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                    <UserImage styleClass="profile-pic" imageUrl={imageUrl} />    
                    <div className="container-fluid profile-container bg-white rounded-border">
                        <UserData styleClass="profile-name" value={currentUser.firstName + " " + currentUser.lastName} />
                        <UserData styleClass="profile-username" value={currentUser.username} />
                        <UserData styleClass="profile-data" tag={locationData(currentUser.location)} />
                        <UserData styleClass="profile-data" tag={winRateAndAge(currentUser.winRate, currentUser.age)} />
                        {editButtons}
                    </div>
                    <UserMatches username={currentUser.username} history={history} />
                </div>
            </div>
        </div>
    );
}

UserProfile.propTypes = {
    imageUrl: PropTypes.string.isRequired,
    currentUser: UserProfilePropType.isRequired,
    editButtons: PropTypes.object,
    locationData: PropTypes.func.isRequired,
    winRateAndAge: PropTypes.func.isRequired,
    history: PropTypes.object.isRequired
}

export default WithError(WithLoading(UserProfile));