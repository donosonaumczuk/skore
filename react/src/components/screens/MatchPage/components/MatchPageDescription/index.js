import React, { Fragment } from 'react';
import PropTypes from 'prop-types';

const MatchPageDescription = ({ description }) => {
    if (!description) {
        return <Fragment></Fragment>;
    }
    return (
        <div className="row text-center">
            <div className="col">
                <i className="fas mr-2 fa-comment-alt"></i>
                {description}
            </div>
        </div>
    );
}

MatchPageDescription.propTypes = {
    description: PropTypes.string
}

export default MatchPageDescription;