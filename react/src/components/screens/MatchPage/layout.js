import React from 'react';
import HomeMatchPropType from '../../../proptypes/HomeMatchPropType';
import WithError from '../../hocs/WithError';
import WithLoading from '../../hocs/WithLoading';
import WithMessage from '../../hocs/WithMessage';

//TODO improve layout
const MatchPage = ({ currentMatch }) => {
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3
                             col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">
                    <div className="row text-center">
                        <div className="col">
                            <h1>{currentMatch.title}</h1>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

MatchPage.propTypes = {
    currentMatch: HomeMatchPropType.isRequired
}

export default WithError(WithLoading(WithMessage(MatchPage)));