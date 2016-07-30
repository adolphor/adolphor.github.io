$(document).ready(function () {
    heightFunctions();
});

function heightFunctions() {
    var winHeight = $(window).height();
    if (winHeight) {
        $("#pageContent").css("min-height", winHeight);
    }

    var sideHight = $("#sidebar").height();
    var contHight = $("#content").height();
    var height = (sideHight > contHight) ? sideHight : contHight;
    // $("#sidebar, #content").css("min-height", height);

}
