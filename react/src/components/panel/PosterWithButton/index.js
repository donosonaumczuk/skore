import React from 'react';
import { Link } from 'react-router-dom';
import Proptypes from 'prop-types';
import i18next from 'i18next';

const getLink = (currentUser, buttonText, buttonUrl) => {
    if (currentUser) {
        return (
            <Link id="create-match-btn" className="btn btn-white-succ" to={buttonUrl} role="button">
                {buttonText}
            </Link>
        );
    }
    else {
        return (
            <React.Fragment>
                <Link className="mr-1 poster-link" to="/login">
                    {i18next.t('navBar.signIn')}
                </Link>
                <span className="mr-1">{i18next.t('navBar.or')}</span>
                <Link className="poster-link" to="/signUp">
                    {i18next.t('navBar.createAccount')}
                </Link>
            </React.Fragment>
        );
    }
}

const PosterWithButton = ({ posterStyle, message, buttonText, buttonUrl, currentUser }) => {
    const link = getLink(currentUser, buttonText, buttonUrl);
    return (
        <div className={`col text-center p-4 ${posterStyle}`}>
            <p>{message}</p>
            {link}
        </div>
    );
}

PosterWithButton.propTypes = {
    posterStyle: Proptypes.string.isRequired,
    message: Proptypes.string.isRequired,
    buttonText: Proptypes.string.isRequired,
    buttonUrl: Proptypes.string.isRequired,
}

export default PosterWithButton;