import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import ActionButton from '../../../../ActionButton';

const getLikeButton = (isLogged, isLiked, likeSport, dislikeSport, sportName) => {
if (!isLogged) {
        return <Fragment></Fragment>;
    }
    if (isLiked) {
        return (
            <ActionButton buttonStyle="btn btn-negative join-button" 
                        handleClick={dislikeSport} data={sportName}
                        buttonText={null} fontAwesome="fas fa-thumbs-down mr-1" />
        );
    }
    else {
        return (
            <ActionButton buttonStyle="btn btn-green join-button"
                        handleClick={likeSport} data={sportName} 
                        buttonText={null} fontAwesome="fas fa-thumbs-up mr-1" />
        )
    }
}

const SportLikeButton = ({ isLogged, isLiked, likeSport, dislikeSport, sportName }) => {
    const likeButton = getLikeButton(isLogged, isLiked, likeSport, dislikeSport, sportName);
    return (
            <div className="offset-1 col my-auto">
                {likeButton}
            </div>
    );
}

SportLikeButton.propTypes = {
    isLogged: PropTypes.string,
    isLiked: PropTypes.bool,
    likeSport: PropTypes.func.isRequired,
    dislikeSport: PropTypes.func.isRequired,
    sportName: PropTypes.string.isRequired
}

export default SportLikeButton;