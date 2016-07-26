$(document).ready(function () {
    heightFunctions();
});

function heightFunctions() {
    var winHeight = $(window).height();
    if (winHeight) {
        $("#sidebar,#contents").css("min-height", winHeight);
    }
}
