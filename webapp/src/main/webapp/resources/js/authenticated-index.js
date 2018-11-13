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
    currentFilters = filters.toJoin;
    section = '';
    var url = getURLFromFilters();
    window.history.pushState("", "", url);
    getButton = getJoinButton;
    endPointURL = getToJoinURL();
    loadCurrentFilters();
    loadMatches();
});

$("#joined").click(function() {
    clearMatchs();
    putLoader();
    currentFilters = filters.joined;
    section = 'joined';
    var url = getURLFromFilters();
    window.history.pushState("", "", url);
    getButton = getCancelButton;
    endPointURL = getJoinedURL();
    loadCurrentFilters();
    loadMatches();
});

$("#created").click(function() {
    clearMatchs();
    putLoader();
    currentFilters = filters.created;
    section = 'created';
    var url = getURLFromFilters();
    window.history.pushState("", "", url);
    getButton = getDeleteButton;
    endPointURL = getCreatedURL();
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