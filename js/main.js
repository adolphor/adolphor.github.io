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
    var section = '<section id="articleMenu" class="left-section "> <div class="row"><div class="left-title">' +
        ' 文章导航 </div> </div> <ul class="left-ul">';

    var height = $(".post-content").height();
    if (height > 800) {
        var pathname = window.location.pathname;
        var h2Array = $(".post-side h2");
        $.each(h2Array, function () {
            var h2Title = $(this).text();
            var h2Id = $(this).attr("id");
            var link = '<li><a href="' + pathname + '#' + h2Id + '">' + h2Title + '</a></li>'
            section += link;

            var h3Array = $(this).nextUntil('h2').filter("h3");
            $.each(h3Array, function () {
                var h3Title = $(this).text();
                var h3Id = $(this).attr("id");
                var subLink = '<li><a href="' + pathname + '#' + h3Id + '">' + h3Title + '</a></li>'
                section += subLink;
            });
        });
        section += ("</ul></section>");

        $(".menu-side section:last-child").append(section);

        var position = $('#articleMenu').offset();
        $(window).scroll(function() {
            // 滚动鼠标的时候进行高度矫正
            var sideHight = $(".menu-side").height();
            var contHight = $(".post-side").height();
            var height = (sideHight > contHight) ? sideHight : contHight;
            $(".menu-side, .post-side").css("min-height", height);

            // 定位菜单位置
            if($(window).scrollTop() > position.top) {
                $('#articleMenu').css('position','fixed').css('top','0');
            } else {
                $('#articleMenu').css('position','static');
            }
        });
    }


}

