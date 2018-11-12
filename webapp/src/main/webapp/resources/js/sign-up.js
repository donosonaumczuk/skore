
// This example displays an address form, using the autocomplete feature
// of the Google Places API to help users fill in the information.

// This example requires the Places library. Include the libraries=places
// parameter when you first load the API. For example:
// <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places">

var placeSearch, autocomplete;
var componentForm = {
    route: 'short_name',
    locality: 'long_name',
    administrative_area_level_1: 'short_name',
    country: 'long_name'
};

function initAutocomplete() {
    // Create the autocomplete object, restricting the search to geographical
    // location types.
    autocomplete = new google.maps.places.Autocomplete(
        /** @type {!HTMLInputElement} */(document.getElementById('autocomplete')),
        {types: ['address']});

    // When the user selects an address from the dropdown, populate the address
    // fields in the form.
    autocomplete.addListener('place_changed', fillInAddress);
}

function fillInAddress() {
    // Get the place details from the autocomplete object.
    var place = autocomplete.getPlace();

    for (var component in componentForm) {
        document.getElementById(component).value = '';
        document.getElementById(component).disabled = false;
    }

    // Get each component of the address from the place details
    // and fill the corresponding field on the form.
    for (var i = 0; i < place.address_components.length; i++) {
        var addressType = place.address_components[i].types[0];
        if (componentForm[addressType]) {
            var val = place.address_components[i][componentForm[addressType]];
            document.getElementById(addressType).value = val;
        }
    }

    // Custom by skore: try to set sublocality if do not have administrative_area_level_1
    if(document.getElementById('locality').value.toString() == '') {
        for(var i = 0; i < place.address_components.length; i++) {
            if(place.address_components[i].types.length > 1 && place.address_components[i].types[1].toString() == 'sublocality') {
                document.getElementById('locality').value = place.address_components[i]['long_name'];
            }
        }
    }
}

// Bias the autocomplete object to the user's geographical location,
// as supplied by the browser's 'navigator.geolocation' object.
function geolocate() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var geolocation = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };
            var circle = new google.maps.Circle({
                center: geolocation,
                radius: position.coords.accuracy
            });
            autocomplete.setBounds(circle.getBounds());
        });
    }
}

$(function () {
    $('#datepicker').datetimepicker({
        format: 'L',
        defaultDate: moment(getDateWithFormat(), "MM-DD-YYYY"),
        //maxDate: moment(getDateWithFormat(), "MM-DD-YYYY") /* This broke spring reload form data */
    });
});

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

function showname () {
    var name = document.getElementById('image');
    $('#fileLabel').append(name.files.item(0).name);

};