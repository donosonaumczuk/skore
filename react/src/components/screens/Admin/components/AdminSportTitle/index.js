import React from 'react';
import i18next from 'i18next';
import SportTextInfo from '../SportTextInfo';
import SportButton from '../SportButton';

const AdminSportTitle = () => {
    return (
        <div className="row p-2 mt-2 ml-5 mr-5 match-card rounded-border">
            <SportTextInfo text={i18next.t('admin.sportImage')} isTitle={true} />
            <SportTextInfo text={i18next.t('admin.sportName')} isTitle={true} />
            <SportTextInfo text={i18next.t('admin.displayName')} isTitle={true} />
            <SportTextInfo text={i18next.t('admin.playersPerTeam')} isTitle={true} />
            <div className="col"></div>
            <SportButton buttonStyle="btn btn-green join-button" 
                            buttonUrl="createSport" iStyle="fas fa-plus mr-1" 
                            buttonLabel={i18next.t('admin.createSport')} />
        </div>
    );
}

export default AdminSportTitle;