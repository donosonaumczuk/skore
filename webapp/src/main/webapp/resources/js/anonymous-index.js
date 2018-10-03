var filters = {
    minStartTime: null,
    maxStartTime: null,
    minFinishTime: null,
    maxFinishTime: null,
    types: [],
    sportNames: [],
    minQuantity: 0,
    maxQuantity: 0,
    country: {},
    state: {},
    city: {},
    minFreePlaces: 0,
    maxFreePlaces: 0
};

var currentFilters = filters;
var endPointURL = '/filterMatch';