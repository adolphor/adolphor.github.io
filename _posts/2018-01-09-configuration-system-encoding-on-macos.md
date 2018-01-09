---
layout:     post
title:      macOS下的系统编码设置
date:       2018-01-09 13:30:51 +0800
postId:     2018-01-09-13-30-51
categories: [blog]
tags:       [macOS]
geneMenu:   true
excerpt:    macOS下的系统编码设置
---

macOS下，当系统语言是中文的时候，系统编码信息如下：
```
$ locale
LANG="zh_CN.UTF-8"
LC_COLLATE="zh_CN.UTF-8"
LC_CTYPE="zh_CN.UTF-8"
LC_MESSAGES="zh_CN.UTF-8"
LC_MONETARY="zh_CN.UTF-8"
LC_NUMERIC="zh_CN.UTF-8"
LC_TIME="zh_CN.UTF-8"
LC_ALL=
```

当将系统语言设置为英文之后，系统编码信息如下：
```
$ locale
LANG=
LC_COLLATE="C"
LC_CTYPE="UTF-8"
LC_MESSAGES="C"
LC_MONETARY="C"
LC_NUMERIC="C"
LC_TIME="C"
LC_ALL=
```

在这种情况下，编译Java代码的时候，即便在maven中添加如下编码设置依然会出现乱码的情况：
```
<properties>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
  <maven.compiler.encoding>UTF-8</maven.compiler.encoding>
</properties>

<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <version>3.0</version>
  <configuration>
    <source>1.7</source>
    <target>1.7</target>
    <encoding>${project.build.sourceEncoding}</encoding>
  </configuration>
</plugin>
```

具体表现为，系统日志中可以正常显示中文，系统API接口调用也是中文，但是当系统服务调用第三方接口服务的时候（比如
调用短信接口、调用微信公众号消息推送接口等）就会出现乱码，在这种情况下，逐渐排查到可能是因为更改系统语言导致的
编码格式的改变，也就是上面显示的系统编码信息，那么可以进行如下设置，可以保证在英文状态下设置具体的编码语言：

```
$ vim ~/.bash_profile
export LC_ALL=en_US.UTF-8  
export LANG=en_US.UTF-8
```

重新打开terminal或者重启系统之后，可以看到配置生效之后的信息：

```
$ locale
LANG="en_US.UTF-8"
LC_COLLATE="en_US.UTF-8"
LC_CTYPE="en_US.UTF-8"
LC_MESSAGES="en_US.UTF-8"
LC_MONETARY="en_US.UTF-8"
LC_NUMERIC="en_US.UTF-8"
LC_TIME="en_US.UTF-8"
LC_ALL="en_US.UTF-8"
```

## 参考资料

* [stackOverflow - In OS X Lion, LANG is not set to UTF-8, how to fix it?](https://stackoverflow.com/questions/7165108/in-os-x-lion-lang-is-not-set-to-utf-8-how-to-fix-it)
* [oracle - What Is a Locale?](https://docs.oracle.com/cd/E23824_01/html/E26033/glmbx.html)
