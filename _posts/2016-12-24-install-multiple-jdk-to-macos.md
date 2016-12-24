---
layout:     post
title:      macOS上安装多个版本的JDK
date:       2016-12-24 17:31:08 +0800
postId:     2016-12-24-17-31-08
categories: [Java]
tags:       [Java, JVM, Java8]
geneMenu:   true
excerpt:    macOS上安装多个版本的JDK
---

## 说明

从macOS 10.12开始，Apple公司已经已经不再对JDK6提供技术支持，
而Oracle公司只提供了1.7以及1.8版本的JDK，所以本文主要安装这两个版本。

* 系统版本：10.12.2 (16C67)
* JDK版本：
  * 1.7：1.7.0_80
  * 1.8：1.8.0_111


## 安装
从官网JDK归档页面可以找到Oracle提供的所有版本的JDK：
http://www.oracle.com/technetwork/java/archive-139210.html。
下载macOS版本的1.7和1.8，按照正常的安装方法进行安装即可。

## 配置
安装完成之后，调出terminal命令行工具，检验安装是否成功：

{% highlight shell %}
$ java -version
{% endhighlight %}

```
java version "1.8.0_111"
Java(TM) SE Runtime Environment (build 1.8.0_111-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.111-b14, mixed mode)
```

那如何进行版本切换呢？先看当前生效的JDK路径：
{% highlight shell %}
$ which java
{% endhighlight %}

```
/usr/bin/java
```

{% highlight shell %}
$ cd /usr/bin
$ ls -l
{% endhighlight %}

```
……
lrwxr-xr-x   1 root   wheel        74 Oct 21 18:10 java -> /System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/java
lrwxr-xr-x   1 root   wheel        75 Oct 21 18:10 javac -> /System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/javac
lrwxr-xr-x   1 root   wheel        77 Oct 21 18:10 javadoc -> /System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/javadoc
……
```

可以发现这个java只是一个当前系统生效的JDK的一个软连接，而使用如下的命令可以指定当前系统使用哪个版本的JDK：

{% highlight shell %}
$ /usr/libexec/java_home -v 版本号
{% endhighlight %}

编辑`bash_profile`文件，添加如下内容：

{% highlight shell %}
$ vi ~/.bash_profile

export JAVA_7_HOME="$(/usr/libexec/java_home -v 1.7)"
export JAVA_8_HOME="$(/usr/libexec/java_home -v 1.8)"

alias jdk8='export JAVA_HOME=$JAVA_8_HOME'
alias jdk7='export JAVA_HOME=$JAVA_7_HOME'

# 默认使用JDK7
export JAVA_HOME=$JAVA_7_HOME

$ source ~/.bash_profile
{% endhighlight %}

## 切换使用方法
如上配置之后，默认使用JDK1.7，然后使用`jdk7`和`jdk8`来切换当前系统的JDK版本：
{% highlight shell %}
$ java -version
{% endhighlight %}

```
java version "1.7.0_80"
Java(TM) SE Runtime Environment (build 1.7.0_80-b15)
Java HotSpot(TM) 64-Bit Server VM (build 24.80-b11, mixed mode)
```

{% highlight shell %}
# 切换到1.8，使用如下命令
$ jdk8
$ java -version
{% endhighlight %}

```
java version "1.8.0_111"
Java(TM) SE Runtime Environment (build 1.8.0_111-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.111-b14, mixed mode)
```

{% highlight shell %}
# 再切回1.7
$ jdk7
$ java -version
{% endhighlight %}

```
java version "1.7.0_80"
Java(TM) SE Runtime Environment (build 1.7.0_80-b15)
Java HotSpot(TM) 64-Bit Server VM (build 24.80-b11, mixed mode)
```

## 参考资料

* [Multiple JDK in Mac OSX 10.10 Yosemite](http://abetobing.com/blog/multiple-jdk-mac-osx-10-10-yosemite-88.html)
* [Mac下同时安装多个版本的JDK](http://www.tuicool.com/articles/uUJjEb)

