import React from 'react';

const handleClick = (creator) => {

}

const CreatorInfo = ({ creatorImageUrl, creator, title }) => {
    return (
        <React.Fragment>
            <div className="col-2 col-sm-1 pl-0">
                <img src={creatorImageUrl} onClick={handleClick(creator)} className="user-avatar" alt="user-pic" />
            </div>
            <div className="col-3 col-sm-4">
                <div className="row">
                    <p className="name-label">{title}</p>
                </div>
                <div className="row">
                    <a className="username-label" href={`/users/${creator}`}>@{creator}</a>
                </div>
            </div>
        </React.Fragment>
    );

}

export default CreatorInfo;