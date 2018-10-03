var filters = {
    toJoin: {
        country: {},
        state: {},
        city: {}
    },
    joined: {
        country: {},
        state: {},
        city: {}
    },
    created: {
        country: {},
        state: {},
        city: {}
    }
};

var currentFilters = filters.toJoin;
var endPointURL = getDefaultEndPoint();

function getToJoinURL() {
    return '/toJoinMatch';
}

function getJoinedURL() {
    return '/joinedMatch';
}

function getCreatedURL() {
    return '/createdMatch';
}

$("#to-join" ).click(function() {
    clearMatchs();
    putLoader();
    getButton = getJoinButton;
    postAppendMatch = joinButtonPostAppendMatch;
    endPointURL = getToJoinURL();
    currentFilters = filters.toJoin;
    loadCurrentFilters();
    loadMatches();
});

$("#joined").click(function() {
    clearMatchs();
    putLoader();
    getButton = getCancelButton;
    postAppendMatch = cancelButtonPostAppendMatch;
    endPointURL = getJoinedURL();
    currentFilters = filters.joined;
    loadCurrentFilters();
    loadMatches();
});

$("#created").click(function() {
    clearMatchs();
    putLoader();
    getButton = getDeleteButton;
    postAppendMatch = deleteButtonPostAppendMatch;
    endPointURL = getCreatedURL();
    currentFilters = filters.created;
    loadCurrentFilters();
    loadMatches();
});

function loadCurrentFilters() {
    pageNumber = 1;

    console.log(currentFilters);

    $('.badge').remove();

    var countries = Object.keys(currentFilters.country);
    for(var i = 0; i < countries.length; i++) {
        addBadge(countries[i], 'country');
    }

    var states = Object.keys(currentFilters.state);
    for(var i = 0; i < states.length; i++) {
        addBadge(states[i], 'state');
    }

    var cities = Object.keys(currentFilters.city);
    for(var i = 0; i < cities.length; i++) {
        addBadge(cities[i], 'city');
    }
}

function getDefaultEndPoint() {
    return getToJoinURL();
}

function deleteButtonPostAppendMatch(match) {

}

function cancelButtonPostAppendMatch(match) {

}

function getCancelButton(match) {
    return '<a class="btn btn-negative" href="/joinMatch/' + getMatchURLKey(match.startTime, match.team1, match.finishTime) +
        ' role="button"><i class="fas fa-times mr-1"></i>' + labelMap.joined[lang] + '</a>';
}

function getDeleteButton(match) {
    return '<a class="btn btn-negative" href="/joinMatch/' + getMatchURLKey(match.startTime, match.team1, match.finishTime) +
        ' role="button"><i class="fas fa-trash-alt mr-1"></i>' + labelMap.created[lang] + '</a>';
}

function joinButtonPostAppendMatch(match) {
    /* Do nothing... */
    return;
}