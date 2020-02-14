import React from 'react';
import PropTypes from 'prop-types';
import HomeMatchPropType from '../../../proptypes/HomeMatchPropType';
import MatchTitle from '../../match/MatchTitle';
import MatchPageCreator from './components/MatchPageCreator';
import MatchPageSport from './components/MatchPageSport';
import MatchPageCompetitivity from './components/MatchPageCompetitivity';
import MatchDate from '../../match/MatchDate';
import MatchPageDuration from './components/MatchPageDuration';
import MatchPageDescription from './components/MatchPageDescription';
import MatchPageLocation from './components/MatchPageLocation';
import WithError from '../../hocs/WithError';
import MatchPageStatus from './components/MatchPageStatus';
import MatchPageAvailability from './components/MatchPageAvailability';
import MatchPagePlayers from './components/MatchPagePlayers';
// import WithAnonymous from '../../hocs/WithAnonymous';//TODO add without breaking
import WithExecuting from '../../hocs/WithExecuting';
import WithLoading from '../../hocs/WithLoading';
import WithMessage from '../../hocs/WithMessage';

const getImageUrls = links => {
    let creatorImageUrl;
    let sportImageUrl;
    links.map(link => {
        if (link.rel === "creatorImage") {
            creatorImageUrl = link.href;
        }
        if (link.rel === "sportImage") {
            sportImageUrl = link.href;
        }
        return link;
    });
    return {
        creatorImageUrl: creatorImageUrl,
        sportImageUrl: sportImageUrl
    };
}

const MatchPage = ({ currentMatch, updateMatchScore, joinMatch,
                        cancelMatch, deleteMatch }) => {
    const { creatorImageUrl, sportImageUrl } = getImageUrls(currentMatch.links);
    const { title, creator, sportName, competitive, date, time, minutesOfDuration,
            description, location, team1, team2 } = currentMatch;
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="container-fluid bg-white rounded-border match-container
                            offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3
                            col-lg-6 offset-xl-4 col-xl-4">
                    <MatchTitle matchTitle={title} />
                    <MatchPageCreator creatorImageUrl={creatorImageUrl} creator={creator} />
                    <MatchPageSport sportImageUrl={sportImageUrl} sport={sportName} />
                    <MatchPageCompetitivity isCompetitive={competitive} />
                    <MatchDate date={date} time={time} isMatchPage={true} />
                    <MatchPageDuration startTime={time} durationInMinutes={minutesOfDuration} />
                    <MatchPageDescription description={description} />
                    <MatchPageLocation address={location} />
                    <MatchPageAvailability currentMatch={currentMatch} joinMatch={joinMatch}
                                            cancelMatch={cancelMatch} deleteMatch={deleteMatch} />
                    <MatchPageStatus currentMatch={currentMatch} updateMatchScore={updateMatchScore} />
                    <MatchPagePlayers teamOnePlayers={team1.players} 
                                        teamTwoPlayers={team2 ? team2.players : [] } />
                </div>
            </div>
        </div>
    );
}

MatchPage.propTypes = {
    currentMatch: HomeMatchPropType.isRequired,
    updateMatchScore: PropTypes.func.isRequired,
    joinMatch: PropTypes.func.isRequired,
    cancelMatch: PropTypes.func.isRequired,
    deleteMatch: PropTypes.func.isRequired
}

export default WithError(WithLoading(WithExecuting(WithMessage((MatchPage)))));