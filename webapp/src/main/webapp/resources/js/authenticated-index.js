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
    // var currentURL = document.location; //TODO: remove later
    window.history.replaceState("object or string", "Title", '/'); //TODO: try to edit URL on the fly
    clearMatchs();
    putLoader();
    getButton = getJoinButton;
    endPointURL = getToJoinURL();
    currentFilters = filters.toJoin;
    loadCurrentFilters();
    loadMatches();
});

$("#joined").click(function() {
    // var currentURL = document.location; //TODO: remove later
    window.history.replaceState("object or string", "Title", '/joined'); //TODO: try to edit URL on the fly
    clearMatchs();
    putLoader();
    getButton = getCancelButton;
    endPointURL = getJoinedURL();
    currentFilters = filters.joined;
    loadCurrentFilters();
    loadMatches();
});

$("#created").click(function() {
    // var currentURL = document.location; //TODO: remove later
    window.history.replaceState("object or string", "Title", '/created'); //TODO: try to edit URL on the fly
    clearMatchs();
    putLoader();
    getButton = getDeleteButton;
    endPointURL = getCreatedURL();
    currentFilters = filters.created;
    loadCurrentFilters();
    loadMatches();
});

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