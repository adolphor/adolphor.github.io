---
layout: page
title:  "在GitHub上使用markdown语法"
date:   2015-10-18 11:45:00
categories: blog markdown
---

## markdown简介
markdown是为了让html元素的书写和阅读更加方便，并不是为了取代html，因为使用markdown书写的文件，经过编译器
(比如：)编译之后，产生的就是原生可供

## 标题

## 代码块

{% highlight html linenos %}
{% highlight java linenos %}
asdfasd 
{% endhighlight %}
{% endhighlight %}


## 图片

### markdown图片引用方法：

markdown中支持两种形式来引入图片，`inline` 和 `reference`

* 第一种形式：`inline`

{% highlight html %}
![Alt text](/path/to/img.jpg)
![Alt text](/path/to/img.jpg "Optional title")
{% endhighlight %}


<h3>在GitHub中引用图片</h3>
图片链接可以是图片的相对路径的引用，也可以是图片绝对路径的引用，所以，使用GitHub托管的网站，如果要引用图片，有两种方式：
* 第一种方式，使用相对路径

![img]({{ '/assets/images/other/feathers01.jpg' }} "title")
test2
<img src="{{ "/assets/images/other/feathers01.jpg" }}" alt="贴片功能" title="贴片功能" />


## 参考文献
* 官方语法文档 [markdown](http://daringfireball.net/projects/markdown/syntax)
* 在线编辑器 [stackedit.io/editor](https://stackedit.io/editor)



