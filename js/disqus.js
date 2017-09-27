var disqus_config = function () {
  this.page.url = pageUrl;  // Replace PAGE_URL with your page's canonical URL variable
  this.page.identifier = pagePostId; // Replace PAGE_IDENTIFIER with your page's unique identifier variable
};

(function () {
  if (document.getElementById("disqus_thread")) { // 只有disqus节点存在才初始化对话框
    var d = document, s = d.createElement('script');
    s.src = '//' + disqusId + '.disqus.com/embed.js';
    s.setAttribute('data-timestamp', +new Date());
    (d.head || d.body).appendChild(s);
  }
})();
