import React from 'react';

const MatchPagePlayer = ({ gamePlayers }) => {
    return (
        <tr>
            <td scope="row">
                <a data-toggle="tooltip" data-placement="top" data-html="true" title="Nombre Apellido | 21 años" class="player-username skore-link" href="http://baseurl.com/api/profile/otherusername">@otherUsername</a>
            </td>
            <td scope="row">
                <a data-toggle="tooltip" data-placement="top" data-html="true" title="Nombre Apellido | 21 años" class="player-username skore-link" href="http://baseurl.com/api/profile/myUser">@myUser</a>
			</td>
        </tr>
    )
}

export default MatchPagePlayer;