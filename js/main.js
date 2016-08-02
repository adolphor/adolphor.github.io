$(document).ready(function () {
    heightFunctions();
    geneMenu();
});

function heightFunctions() {
    var winHeight = $(window).height();
    if (winHeight) {
        $("#pageContent").css("min-height", winHeight);
    }
}

// 生成左侧文章结构目录
function geneMenu() {
    var height = $(".post-content").height();
    if (height > 800) {
        var h2Array = $(".post-side h2");
        $.each(h2Array, function () {
            var h2 = $(this).text();
            console.log(h2);

            var h3Array = $(this).nextUntil('h2').filter("h3");
            $.each(h3Array, function () {
                var h3 = $(this).text();
                console.log(h3);
            });
        });
    }
}