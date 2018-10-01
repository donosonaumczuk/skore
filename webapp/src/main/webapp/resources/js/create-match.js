console.log("Starting to log...")
console.log($("#competitivity1").attr("checked"));
console.log($("#competitivity2").attr("checked"));
console.log($("#mode1").attr("checked"));
console.log($("#mode2").attr("checked"));
console.log("Finish log!")
console.log("\n\n\n\n");

if($("#competitivity1").attr("checked") == "checked") {
    $("#friendly").addClass("active");
    $("#friendly i").removeClass("d-none");
    $("#competitive i").addClass("d-none");
}
else if($("#competitivity2").attr("checked") == "checked") {
    $("#competitive").addClass("active");
    $("#competitive i").removeClass("d-none");
    $("#friendly i").addClass("d-none");
}

if($("#mode1").attr("checked") == "checked") {
    $("#individual").addClass("active");
    $("#individual i").removeClass("d-none");
    $("#team i").addClass("d-none");
    $('#collapseTeam').collapse('hide');
}
else if($("#mode2").attr("checked") == "checked") {
    $("#team").addClass("active");
    $("#team i").removeClass("d-none");
    $("#individual i").addClass("d-none");
    $('#collapseTeam').collapse('show');
}

$("#friendly").click(function() {
    $("#friendly i").removeClass("d-none");
    $("#competitive i").addClass("d-none");
});

$( "#competitive" ).click(function() {
    $("#competitive i").removeClass("d-none");
    $("#friendly i").addClass("d-none");
});

$( "#individual" ).click(function() {
    $("#individual i").removeClass("d-none");
    $("#team i").addClass("d-none");

    $('#collapseTeam').collapse('hide');
});

$( "#team" ).click(function() {
    $("#team i").removeClass("d-none");
    $("#individual i").addClass("d-none");

    $('#collapseTeam').collapse('show');
});

$(function () {
    $('#datepicker').datetimepicker({
        format: 'L',
        defaultDate: moment(getDateWithFormat(), "MM-DD-YYYY"),
        minDate: moment(getDateWithFormat(), "MM-DD-YYYY")
    });
});

$(function () {
    $('#timepicker-from').datetimepicker({
        format: 'HH:mm',
        defaultDate: moment("18:00", "HH:mm"),
    });
});

$(function () {
    $('#timepicker-to').datetimepicker({
        format: 'HH:mm',
        defaultDate: moment("00:20", "HH:mm"),
        minDate: moment("00:20", "HH:mm")
    });
});

function getTimePlusHalfOur() {
    var dt = new Date();
    dt.setMinutes(dt.getMinutes() + 30);
    return getTimeWithFormat(dt);
}

function getTimeWithFormat(date) {
    HH = date.getHours();
    mm = date.getMinutes();
    return HH + ':' + mm;
}

function getDateWithFormat() {
    var date = new Date();
    var dd = date.getDate();
    var mm = date.getMonth() + 1; //January is 0!
    var yyyy = date.getFullYear();

    if(dd < 10)
        dd = '0' + dd;

    if(mm < 10)
        mm = '0' + mm;

    return mm + '/' + dd + '/' + yyyy;
}