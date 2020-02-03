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
    if (!params) {
        params = { tab: `${currentTab}` };
    }
    else {
        params = { ...params, tab: `${currentTab}` };
    }
    const url = buildUrlFromParamQueries(params);
    return url;
}

const Utils = {
    hasMorePages: hasMorePages,
    buildUrlFromParamQueriesAndTab: buildUrlFromParamQueriesAndTab
};

export default Utils;