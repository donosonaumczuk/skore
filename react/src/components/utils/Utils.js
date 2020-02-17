import React from 'react';
import PropTypes from 'prop-types';
import { buildUrlFromParamQueries } from '../../services/Util';

const hasMorePages = links => {
    let hasMore = false;
    links.forEach(link => {
        if (link.rel === "next") {
            hasMore = true;
        }
    });
    return hasMore;
}

hasMorePages.PropTypes = {
    links: PropTypes.array.isRequired
}

const buildUrlFromParamQueriesAndTab = (params, currentTab) => {
    if (currentTab) {
        if (!params) {
            params = { tab: `${currentTab}` };
        }
        else {
            params = { ...params, tab: `${currentTab}` };
        }
    }
    let url = buildUrlFromParamQueries(params);
    return url.replace(/ /g, "+");
}

const removeUnknownHomeFilters = filters => {
    return {
        country: filters.country,
        state: filters.state,
        city: filters.city,
        sport: filters.sport,
        onlyLikedSports: filters.onlyLikedSports,
        onlyLikedUsers: filters.onlyLikedUsers,
    };
}

const replaceWithNewMatch = (matches, matchToReplace) => {
    return matches.map(match => {
        if (match.key === matchToReplace.key) {
            return matchToReplace;
        }
        return match;
    });
}

const deleteMatch = (matches, matchToDelete) => {
    return matches.filter(match => match.key !== matchToDelete.key);
}

const generateOptionsForSelectBetweenValues = (minValue, maxValue) => {
    let options = [];
    for (var i = minValue; i <= maxValue; i++) {
        options.push(<option key={i} value={i}>{i}</option>);
    }
    return options;
}

const removeLastSpaceFromString = string => {
    if (!string || string.length === 0) {
        return "";
    }
    const length = string.length;
    if (string.charAt(length - 1) === ' ') {
        return string.substring(0, length - 1);
    }
    return string;
}

const removePlayerFromMatch = (match, userId) => {
    let teamOnePlayers = match.team1.players;
    teamOnePlayers = teamOnePlayers.filter(player => player.userId !== userId);
    match.team1.players = teamOnePlayers;
    if (match.team2) {
        let teamTwoPlayers = match.team2.players;
        teamTwoPlayers = teamTwoPlayers.filter(player => player.userId !== userId);
        match.team2.players = teamTwoPlayers;
    }
    match.currentPlayers = match.currentPlayers -1;
    return match;
}

const addressToString = address => {
    if (!address) {
        return "";
    }
    const street = removeLastSpaceFromString(address.street);
    const number = removeLastSpaceFromString(address.number);
    const city = removeLastSpaceFromString(address.city);
    const state = removeLastSpaceFromString(address.state);
    const country = removeLastSpaceFromString(address.country);
    let addressString = `${street}`;
    addressString = number.length > 0 ? `${addressString} ${number}` : addressString;
    addressString = city.length > 0 ? `${addressString}, ${city}` : addressString;
    addressString = state.length > 0 ? `${addressString}, ${state}` : addressString;
    addressString = country.length > 0 ? `${addressString}, ${country}` : addressString;
    return addressString;
}

const getErrorMessage = (errorMessage) => {
    if (errorMessage) {
        return (<span className="invalid-feedback d-block">
                    {errorMessage}
                </span>);
    }
    return <React.Fragment></React.Fragment>;
}

const Utils = {
    hasMorePages: hasMorePages,
    buildUrlFromParamQueriesAndTab: buildUrlFromParamQueriesAndTab,
    removeUnknownHomeFilters: removeUnknownHomeFilters,
    replaceWithNewMatch: replaceWithNewMatch,
    deleteMatch: deleteMatch,
    generateOptionsForSelectBetweenValues: generateOptionsForSelectBetweenValues,
    removeLastSpaceFromString: removeLastSpaceFromString,
    addressToString: addressToString,
    removePlayerFromMatch: removePlayerFromMatch,
    getErrorMessage: getErrorMessage
};

export default Utils;