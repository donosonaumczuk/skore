import React from 'react';
import i18next from 'i18next';

const MatchPagePlayers = () => {
    return (
        <div className="row text-center">
            <table className="table table-striped">
                <thead>
                <tr>
                    <th className="team-name" scope="col">
                        {i18next.t('match.teamOne')}
                    </th>
                    <th className="team-name" scope="col">
                        {i18next.t('match.teamTwo')}
                    </th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    )
}

export default MatchPagePlayers;