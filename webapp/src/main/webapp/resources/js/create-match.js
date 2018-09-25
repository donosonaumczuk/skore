$( "#friendly" ).click(function() {
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
});

$( "#team" ).click(function() {
    $("#team i").removeClass("d-none");
    $("#individual i").addClass("d-none");
});

$('#collapseTeam').on('hide.bs.collapse', function () {
    $("#team").attr("data-toggle", "collapse");
    $("#individual").removeAttr("data-toggle");
})

$('#collapseTeam').on('show.bs.collapse', function () {
    $("#individual").attr("data-toggle", "collapse");
    $("#team").removeAttr("data-toggle");
})

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
        minDate: moment(getTimePlusHalfOur(), "HH:mm")
    });
});

$(function () {
    $('#timepicker-to').datetimepicker({
        format: 'HH:mm',
        minDate: moment(getTimePlusAnOur(), "HH:mm")
    });
});

function getTimePlusHalfOur() {
    var dt = new Date();
    dt.setMinutes(dt.getMinutes() + 30);
    return getTimeWithFormat(dt);
}

function getTimePlusAnOur() {
    var dt = new Date();
    dt.setHours(dt.getHours() + 1);
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