import React from 'react';
import Proptypes from 'prop-types';
import i18next from 'i18next';

const UserImage = ({ styleClass, imageUrl }) => {
    return (
        <div className="row text-center">
            <div className="col">
                <img className={styleClass} src={imageUrl? imageUrl : ""} alt={i18next.t('profile.imageDescription')} />
            </div>
        </div>
    )
}

UserImage.propTypes = {
    styleClass: Proptypes.string.isRequired,
    imageUrl: Proptypes.string
}

export default UserImage;