import React from 'react';
import { useHistory, withRouter, Link } from 'react-router-dom';
import i18next from 'i18next';
import Proptypes from 'prop-types';

const handleClick = (creator, history) => {
    history.push(`/users/${creator}`);
}

const CreatorInfo = ({ creatorImageUrl, creator, title }) => {
    const history = useHistory();
   
    return (
        <React.Fragment>
            <div className="col-2 col-sm-1 pl-0">
                    <img src={creatorImageUrl} onClick={() => handleClick(creator, history)} className="user-avatar"
                    alt={i18next.t('profile.imageDescription')} />
            </div>
            <div className="col-3 col-sm-4">
                <div className="row">
                    <p className="name-label">{title}</p>
                </div>
                <div className="row">
                    <Link className="username-label" to={`/users/${creator}`}>@{creator}</Link>
                </div>
            </div>
        </React.Fragment>
    );
}

CreatorInfo.propTypes = {
    creatorImageUrl: Proptypes.string,
    creator: Proptypes.string.isRequired,
    title: Proptypes.string.isRequired,
}
export default withRouter(CreatorInfo);