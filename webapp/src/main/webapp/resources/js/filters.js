var filters = {
    toJoin: {
        country: {},
        state: {},
        city: {}
    },
    joined: {
        country: {},
        state: {},
        city: {}
    },
    created: {
        country: {},
        state: {},
        city: {}
    }
};

$('.filter-badge').hover(function() {
        $(this).find('.fas').css("color", "grey");
        $(this).css("cursor", "pointer");
    },
    function() {
        $(this).find('.fas').css("color", "black");
        $(this).css("cursor", "auto");
    }
);

$('.filter-badge').click(function () {
    $(this).remove();
});

$(".filter-input").keyup(function (e) {
    if (e.keyCode == 13) {
        var value = $(this).val();

        if(value.length == 0)
            return;

        $(this).parent().append(getBadge(value));

        $('.filter-badge').hover(function() {
                $(this).find('.fas').css("color", "grey");
                $(this).css("cursor", "pointer");
            },
            function() {
                $(this).find('.fas').css("color", "black");
                $(this).css("cursor", "auto");
            }
        );

        $('.filter-badge').click(function () {
            $(this).remove();
        });

        $(this).val('');
    }
});

function getBadge(value) {
    var MAX_LENGTH = 14;
    var isLong = false;
    if(value.length > MAX_LENGTH)
        isLong = true;

    var title = '';

    if(isLong) {
        title += 'title="' + htmlspecialchars(value) + '"';
        value = htmlspecialchars(value.substr(0, MAX_LENGTH)) + '...';
    }

    return '<span class="badge badge-pill filter-badge m-1" ' + title + '>' + value + ' <i class="fas fa-times-circle"></i></span>';
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