$('#create-match-btn').click( function() {
    window.location.href = '/createMatch';
});

var labelMap = {
    toJoin: {
        "en": "JOIN",
        "es": "UNIRSE"
    },
    joined: {
        "en": "CANCEL",
        "es": "CANCELAR"
    },
    created: {
        "en": "DELETE",
        "es": "ELIMINAR"
    },
    days: {
        "MONDAY": {
            "en": "Monday",
            "es": "Lunes"
        },
        "TUESDAY": {
            "en": "Tuesday",
            "es": "Martes"
        },
        "WEDNESDAY": {
            "en": "Wednesday",
            "es": "Miercoles"
        },
        "THURSDAY": {
            "en": "Thursday",
            "es": "Jueves"
        },
        "FRIDAY": {
            "en": "Friday",
            "es": "Viernes"
        },
        "SATURDAY": {
            "en": "Saturday",
            "es": "Sabado"
        },
        "SUNDAY": {
            "en": "Sunday",
            "es": "Domingo"
        }
    },
    months: {
        1: {
            "en": "January",
            "es": "Enero"
        },
        2: {
            "en": "February",
            "es": "Febrero"
        },
        3: {
            "en": "March",
            "es": "Marzo"
        },
        4: {
            "en": "April",
            "es": "Abril"
        },
        5: {
            "en": "May",
            "es": "Mayo"
        },
        6: {
            "en": "June",
            "es": "Junio"
        },
        7: {
            "en": "July",
            "es": "Julio"
        },
        8: {
            "en": "August",
            "es": "Agosto"
        },
        9: {
            "en": "September",
            "es": "Septiembre"
        },
        10: {
            "en": "October",
            "es": "Octubre"
        },
        11: {
            "en": "November",
            "es": "Noviembre"
        },
        12: {
            "en": "December",
            "es": "Diciembre"
        }
    },
    types: {
        friendly: {
            "en": "Friendly",
            "es": "Amistoso"
        },
        competitive: {
            "en": "Competitive",
            "es": "Competitivo"
        }
    }
};

var lang = 'en';
var pageNumber = 1;
var areMoreMatchesToGet = true;
var getButton = getJoinButton;
var postAppendMatch = joinButtonPostAppendMatch;

$.ajax({
    type:   'POST',
    url:    '/lang'
}).done(function(data) {
    lang = JSON.parse(data).lang;
    loadMatches();
});

$(".filter-input").keyup(function (e) {
    if (e.keyCode == 13) {
        var value = $(this).val();

        if(value.length == 0)
            return;

        var context = $(this).parent().attr('id');

        if(currentFilters[context][value] == undefined) {
            pageNumber = 1;
            clearMatchs();
            putLoader();
            addBadge(value, context);
            currentFilters[context][value] = true;
            loadMatches();
        }
        else {
            var badge = $('#' + getBadgeId(htmlspecialchars(value), context));
            badge.addClass('animated bounceIn').one('animationend oAnimationEnd mozAnimationEnd webkitAnimationEnd',
                function () {
                    badge.removeClass('animated bounceIn');
                })
        }

        $(this).val('');
    }
});

function addBadge(value, context) {
    $('#' + context).append(getBadge(value, context));

    var badge = $('#' + getBadgeId(value, context));

    badge.hover(function() {
            $(this).find('.fas').css("color", "grey");
            $(this).css("cursor", "pointer");
        },
        function() {
            $(this).find('.fas').css("color", "black");
            $(this).css("cursor", "auto");
        }
    );

    badge.click(function () {
        clearMatchs();
        pageNumber = 1;
        putLoader();
        $(this).remove();
        delete currentFilters[context][value];
        loadMatches();
    });
}

function getBadgeId(value, context) {
    return 'badge-' + context + '-' + htmlspecialchars(value);
}

