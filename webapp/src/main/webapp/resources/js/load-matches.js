var labelMap = {
    joinLabel: {
        "en": "JOIN",
        "es": "UNIRSE"
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
    }
};

var lang = 'en';
var pageNumber = 1;

$.ajax({
    type:   'POST',
    url:    '/lang'
}).done(function(data) {
        lang = JSON.parse(data).lang;
        loadMatches(pageNumber);
    });

function loadMatches(pageNumber) {
    $.ajax({
        type:   'POST',
        url:    '/filterMatch',
        data: { body: '{ minStartTime: "", maxStartTime: "",' +
            'minFinishTime: "", maxFinishTime: "",' +
            'types: [], sportNames: [],' +
            'minQuantity: "0", maxQuantity: "0",' +
            'countries: [], states: [],' +
            'cities: [],' +
            'minFreePlaces: "0",' +
            'maxFreePlaces: "0",' +
            'pageNumber: "' + pageNumber + '" }' }
    }).done(function(matchJSON) {
            console.log(matchJSON);
            matchArray = JSON.parse(matchJSON);
            $('#loader').remove();
            for(var i = 0; i < matchArray.length; i++) {
                    var matchCard = getMatchCard(matchArray[i]);
                    $('.match-container').append(matchCard);
            }
        });
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

function getMatchCard(match) {
    var startTime = match.startTime;
    var date = formatDate(startTime.dayOfWeek, startTime.monthValue, startTime.dayOfMonth, startTime.year);
    var finishTime = match.finishTime;
    var time =  formatTime(startTime.hour, startTime.minute) + ' - ' + formatTime(finishTime.hour, finishTime.minute);
    var creator = match.team1.leader;
    var username = creator.userName;
    var sport = match.team1.sport.name;
    var name = creator.firstName + ' ' + creator.lastName;
    var availability = match.quantityOccupiedPlaces + ' / ' + match.team1.sport.quantity;
    var place = match.place;
    var location = place.street + ', ' + place.city + ', ' + place.state + ', ' + place.country;
    var sportImg = 'football.svg';
    var avatar = 'user-default.svg';

    var matchCard = '' +
        '<div class="row p-2 mt-2 match-card rounded-border">' +
            '<div class="col">' +
                '<div class="row mb-4">' +
                    '<div class="col-2 col-sm-1 pl-0">' +
                        '<img src="/img/' + avatar + '" class="user-avatar" alt="user-pic">' +
                    '</div>' +
                    '<div class="col-3 col-sm-4">' +
                        '<div class="row">' +
                            '<p class="name-label">' + name + '</p>' +
                        '</div>' +
                        '<div class="row">' +
                            '<a class="username-label" href="/profile/' + username + '">@' + username + '</a>' +
                        '</div>' +
                    '</div>' +
                    '<div class="col-2 col-sm-3">' +
                        '<div class="container-fluid pt-2">' +
                            '<div class="row">' +
                                '<div class="col col-xl-4 mr-0 mt-1">' +
                                    '<img src="/img/' + sportImg + '" class="sport-img" alt="sport-pic">' +
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
                                '<button class="btn btn-green"><i class="fas fa-plus mr-1"></i>' + labelMap.joinLabel[lang] + '</button>' +
                            '</div>' +
                        '</div>' +
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