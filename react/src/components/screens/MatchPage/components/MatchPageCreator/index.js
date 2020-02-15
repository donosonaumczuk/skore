import React from 'react';
import { useHistory, withRouter, Link } from 'react-router-dom';
import i18next from 'i18next';
import Proptypes from 'prop-types';

const handleClick = (e, creator, history) => {
    e.stopPropagation();
    history.push(`/users/${creator}`);
}

const MatchPageCreator = ({ creatorImageUrl, creator }) => {
    const history = useHistory();
    return (
        <React.Fragment>
            <div className="row text-center">
                <div className="col">
                    <img src={creatorImageUrl} onClick={(e) => handleClick(e, creator, history)}
                            className="user-avatar" alt={i18next.t('profile.imageDescription')} />
                </div>
            </div>
            <div className="row text-center">
                <div className="col">
                    <Link className="username-label" to={`/users/${creator}`}
                            onClick={(e) => handleClick(e, creator, history)} >
                        @{creator}
                    </Link>
                </div>
            </div> 
        </React.Fragment>
    );
}

MatchPageCreator.propTypes = {
    creatorImageUrl: Proptypes.string,
    creator: Proptypes.string.isRequired,
}

export default withRouter(MatchPageCreator);