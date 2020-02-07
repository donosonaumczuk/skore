import React from 'react';
import PropTypes from 'prop-types';
import i18next from 'i18next';
import MatchButton from '../../match/MatchButton';
import HomeMatchPropType from '../../../proptypes/HomeMatchPropType';
import WithError from '../../hocs/WithError';
import WithLoading from '../../hocs/WithLoading';
import WithMessage from '../../hocs/WithMessage';

//TODO improve layout and make update result button only appears when match has been played
const MatchPage = ({ currentMatch, updateMatchScore }) => {
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
                    <MatchButton buttonStyle="btn btn-green join-button"
                            handleClick={updateMatchScore} currentMatch={currentMatch}
                            buttonText={i18next.t('setMatchScoreForm.setScoreButton')}
                            fontAwesome="fas fa-times mr-1" />
                </div>
            </div>
        </div>
    );
}

MatchPage.propTypes = {
    currentMatch: HomeMatchPropType.isRequired,
    updateMatchScore: PropTypes.func.isRequired
}

export default WithError(WithLoading(WithMessage(MatchPage)));