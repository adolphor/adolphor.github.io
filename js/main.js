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
    var section = '<section class="left-section "> <div class="row"><div class="left-title">' +
        ' 文章导航 </div> </div> <ul class="left-ul">';

    var height = $(".post-content").height();
    if (height > 800) {
        var pathname = window.location.pathname;
        var h2Array = $(".post-side h2");
        $.each(h2Array, function () {
            var h2Title = $(this).text();
            var h2Id = $(this).attr("id");
            var link = '<li><a href="' + pathname +'#'+ h2Id + '">' + h2Title + '</a></li>'
            section += link;
            console.log(link);

            var h3Array = $(this).nextUntil('h2').filter("h3");
            $.each(h3Array, function () {
                var h3Title = $(this).text();
                var h3Id = $(this).attr("id");
                var subLink = '<li><a href="' + pathname +'#'+ h3Id + '">' + h3Title + '</a></li>'
                section += subLink;
                console.log(subLink);
            });
        });
        section+=("</ul></section>");

       $(".left-side section:last-child").append(section);

    }
}