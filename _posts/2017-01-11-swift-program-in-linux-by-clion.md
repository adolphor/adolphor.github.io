---
layout:     post
title:      Linux环境下使用Clion编写swift项目
date:       2017-01-11 23:30:51 +0800
postId:     2017-01-11-23-30-51
categories: [blog]
tags:       [iOS, swift]
geneMenu:   true
excerpt:    Linux环境下使用Clion编写swift项目
---

## 安装swift

### 下载

先下载当前最新版的swift-3.0.2，下载地址：`https://swift.org/download/#releases`，
将其解压在当前用户的workspace目录下，如下所示：

{% highlight shell %}
# 安装依赖包
$ sudo apt-get install clang
# 配置swift环境变量
$ sudo gedit ~/.bashrc
export SWIFT_HOME=$HOME/workspace/swift-3.0.2
export PATH=${SWIFT_HOME}/usr/bin:$PATH
$ source ~/.bashrc
# 测试
$ swift --version
{% endhighlight %}

创建swift项目：
{% highlight shell %}
# 创建swift项目目录
$ cd ～
$ mkdir CLionProjects && cd CLionProjects
$ mkdir helloSwift && cd helloSwift
# 创建swift项目
$ swift package init --type executable
Creating executable package: helloSwift
Creating Package.swift
Creating .gitignore
Creating Sources/
Creating Sources/main.swift
Creating Tests/
# 编译
$ swift build
Compile Swift Module 'helloSwift' (1 sources)
Linking ./.build/debug/helloSwift
# 运行
$ .build/debug/helloSwift
Hello, world!
{% endhighlight %}

## 安装Clion以及swift插件

### 安装Clion
Clion下载地址：`http://www.jetbrains.com/clion/`，下载之后解压址安装目录`/workspace/clion-2016.3.2`，
进入bin文件，使用shell命令启动Clion，记得创建快捷方式。
{% highlight shell %}
$ sh clion.sh
{% endhighlight %}

### 安装Clion的swift插件
插件下载地址：`https://plugins.jetbrains.com/clion/plugin/8240-swift`，
Clion中 File -> Settings -> Plugins -> Install plugin form disk，
选择下载的插件，确定之后重启即可，就能在 File -> Settings -> Build,Execution 目录下看到
swift，选中之后需要指定 SDK Home，如下图所示：

![plugin-dis](/image/post/2017/01/12/20170112-0101-plugin-disk.png)

![plugin-dis](/image/post/2017/01/12/20170112-0102-swift-sdk.png)


### 配置Clion的External tools

```
group:swift
name:swift-clean
program:/bin/rm
parameters:-f $ProjectName$
work dir:$ProjectFileDir$/.build/debug

group:swift
name:swift-build
program:/home/adolphor/workspace/swift-3.0.2/usr/bin/swift-build
work dir:$ProjectFileDir$
```

![plugin-dis](/image/post/2017/01/12/20170112-0108-swift-clean.png)

![plugin-dis](/image/post/2017/01/12/20170112-0109-swift-build.png)

完成之后的样子：
![plugin-dis](/image/post/2017/01/12/20170112-0110-swift-tools.png)



## Clion中运行项目


### 导入swift项目

File -> Import Project

![plugin-dis](/image/post/2017/01/12/20170112-0103-import-program.png)

![plugin-dis](/image/post/2017/01/12/20170112-0104-import-select.png)

![plugin-dis](/image/post/2017/01/12/20170112-0105-import-success.png)

### 配置cmake文件

![plugin-dis](/image/post/2017/01/12/20170112-0106-cmake-template.png)

![plugin-dis](/image/post/2017/01/12/20170112-0107-cmake-done.png)

### 配置运行条件

![plugin-dis](/image/post/2017/01/12/20170112-0111-execute.png)
![plugin-dis](/image/post/2017/01/12/20170112-0112-config-all.png)
![plugin-dis](/image/post/2017/01/12/20170112-0113-execute-done.png)


## 参考资料

* [test](test.html)

{% highlight java %}
{% endhighlight %}
