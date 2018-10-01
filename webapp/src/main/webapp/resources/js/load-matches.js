var labelMap = {
    joinLabel: {
        "en": "JOIN",
        "es": "UNIRSE"
    },
    days: {
        "WEDNESDAY": {
            "en": "Wednesday",
            "es": "Miercoles"
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
    var matchJSON = '[{"team2":{"players":[],"sport":{"quantity":12,"name":"Football"},"temporal":false,"acronym":"BFC","leader":{"cellphone":null,"birthday":null,"reputation":0,"friends":null,"likes":null,"userName":"jgarcia","password":null,"notifications":null,"home":null,"userId":1,"lastName":"Garcia","email":"jgarcia@gmail.com","firstName":"Juan"},"name":"Barcelona FC"},"finishTime":{"dayOfWeek":"WEDNESDAY","dayOfYear":346,"monthValue":12,"dayOfMonth":12,"hour":1,"minute":0,"second":0,"nano":0,"year":2018,"month":"DECEMBER","chronology":{"calendarType":"iso8601","id":"ISO"}},"team1":{"players":[],"sport":{"quantity":12,"name":"Football"},"temporal":false,"acronym":"CABJ","leader":{"cellphone":null,"birthday":null,"reputation":0,"friends":null,"likes":null,"userName":"donosonaumczuk","password":null,"notifications":null,"home":null,"userId":2,"lastName":"Donoso Naumczuk","email":"donosonaumczuk@gmail.com","firstName":"Alan"},"name":"Boca Juniors"},"place":{"city":"La Plata","street":"Azaleaz 234","state":"Buenos Aires","country":"Argentina"},"quantityOccupiedPlaces":15,"description":"","startTime":{"dayOfWeek":"WEDNESDAY","dayOfYear":346,"monthValue":12,"dayOfMonth":12,"hour":0,"minute":0,"second":0,"nano":0,"year":2018,"month":"DECEMBER","chronology":{"calendarType":"iso8601","id":"ISO"}},"type":"Competitivo","result":"3-0"},{"team2":null,"finishTime":{"dayOfWeek":"WEDNESDAY","dayOfYear":346,"monthValue":12,"dayOfMonth":12,"hour":1,"minute":0,"second":0,"nano":0,"year":2018,"month":"DECEMBER","chronology":{"calendarType":"iso8601","id":"ISO"}},"team1":{"players":[],"sport":{"quantity":12,"name":"Football"},"temporal":false,"acronym":"ASDD","leader":{"cellphone":null,"birthday":null,"reputation":0,"friends":null,"likes":null,"userName":"docker69","password":null,"notifications":null,"home":null,"userId":2,"lastName":"Docker","email":"docker@gmail.com","firstName":"Package"},"name":"Package Team"},"place":{"city":"La Plata","street":"Azaleaz 234","state":"Buenos Aires","country":"Argentina"},"quantityOccupiedPlaces":15,"description":"xD","startTime":{"dayOfWeek":"WEDNESDAY","dayOfYear":346,"monthValue":12,"dayOfMonth":12,"hour":0,"minute":0,"second":0,"nano":0,"year":2018,"month":"DECEMBER","chronology":{"calendarType":"iso8601","id":"ISO"}},"type":"Competitivo","result":"3-0"}]';

    // $.ajax({
    //     type:   'POST',
    //     url:    '/filterMatch',
    //     data: {body: '{minStartTime: "", maxStartTime: "",'+
    //         'minFinishTime: "", maxFinishTime: "",'+
    //         'types: ["Argentina", "CABA"], sportNames: ["Pcia de Buenos Aires"],'+
    //         'minQuantity: "1", maxQuantity: "1",'+
    //         'countries: ["Argentina"], states: ["CABA"],'+
    //         'cities: ["Ituzaingo"],'+
    //         'minFreePlaces: "1",'+
    //         'maxFreePlaces: "1",'+
    //         'pageNumber: "1"}'}
    // }).done(function(matchJSON) {
    //         matchArray = JSON.parse(matchJSON);
    //         for(var i = 0; i < matchArray.length; i++) {
    //             var matchCard = getMatchCard(matchArray[i]);
    //             $('.match-container').append(matchCard);
    //         }
    //     });

    matchArray = JSON.parse(matchJSON);
    for(var i = 0; i < matchArray.length; i++) {
        var matchCard = getMatchCard(matchArray[i]);
        $('.match-container').append(matchCard);
    }
}

function getMatchCard(match) {
    var startTime = match.startTime;
    var date = startTime.monthValue + '/' + startTime.dayOfMonth + '/' + startTime.year;
    var finishTime = match.finishTime;
    var time =  startTime.hour + ':' + startTime.minute + ' - ' + finishTime.hour + ':' + finishTime.minute;
    var creator = match.team1.leader;
    var username = creator.userName;
    var sport = match.team1.sport.name;
    var name = creator.firstName + ' ' + creator.lastName;
    var availability = match.quantityOccupiedPlaces + ' / ' + match.team1.sport.quantity;
    var place = match.place;
    var location = place.street + ', ' + place.city + ', ' + place.state + ', ' + place.country;
    var sportImg = 'football.svg';

    var matchCard = '' +
        '<div class="row p-2 mt-2 match-card rounded-border">' +
            '<div class="col">' +
                '<div class="row mb-4">' +
                    '<div class="col-2 col-sm-1 pl-0">' +
                        '<img src="' + 'img/user-default.svg' + '" class="user-avatar" alt="user-pic">' +
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
                                    '<img src="img/' + sportImg + '" class="sport-img" alt="sport-pic">' +
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