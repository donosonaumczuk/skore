import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import AccountPropType from '../../../../../proptypes/AccountPropType';
import ActionButton from '../../../../ActionButton';

const getButton = (isLogged, isLiked, likeUser, dislikeUser, username) => {
    if (!isLogged) {
        return <Fragment></Fragment>;
    }
    if (isLiked) {
        return (
            <ActionButton buttonStyle="btn btn-negative join-button" 
                        handleClick={dislikeUser} data={username}
                        buttonText={null} fontAwesome="fas fa-thumbs-down mr-1" />
        );
    }
    else {
        return (
            <ActionButton buttonStyle="btn btn-green join-button"
                        handleClick={likeUser} data={username} 
                        buttonText={null} fontAwesome="fas fa-thumbs-up mr-1" />
        )
    }
}

const goToProfile = (username, history) => {
    history.push(`/users/${username}`);
}

const Account = ({ account, isLogged, isLiked, likeUser, dislikeUser, history }) => {
    const button = getButton(isLogged, isLiked, likeUser, dislikeUser, account.username);
    return (
            <div className="card" onClick={(e) => goToProfile(account.username, history)}>
                <div className="card-body">
                    <div className="row">
                        <div className="col-10">
                            <h5 className="card-title">
                                {account.username}
                            </h5>
                        </div>
                        <div className="col">
                            {button}
                        </div>
                    </div>
                </div>
            </div>
      );
}

Account.propTypes = {
    account: AccountPropType.isRequired,
    isLogged: PropTypes.string,
    isLiked: PropTypes.bool,
    likeUser: PropTypes.func.isRequired,
    dislikeUser: PropTypes.func.isRequired
}

export default Account;