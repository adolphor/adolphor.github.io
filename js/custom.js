$(document).ready(function () {
    heightFunctions();
});

function heightFunctions() {
    var winHeight = $(window).height();
    if (winHeight) {
        $("#content").css("min-height", winHeight);
    }
}
