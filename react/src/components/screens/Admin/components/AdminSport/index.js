import React from 'react';
import i18next from 'i18next';
import SportPropTypes from '../../../../../proptypes/SportPropType';
import SportImage from '../SportImage';
import SportTextInfo from '../SportTextInfo';
import SportButton from '../SportButton';

const getSportImageUrl = links => {
    let sportImageUrl;
    links.forEach(link => {
        if (link.rel === "image") {
            sportImageUrl = link.href;
        }
    });
    return sportImageUrl;
}

const AdminSport = ({ sport }) => {
    //TODO maybe make image bigger and name bigger, add more staff to sport
    const sportImageUrl = getSportImageUrl(sport.links);
    return (
        <div className="row p-2 mt-2 ml-5 mr-5 match-card rounded-border">
            <SportImage sportImageUrl={sportImageUrl} sport={sport.displayName} />
            <SportTextInfo text={sport.sportName} />
            <SportTextInfo text={sport.displayName} />
            <SportTextInfo text={`${sport.playerQuantity}`} />
            <SportButton buttonStyle="btn btn-outline-secondary join-button"
                            buttonUrl={`sports/${sport.sportName}/edit`}
                            iStyle="fas fa-edit mr-1" 
                            buttonLabel={i18next.t('admin.editSport')} />
            <SportButton buttonStyle="btn btn-negative join-button" 
                            buttonUrl={`sports/${sport.sportName}/edit`}
                            iStyle="fas fa-trash-alt mr-1"
                            buttonLabel={i18next.t('admin.deleteSport')} />
        </div>
    );
}

AdminSport.propTypes = {
    sport: SportPropTypes.isRequired
}

export default AdminSport;