function loadMatches() {
    $.ajax({
        type:   'POST',
        url:    endPointURL,
        data: { body: getMatchRequestBody() }
    }).done(function(matchJSON) {
        pageNumber++;

        matchArray = JSON.parse(matchJSON);

        console.log(matchArray);

        removeLoader();

        for(var i = 0; i < matchArray.length; i++) {
            var matchCard = getMatchCard(matchArray[i]);
            $('.match-container').append(matchCard);
            postAppendMatch(matchArray[i]);
        }
    });
}

function removeLoader() {
    $('#loader').remove();
}

function putLoader() {
    $('.match-container').append(getLoader());
}

function clearMatchs() {
    $('.match-container').empty();
}

function getMatchRequestBody() {
    return '{ minStartTime: "", maxStartTime: "",' +
        'minFinishTime: "", maxFinishTime: "",' +
        'types: [], sportNames: [],' +
        'minQuantity: "0", maxQuantity: "0",' +
        'countries: ' + JSON.stringify(Object.keys(currentFilters['country'])) +
        ', states: ' + JSON.stringify(Object.keys(currentFilters['state'])) +',' +
        'cities: ' + JSON.stringify(Object.keys(currentFilters['city'])) + ',' +
        'minFreePlaces: "0",' +
        'maxFreePlaces: "0",' +
        'pageNumber: "' + pageNumber + '" }';
}

function formatTime(hours, minutes) {
    if(hours < 10)
        hours = '0' + hours;

    if(minutes < 10)
        minutes = '0' + minutes;

    return hours + ":" + minutes;
}

function formatDate(weekDay, month, day, year) {
    if(lang == 'es') {
        return labelMap.days[weekDay]['es'] + ', ' + day + ' de ' + labelMap.months[month]['es'] + ', ' + year;
    }
    else { /* Otherwise, default language: English (en) */
        return labelMap.days[weekDay]['en'] + ', ' + labelMap.months[month]['en'] + ' ' + day + ', ' + year;
    }
}

function getLoader() {
    return '' +
        '<div class="row p-2 mt-2" id="loader">' +
            '<div class="offset-5 col-2">' +
                '<img class="img-fluid" src="/img/loader.gif">' +
            '</div>' +
        '</div>';

}
function getMatchURLKey(startTime, team1, finishTime) {
    var key = '' + startTime.year + ((startTime.monthValue > 9)? '':'0') + startTime.monthValue +
        ((startTime.dayOfMonth > 9)? '':'0') + startTime.dayOfMonth;
    key += ((startTime.hour > 9)? '':'0') + ((startTime.minute > 9)? '':'0');
    key += team1.name;
    key += finishTime.year + ((finishTime.monthValue > 9)? '':'0') + finishTime.monthValue +
        ((finishTime.dayOfMonth > 9)? '':'0') + finishTime.dayOfMonth;
    key += ((finishTime.hour > 9)? '':'0') + ((finishTime.minute > 9)? '':'0');

    return key;
}

