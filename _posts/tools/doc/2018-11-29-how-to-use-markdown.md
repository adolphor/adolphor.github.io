---
layout:     post
title:      来使用markdown吧
date:       2018-11-29 22:11:08 +0800
postId:     2018-11-29-22-11-08
categories: [Tools]
keywords:   [Tools]
---

markdown已经成为了程序员文档编辑的标配，也得到越来越多编辑器的支持，基础语法就不讲了，讲讲其他的吧。

## 图片插入

* 使用Github，千牛等图床
* 使用输入的快捷输入方式，指定快捷输入短语，比如图片的插入等固定格式的语句

## Jekyll 图片大小

```markdown
![HTTP vs HTTPS]({{ site.baseurl }}/image/post/2018/04/28/20180428-http-vs-https.jpg){: width="500" }
```

width="500"：

![HTTP vs HTTPS]({{ site.baseurl }}/image/post/2018/04/28/20180428-http-vs-https.jpg){: width="500" }

width="300"：

![HTTP vs HTTPS]({{ site.baseurl }}/image/post/2018/04/28/20180428-http-vs-https.jpg){: width="300" }

## 其他元素

如果在使用markdown不方便的情况下，可以使用如下的纯文本方式进行图形的绘制和描述

```
╔════════════════════════════════════════════════════════════════════════════╗
║ WARNING: your installation of Flutter is 40 days old.                      ║
║                                                                            ║
║ To update to the latest version, run "flutter upgrade".                    ║
╚════════════════════════════════════════════════════════════════════════════╝
```

## 段首缩进

段首缩进这件事，应该是 CSS 或者其他排版工具的事情，Markdown 奉行的是样式和内容分开的哲学。即使手动输入空格，&nbsp; 也是很不推荐的方法。
我推荐全角空格，切换到全角模式下（一般的中文输入法都是按 shift + space）输入两个空格就行了。这个相对 &nbsp; 来说稍微干净一点，而且宽度是整整两个汉字，很整齐。

如果是网页博客，可以通过添加如下样式实现：
```css
p {
  text-indent: 2em; /*首行缩进*/
}
```

## 参考

* [在 Markdown 语言中，如何实现段首空格的显示](https://www.zhihu.com/question/21420126/answer/18183047)
* [选择使用正确的 Markdown Parser](https://github.com/xitu/gold-miner/blob/master/TODO/choosing-right-markdown-parser.md)
* [markdown / markdown.github.com](https://github.com/markdown/markdown.github.com/wiki/Implementations)
* [实现一个markdown解析器需要具备哪些知识](https://www.zhihu.com/question/28756456)
