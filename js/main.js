$(document).ready(function () {
  heightFunctions();
  geneMenu();
  heightResize();

  // 如果调整
  $(window).resize(function () {
    menuResize();
  });

});

var position;

function heightFunctions() {
  var winHeight = $(window).height();
  if (winHeight) {
    $("#pageContent").css("min-height", winHeight);
  }
}

// 生成左侧文章结构目录
function geneMenu() {
  var height = $(".post-content").height();
  var isGene = $("#geneMenu").text();
  var len = $(".post-side h2").length + $(".post-side h3").length;
  if (isGene == "true" && len > 4 && height > 800) {  // 文章导航生成条件
    var section = '<section id="articleMenu" class="left-section row"><div class="col-md-12"><div class="left-title row">' +
      ' 文章导航 </div> <ul class="left-ul row menu-ul">';
    var pathname = window.location.pathname;
    var h2Array = $(".post-side h2");
    $.each(h2Array, function () {
      var h2Title = $(this).text();
      var h2Id = $(this).attr("id");
      var link = '<li class="menu-li" title="' + h2Title + '"><a href="' + pathname + '#' + h2Id + '">' + h2Title + '</a></li>'
      section += link;

      var h3Array = $(this).nextUntil('h2').filter("h3");
      section += "<ul class='sub-menu-ul'>";
      $.each(h3Array, function () {
        var h3Title = $(this).text();
        var h3Id = $(this).attr("id");
        var subLink = '<li class="sub-menu-li" title="' + h3Title + '"><a href="' + pathname + '#' + h3Id + '">' + h3Title + '</a></li>'
        section += subLink;
      });
      section += "</ul>";
    })
    section += ("</ul></div></section>");
    $(".menu-side").append(section);

    menuResize();

    $(window).scroll(function () {
      // 定位菜单位置
      if ($(window).scrollTop() > position.top) {
        $('#articleMenu').css('position', 'fixed').css('top', '0');
      } else {
        $('#articleMenu').css('position', 'static');
      }
      // BUG fix：如果有加载图片，会导致菜单栏高度不够，所以滚动鼠标的时候进行重新计算
      menuResize();
    });
  }

}

function menuResize() {
  var windowHeight = $(window).height();
  var width = $("aside section:first").width();
  $("#articleMenu").css("width", width);
  $("#articleMenu ul:first").css("overflow-y", "auto")
    .css("height", (windowHeight - 110))
    .css("width", (width - 18));

  // bugFix:只需要赋值一次就可以了
  if (!position) {
    position = $('#articleMenu').offset();
  }

}

function heightResize() {
  var sideHeight = $(".menu-side").height();
  var contHeight = $(".post-side").height();
  var height = Math.max(sideHeight, contHeight);
  $(".menu-side, .post-side").css("min-height", height + 260);
}