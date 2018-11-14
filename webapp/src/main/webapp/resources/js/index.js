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
    full: {
        "en": "FULL",
        "es": "COMPLETO"
    },
    friendlyTooltip: {
        "en": "<b>Friendly</b> does not requiere an account to join the match. Does not impact the winrate",
        "es": "<b>Amistoso</b> no requiere tener una cuenta para unirse. No impacta en el winrate"
    },
    competitiveTooltip: {
        "en": "<b>Competitive</b> requires an account to join the match. Impacts the winrate",
        "es": "<b>Competitivo</b> requiere tener una cuenta para unirse. Impacta en el winrate"
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
var reachEndOfPagination = false;
var getButton = getJoinButton;
var collapsed = true;

var width = $(window).width();
if(width < 768) {
    $('.sidepanel').addClass('collapse navbar-collapse');
}

$(window).resize(function() {
    var width = $(window).width();
    if(width < 768) {
        $('.sidepanel').addClass('collapse navbar-collapse');
    }
    else {
        $('.sidepanel').removeClass('collapse navbar-collapse');
    }
});

function navbarTogglerClick() {
    if(collapsed) {
        $(window).scrollTop($("body").offset().top);
        collapsed = false;
    }
    else {
        collapsed = true;
    }
}

$.ajax({
    type:   'POST',
    url:    contextPath + '/lang'
}).done(function(data) {
    lang = JSON.parse(data).lang;
    loadMatches();
});

$(document).ready(function(){
    $(".filter-input").keypress(function(e){
        var keyCode = e.which;

        /* 48-57 - (0-9)Numbers
            65-90 - (A-Z)
            97-122 - (a-z)
            8 - (backspace)
            32 - (space) */

        /* To avoid injection */
        if ( !( (keyCode >= 48 && keyCode <= 57)
                ||(keyCode >= 65 && keyCode <= 90)
                || (keyCode >= 97 && keyCode <= 122) )
            && keyCode != 8 && keyCode != 32) {
            e.preventDefault();
        }
    });

    $('[data-toggle="tooltip"]').tooltip();

    loadCurrentFilters();
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

    var sports = Object.keys(currentFilters.sport);
    for(var i = 0; i < sports.length; i++) {
        addBadge(sports[i], 'sport');
    }
}

$(".filter-input").keyup(function(e) {
    if (e.keyCode == 13) {
        applyFilter(this);
    }
});

function applyFilters() {
    var filterInputs = $(".filter-input");

    for(var i = 0; i < filterInputs.length; i++) {
        applyFilter(filterInputs[i]);
    }
}

function applyFilter(filterInput) {
    var value = $(filterInput).val();

    if(value.length == 0)
        return;

    var context = $(filterInput).parent().attr('id');
    var values = value.split(' ');
    var needToLoad = false;

    for(var i = 0; i < values.length; i++) {
        if(currentFilters[context][values[i]] == undefined) {
            if(!needToLoad) {
                pageNumber = 1;
                reachEndOfPagination = false;
                clearMatchs();
                putLoader();
                needToLoad = true;
            }

            addBadge(values[i], context);
            addFilterToURL(values[i], context);
            currentFilters[context][values[i]] = true;
        }
        else {
            var badge = $('#' + getBadgeId(htmlspecialchars(values[i]), context));
            badge.addClass('animated bounceIn').one('animationend oAnimationEnd mozAnimationEnd webkitAnimationEnd',
                function () {
                    badge.removeClass('animated bounceIn');
                });
        }

    }

    if(needToLoad) {
        loadMatches();
    }

    $(filterInput).val('');
}

function getURLFromFilters() {
    var url = "/" + section;
    var started = false;

    var countries = Object.keys(currentFilters.country);
    for(var i = 0; i < countries.length; i++) {
        if(started) {
            url += "&";
        }
        else {
            url += "?";
            started = true;
        }

        url += "country=" + countries[i];
    }

    var states = Object.keys(currentFilters.state);
    for(var i = 0; i < states.length; i++) {
        if(started) {
            url += "&";
        }
        else {
            url += "?";
            started = true;
        }

        url += "state=" + states[i];
    }

    var cities = Object.keys(currentFilters.city);
    for(var i = 0; i < cities.length; i++) {
        if(started) {
            url += "&";
        }
        else {
            url += "?";
            started = true;
        }

        url += "city=" + cities[i];
    }

    var sports = Object.keys(currentFilters.sport);
    for(var i = 0; i < sports.length; i++) {
        if(started) {
            url += "&";
        }
        else {
            url += "?";
            started = true;
        }

        url += "sport=" + sports[i];
    }

    return url;
}

function addFilterToURL(filter, context) {
    var currentURL = document.location;

    if(location.search == "") {
        currentURL += "?";
    }
    else {
        currentURL += "&";
    }

    window.history.replaceState("", "", currentURL + context + '=' + filter);
}

$(window).scroll(function() {
    if($(window).scrollTop() + $(window).height() == $(document).height()) {
        if(!reachEndOfPagination) {
            loadMatches();
        }
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
        reachEndOfPagination = false;
        putLoader();
        $(this).remove();
        delete currentFilters[context][value];
        var url = contextPath + getURLFromFilters();
        window.history.replaceState("", "", url);
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
        matchArray = JSON.parse(matchJSON);

        if(matchArray.length == 0)
            reachEndOfPagination = true;

        pageNumber++;

        removeLoader();

        for(var i = 0; i < matchArray.length; i++) {
            var matchCard = getMatchCard(matchArray[i]);
            $('.match-container').append(matchCard);
        }

        $('[data-toggle="tooltip"]').tooltip();
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
        'types: [], sports: ' + JSON.stringify(Object.keys(currentFilters['sport'])) + ',' +
        'minQuantity: "0", maxQuantity: "0",' +
        'countries: ' + JSON.stringify(Object.keys(currentFilters['country'])) + ',' +
        'states: ' + JSON.stringify(Object.keys(currentFilters['state'])) +',' +
        'cities: ' + JSON.stringify(Object.keys(currentFilters['city'])) + ',' +
        'minFreePlaces: "0",' +
        'maxFreePlaces: "0",' +
        'pageNumber: "' + pageNumber + '" }';
}

function timeStampFormat(time) {
    return time.year + "-" + ((time.monthValue > 9)? '':'0') + time.monthValue + "-" +
        ((time.dayOfMonth > 9)? '':'0') + time.dayOfMonth + " " + ((time.hour > 9)? '':'0') +
        time.hour + ":" + ((time.minute > 9)? '':'0') + time.minute + ":00";
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
                '<img class="img-fluid" src="' + contextPath + '/img/loader.gif">' +
            '</div>' +
        '</div>';
}

function getMatchURLKey(startTime, team1, finishTime) {
    var key = '' + startTime.year + ((startTime.monthValue > 9)? '':'0') + startTime.monthValue +
        ((startTime.dayOfMonth > 9)? '':'0') + startTime.dayOfMonth;
    key += ((startTime.hour > 9)? '':'0') + startTime.hour + ((startTime.minute > 9)? '':'0') + startTime.minute;
    key += team1.name;
    key += finishTime.year + ((finishTime.monthValue > 9)? '':'0') + finishTime.monthValue +
        ((finishTime.dayOfMonth > 9)? '':'0') + finishTime.dayOfMonth;
    key += ((finishTime.hour > 9)? '':'0') + finishTime.hour + ((finishTime.minute > 9)? '':'0') + finishTime.minute;

    return key;
}

function getDataFromKey(key) {
    var startYear = key.substring(0, 4);
    var startMonth = key.substring(4, 6);
    var startDay = key.substring(6, 8);
    var startHours = key.substring(8, 10);
    var startMin = key.substring(10, 12);
    var startTime = startYear + "-" + startMonth + "-" + startDay + " " + startHours + ":" + startMin + ":00";

    var n = key.length;
    var team1name = key.substring(12, n - 12);

    var finishMin = key.substring(n - 2, n);
    var finishHours = key.substring(n - 4, n - 2);
    var finishDay = key.substring(n - 6, n - 4);
    var finishMonth = key.substring(n - 8, n - 6);
    var finishYear = key.substring(n - 12, n - 8);
    var finishTime = finishYear + "-" + finishMonth + "-" + finishDay + " " + finishHours + ":" + finishMin + ":00";

    var data = new Array();

    data['startTime'] = startTime;
    data['team1name'] = team1name;
    data['finishTime'] = finishTime;

    return data;
}

function getMatchCard(match) {
    var startTime = match.startTime;
    var date = formatDate(startTime.dayOfWeek, startTime.monthValue, startTime.dayOfMonth, startTime.year);
    var finishTime = match.finishTime;
    var time = formatTime(startTime.hour, startTime.minute) + ' - ' + formatTime(finishTime.hour, finishTime.minute);
    var username = match.team1.leader.userName;
    var sport = match.team1.sport.displayName;
    var title = match.title;
    var availability = match.quantityOccupiedPlaces + ' / ' + (match.team1.sport.quantity * 2);
    var place = match.place;
    var location = getMatchLocation(place.street, place.city, place.state, place.country);
    var sportId = match.team1.sport.name;
    var matchKey = getMatchURLKey(startTime, match.team1, finishTime);

    var matchCard = '' +
        '<div class="row p-2 mt-2 match-card rounded-border" onclick="clickMatch(\'' + matchKey + '\')" id="' + matchKey + '">' +
        '<div class="col">' +
        '<div class="row mb-4">' +
        '<div class="col-2 col-sm-1 pl-0">' +
        '<img src="' + contextPath + '/profile/image/' + username + '" onclick="clickAvatar(\'' + username + '\'); event.stopPropagation();" class="user-avatar" alt="user-pic">' +
        '</div>' +
        '<div class="col-3 col-sm-4">' +
        '<div class="row">' +
        '<p class="name-label">' + title + '</p>' +
        '</div>' +
        '<div class="row">' +
        '<a class="username-label" href="' + contextPath + '/profile/' + username + '">@' + username + '</a>' +
        '</div>' +
        '</div>' +
        '<div class="col-2 col-sm-3">' +
        '<div class="container-fluid pt-2">' +
        '<div class="row">' +
        '<div class="col col-xl-4 mr-0 mt-1">' +
        '<img src="' + contextPath + '/sport/image/' + sportId + '" class="sport-img" alt="sport-pic">' +
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

function clickMatch(matchKey) {
    window.location.href = contextPath + "/match/" + matchKey;
}

function clickAvatar(username) {
    window.location.href = contextPath + "/profile/" + username;
}

function getMatchLocation(street, city, state, country) {
    matchLocation = "";
    putComma = false;

    if(street.length > 0 && street.charAt(street.length - 1) == " ") {
        street = street.substr(0, street.length - 1);
    }

    if(street.length > 0) {
        matchLocation += street;
        putComma = true;
    }

    if(city.length > 0 && city.charAt(city.length - 1) == " ") {
        city = city.substr(0, city.length - 1);
    }

    if(city.length > 0) {
        if(putComma) {
            matchLocation += ", ";
        }

        matchLocation += city;
        putComma = true;
    }

    if(state.length > 0 && state.charAt(state.length - 1) == " ") {
        state = state.substr(0, state.length - 1);
    }

    if(state.length > 0) {
        if(putComma) {
            matchLocation += ", ";
        }

        matchLocation += state;
        putComma = true;
    }

    if(country.length > 0 && country.charAt(country.length - 1) == " ") {
        country = country.substr(0, country.length - 1);
    }

    if(country.length > 0) {
        if(putComma) {
            matchLocation += ", ";
        }

        matchLocation += country;
    }

    return matchLocation;
}

function getTypeLabel(type) {
    if(isFriendlyMatch(type)) {
        return '<p><span class="friendly-icon mr-2 fas fa-handshake"></span>' + labelMap.types.friendly[lang] + '<span class="tooltip-icon ml-2 far fa-question-circle"  onclick="event.stopPropagation();" data-toggle="tooltip" data-placement="right" data-html="true" title="' + labelMap.friendlyTooltip[lang] + '"/></p>';
    }

    return '<p><span class="competitive-icon mr-2 fas fa-medal"></span>' + labelMap.types.competitive[lang] + '<span class="tooltip-icon ml-2 far fa-question-circle"  onclick="event.stopPropagation();" data-toggle="tooltip" data-placement="right" data-html="true" title="' + labelMap.competitiveTooltip[lang] + '"/></p>';
}

function getJoinButton(match) {
    if(match.quantityOccupiedPlaces == (match.team1.sport.quantity * 2)) {
        return '<p class="full-match"><i class="fas fa-check mr-1"></i>' + labelMap.full[lang] + '</p>';
    }

    var prefix = isFriendlyMatch(match.type)? '/joinMatch/' : '/joinCompetitiveMatch/';
    return '<a class="btn btn-green join-button" href="'+ contextPath + prefix + getMatchURLKey(match.startTime, match.team1, match.finishTime) +
        '" role="button"><i class="fas fa-plus mr-1"></i>' + labelMap.toJoin[lang] + '</a>';
}

function isFriendlyMatch(type) {
    if(type.split("-")[1] == "Friendly")
        return true;

    return false;
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