function getMatchCard(match) {
    var startTime = match.startTime;
    var date = formatDate(startTime.dayOfWeek, startTime.monthValue, startTime.dayOfMonth, startTime.year);
    var finishTime = match.finishTime;
    var time = formatTime(startTime.hour, startTime.minute) + ' - ' + formatTime(finishTime.hour, finishTime.minute);
    var username = match.team1.leaderUserName;
    var sport = match.team1.sport.displayName;
    var title = match.title;
    var availability = match.quantityOccupiedPlaces + ' / ' + (match.team1.sport.quantity * 2);
    var place = match.place;
    var location = place.street + ', ' + place.city + ', ' + place.state + ', ' + place.country;

    var sportId = match.team1.sport.name;

    var matchCard = '' +
        '<div class="row p-2 mt-2 match-card rounded-border">' +
        '<div class="col">' +
        '<div class="row mb-4">' +
        '<div class="col-2 col-sm-1 pl-0">' +
        '<img src="/profile/image/' + username + '" class="user-avatar" alt="user-pic">' +
        '</div>' +
        '<div class="col-3 col-sm-4">' +
        '<div class="row">' +
        '<p class="name-label">' + title + '</p>' +
        '</div>' +
        '<div class="row">' +
        '<a class="username-label" href="/profile/' + username + '">@' + username + '</a>' +
        '</div>' +
        '</div>' +
        '<div class="col-2 col-sm-3">' +
        '<div class="container-fluid pt-2">' +
        '<div class="row">' +
        '<div class="col col-xl-4 mr-0 mt-1">' +
        '<img src="/sport/image/' + sportId + '" class="sport-img" alt="sport-pic">' +
        '</div>' +
        '<div class="col-6 col-xl d-none d-sm-block pl-0">' +
        '<p class="sport-label">' + sport + '</p>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '<div class="offset-1 col-4 col-sm-3">' +
        '<div class="row text-center">' +
        '<div class="col">' +
        '<i class="name-label fas fa-users mr-2"></i>' + availability +
        '</div>' +
        '</div>' +
        '<div class="row text-center">' +
        '<div class="col mt-xl-2 ml-xl-4">' +
        getButton(match) +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '<div class="row">' +
        '<div class="col">' +
        getTypeLabel(match.type) +
        '</div>' +
        '</div>' +
        '<div class="row">' +
        '<div class="col">' +
        '<p><span class="calendar-icon mr-2 fas fa-calendar-alt"></span>' + date + '</p>' +
        '</div>' +
        '</div>' +
        '<div class="row">' +
        '<div class="col">' +
        '<p><span class="name-label mr-2 fas fa-clock"></span>' + time + '</p>' +
        '</div>' +
        '</div>' +
        '<div class="row">' +
        '<div class="col">' +
        '<p><span class="location-icon mr-2 fas fa-map-marker-alt"></span>' + location + '</p>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>';
    return matchCard;
}

function getTypeLabel(type) {
    if(isFriendlyMatch(type)) {
        return '<p><span class="friendly-icon mr-2 fas fa-handshake"></span>' + labelMap.types.friendly[lang] + '</p>';
    }

    return '<p><span class="competitive-icon mr-2 fas fa-medal"></span>' + labelMap.types.competitive[lang] + '</p>';
}

function getJoinButton(match) {
    console.log(isFriendlyMatch(match.type));
    console.log(isFriendlyMatch(match.type)? '/joinMatch/' : '/joinCompetitiveMatch/');
    var prefix = isFriendlyMatch(match.type)? '/joinMatch/' : '/joinCompetitiveMatch/';
    return '<a class="btn btn-green" href="'+ prefix + getMatchURLKey(match.startTime, match.team1, match.finishTime) +
        '" role="button"><i class="fas fa-plus mr-1"></i>' + labelMap.toJoin[lang] + '</a>';
}

function isFriendlyMatch(type) {
    console.log(type.split("-")[1]);
    if(type.split("-")[1] == "Friendly")
        return true;

    return false;
}

function joinButtonPostAppendMatch(match) {
    /* Do nothing... */
    return;
}

function getBadge(value, context) {
    var MAX_LENGTH = 14;
    var isLong = false;
    var escapedValue = htmlspecialchars(value);

    if(value.length > MAX_LENGTH)
        isLong = true;

    var title = '';

    if(isLong) {
        title += 'title="' + escapedValue + '"';
        value = htmlspecialchars(value.substr(0, MAX_LENGTH)) + '...';
    }

    return '<span class="badge badge-pill filter-badge m-1" id="' + getBadgeId(escapedValue, context) + '" ' + title +
        '>' + value + ' <i class="fas fa-times-circle"></i></span>';
}

function htmlspecialchars(value) {
    if (typeof(value) == "string") {
        value = value.replace(/&/g, '&amp;');
        value = value.replace(/"/g, '&quot;');
        value = value.replace(/'/g, '&#039;');
        value = value.replace(/</g, '&lt;');
        value = value.replace(/>/g, '&gt;');
    }

    return value;
}