// $.post("/match/filter", {minStartTime: "peron", maxStartTime: "peron",
//         minFinishTime: "peron", maxFinishTime: "peron",
//         types: "Argentina", sportNames: "CABA", sportNames: "Pcia de Buenos Aires",
//         minQuantity: "1", maxQuantity: "1",
//         countries: "Argentina", states: "CABA",
//         cities: "Ituzaingo",
//         minFreePlaces: "1",
//         maxFreePlaces: "1"})
//     .done(function(data) {
//         console.log(data);
//     });



// $.ajax({
//     type: "POST",
//     url: "/game/filter",
//     data: {minStartTime: "peron", maxStartTime: "peron",
//         minFinishTime: "peron", maxFinishTime: "peron",
//         types: "Argentina", sportNames: "CABA", sportNames: "Pcia de Buenos Aires",
//         minQuantity: "1", maxQuantity: "1",
//         countries: "Argentina", states: "CABA",
//         cities: "Ituzaingo",
//         minFreePlaces: "1",
//         maxFreePlaces: "1"}
// })
//     .done(function(data) {
//         console.log(data);
//     });

console.log(
[ { "team2": {
        "players": [],
        "sport": {
            "quantity": 12,
            "name": "Football"
        },
        "temporal": false,
        "acronym": "C.A.M.B.I.E.M.O.S.",
        "leader": {
            "cellphone": null,
            "birthday": null,
            "reputation": 0,
            "friends": null,
            "likes": null,
            "userName": "Gato",
            "password": null,
            "notifications": null,
            "home": null,
            "userId": 1,
            "lastName": "Macri",
            "email": "gato@gmail.com",
            "firstName": "Mauricio"
        },
        "name": "Cambiemos"
    },
    "finishTime": {
        "dayOfWeek": "WEDNESDAY",
        "dayOfYear": 346,
        "monthValue": 12,
        "dayOfMonth": 12,
        "hour": 1,
        "minute": 0,
        "second": 0,
        "nano": 0,
        "year": 2018,
        "month": "DECEMBER",
        "chronology": {
            "calendarType": "iso8601",
            "id": "ISO"
        }
    },
    "team1": {
        "players": [],
        "sport": {
            "quantity": 12,
            "name": "Football"
        },
        "temporal": false,
        "acronym": "P.E.R.O.N.C.H.O",
        "leader": {
            "cellphone": null,
            "birthday": null,
            "reputation": 0,
            "friends": null,
            "likes": null,
            "userName": "ElGeneral",
            "password": null,
            "notifications": null,
            "home": null,
            "userId": 2,
            "lastName": "Peron",
            "email": "elgeneral@gmail.com",
            "firstName": "Juan Domingo"
        },
        "name": "Los Peronistas"
    },
    "place": {
        "city": "La Plata",
        "street": "Azaleaz 234",
        "state": "Buenos Aires",
        "country": "Argentina"
    },
    "quantityOccupiedPlaces": 15,
    "description": "Se define la precidencia para 2020",
    "startTime": {
        "dayOfWeek": "WEDNESDAY",
        "dayOfYear": 346,
        "monthValue": 12,
        "dayOfMonth": 12,
        "hour": 0,
        "minute": 0,
        "second": 0,
        "nano": 0,
        "year": 2018,
        "month": "DECEMBER",
        "chronology": {
            "calendarType": "iso8601",
            "id": "ISO"
        }
    },
    "type": "Competitivo",
    "result": "3-0"
} ]);