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
    return contextPath + '/toJoinMatch';
}

function getJoinedURL() {
    return contextPath + '/joinedMatch';
}

function getCreatedURL() {
    return contextPath + '/createdMatch';
}

$("#to-join" ).click(function() {
    clearMatchs();
    putLoader();
    getButton = getJoinButton;
    endPointURL = getToJoinURL();
    currentFilters = filters.toJoin;
    loadCurrentFilters();
    loadMatches();
});

$("#joined").click(function() {
    clearMatchs();
    putLoader();
    getButton = getCancelButton;
    endPointURL = getJoinedURL();
    currentFilters = filters.joined;
    loadCurrentFilters();
    loadMatches();
});

$("#created").click(function() {
    clearMatchs();
    putLoader();
    getButton = getDeleteButton;
    endPointURL = getCreatedURL();
    currentFilters = filters.created;
    loadCurrentFilters();
    loadMatches();
});

function loadCurrentFilters() {
    pageNumber = 1;
    reachEndOfPagination = false;

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

function deleteMatch(key) {
    var data = getDataFromKey(key);

    $.ajax({
        type:   'POST',
        url:    contextPath + '/deleteMatch',
        data: {
            teamName1: data['team1name'],
            startTime: data['startTime'],
            finishTime: data['finishTime']
        }
    }).done(function(data) {
        if(data == 'true') {
            document.getElementById(key).remove();
        }
    });
}

function cancelAssistance(key) {
    var data = getDataFromKey(key);

    $.ajax({
        type:   'POST',
        url:    contextPath + '/removePlayerFromMatch',
        data: {
            teamName1: data['team1name'],
            startTime: data['startTime'],
            finishTime: data['finishTime']
        }
    }).done(function(data) {
        if(data == 'true') {
            document.getElementById(key).remove();
        }
    });
}

function getCancelButton(match) {
    return '<a class="btn btn-negative join-button" onclick="cancelAssistance(\'' + getMatchURLKey(match.startTime, match.team1, match.finishTime) + '\')"' +
        ' role="button"><i class="fas fa-times mr-1"></i>' + labelMap.joined[lang] + '</a>';
}

function getDeleteButton(match) {
    return '<a class="btn btn-negative join-button" onclick="deleteMatch(\'' + getMatchURLKey(match.startTime, match.team1, match.finishTime) + '\')"' +
        ' role="button"><i class="fas fa-trash-alt mr-1"></i>' + labelMap.created[lang] + '</a>';
}