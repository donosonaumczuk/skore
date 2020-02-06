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
    const url = buildUrlFromParamQueries(params);
    return url;
}

const removeUnknownHomeFilters = filters => {
    return {
        country: filters.country,
        state: filters.state,
        city: filters.city,
        sport: filters.sport
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

const Utils = {
    hasMorePages: hasMorePages,
    buildUrlFromParamQueriesAndTab: buildUrlFromParamQueriesAndTab,
    removeUnknownHomeFilters: removeUnknownHomeFilters,
    replaceWithNewMatch: replaceWithNewMatch,
    deleteMatch: deleteMatch
};

export default Utils;