import PropTypes from 'prop-types';

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

const Utils = {
    hasMorePages: hasMorePages
};

export default Utils;