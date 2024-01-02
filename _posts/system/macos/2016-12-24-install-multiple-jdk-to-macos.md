---
layout:     post
title:      macOS上安装多个版本的JDK
date:       2016-12-24 17:31:08 +0800
postId:     2016-12-24-17-31-08
categories: [JVM, macOS]
keywords:   [Java, JVM, macOS]
---

## 说明

从macOS 10.12开始，Apple公司已经已经不再对JDK6提供技术支持，
而Oracle公司只提供了1.7以及1.8版本，以及最新的JDK9 ，所以本文主要安装这三个版本。

* 系统版本：10.12.6
* JDK版本：
  - 1.7 (1.7.0_80)
  - 1.8 (1.8.0_111)
  - 9 (build 9+181)


## 安装
从官网JDK归档页面可以找到Oracle提供的所有历史版本的JDK：
```
http://www.oracle.com/technetwork/java/archive-139210.html
```
下载macOS版本的1.7、1.8 和 9，按照正常的安装方法进行安装。

## 配置
安装完成之后，调出terminal命令行工具，检验安装是否成功：

```shell
$ java -version
```

```
java version "9"
Java(TM) SE Runtime Environment (build 9+181)
Java HotSpot(TM) 64-Bit Server VM (build 9+181, mixed mode)
```

那如何进行版本切换呢？先看当前生效的JDK路径：
```shell
$ which java
```

```
/usr/bin/java
```

```shell
$ cd /usr/bin
$ ls -l
```

```
……
lrwxr-xr-x   1 root   wheel        74 Oct 21 18:10 java -> /System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/java
lrwxr-xr-x   1 root   wheel        75 Oct 21 18:10 javac -> /System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/javac
lrwxr-xr-x   1 root   wheel        77 Oct 21 18:10 javadoc -> /System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/javadoc
……
```

可以发现这个java只是一个当前系统生效的JDK的一个软连接，Java安装的是实际位置位于
```
/Library/Java/JavaVirtualMachines
```

使用如下的命令可以指定当前系统使用哪个版本的JDK：

```shell
$ /usr/libexec/java_home -v 版本号
```

编辑`bash_profile`文件，添加如下内容（注意：JDK9不是 -v 1.9）：

```shell
$ vi ~/.bash_profile

export JAVA_6_HOME="$(/usr/libexec/java_home -v 1.6)"
export JAVA_7_HOME="$(/usr/libexec/java_home -v 1.7)"
export JAVA_8_HOME="$(/usr/libexec/java_home -v 1.8)"
export JAVA_9_HOME="$(/usr/libexec/java_home -v 9)"
export JAVA_10_HOME="$(/usr/libexec/java_home -v 10)"
export JAVA_11_HOME="$(/usr/libexec/java_home -v 11)"

alias jdk6='export JAVA_HOME=$JAVA_6_HOME'
alias jdk7='export JAVA_HOME=$JAVA_7_HOME'
alias jdk8='export JAVA_HOME=$JAVA_8_HOME'
alias jdk9='export JAVA_HOME=$JAVA_9_HOME'
alias jdk10='export JAVA_HOME=$JAVA_10_HOME'
alias jdk11='export JAVA_HOME=$JAVA_11_HOME'

# 默认使用JDK8
export JAVA_HOME=$JAVA_8_HOME

$ source ~/.bash_profile
```

## 版本切换
如上配置之后，默认使用JDK1.8，然后使用`jdk7`、`jdk8`或`jdk9`来切换当前系统的JDK版本：
```shell
$ java -version
```

```
java version "1.7.0_80"
Java(TM) SE Runtime Environment (build 1.7.0_80-b15)
Java HotSpot(TM) 64-Bit Server VM (build 24.80-b11, mixed mode)
```

```shell
# 切换到JDK1.8，使用如下命令
$ jdk8
$ java -version
```

```
java version "1.8.0_111"
Java(TM) SE Runtime Environment (build 1.8.0_111-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.111-b14, mixed mode)
```

```shell
# 切换到JDK9
$ jdk9
$ java -version
```

```
java version "9"
Java(TM) SE Runtime Environment (build 9+181)
Java HotSpot(TM) 64-Bit Server VM (build 9+181, mixed mode)
```

## 其他

linux环境下JDK配置备份如下：

```shell
$ sudo vi ~/.bashrc
export JAVA_HOME=/my/java/home/jdk180
export CLASSPATH=.:$JAVA_HOME/lib:$JAVA_HOME/jre/lib:$CLASSPATH
export PATH=$JAVA_HOME/bin:$JAVA_HOME/jre/bin:$PATH
$ source ~/.bashrc
```

windows环境下JDK配置如下：

```shell
JAVA_HOME=/my/java/home/jdk180
CLASSPATH=.;%JAVA_HOME%\lib;%JAVA_HOME%\jre\lib
PATH=%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;%PATH%
```

## big sur

查看已安装所有JDK：
```
/usr/libexec/java_home -V

```


## 参考资料

* [Multiple JDK in Mac OSX 10.10 Yosemite](http://abetobing.com/blog/multiple-jdk-mac-osx-10-10-yosemite-88.html)
* [Mac下同时安装多个版本的JDK](http://www.tuicool.com/articles/uUJjEb)
* [Download Java 6 for macOS](https://support.apple.com/kb/DL1572?viewlocale=zh_CN&locale=en_US)
* [历史版本JDK](http://www.oracle.com/technetwork/java/javase/archive-139210.html)
