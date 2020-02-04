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

const Utils = {
    hasMorePages: hasMorePages,
    buildUrlFromParamQueriesAndTab: buildUrlFromParamQueriesAndTab,
    removeUnknownHomeFilters: removeUnknownHomeFilters
};

export default Utils;