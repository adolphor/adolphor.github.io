---
layout:     post
title:      Linux环境下使用Clion编写swift项目
date:       2017-01-11 23:30:51 +0800
postId:     2017-01-11-23-30-51
categories: [swift]
keywords:   [iOS, swift]
---

## 安装swift

### 下载安装

先下载当前最新版的swift-3.0.2，下载地址：`https://swift.org/download/#releases`，
将其解压在当前用户的workspace目录下：

```shell
# 安装依赖包
$ sudo apt-get install clang
# 配置swift环境变量
$ sudo gedit ~/.bashrc
export SWIFT_HOME=$HOME/workspace/swift-3.0.2
export PATH=${SWIFT_HOME}/usr/bin:$PATH
$ source ~/.bashrc
# 测试
$ swift --version
```

### 创建swift项目
```shell
# 创建swift项目目录
$ cd ～
$ mkdir CLionProjects && cd CLionProjects
$ mkdir helloSwift && cd helloSwift
# 创建swift项目
$ swift package init --type executable
```
```
Creating executable package: helloSwift
Creating Package.swift
Creating .gitignore
Creating Sources/
Creating Sources/main.swift
Creating Tests/
```
```shell
# 编译
$ swift build
```
```
Compile Swift Module 'helloSwift' (1 sources)
Linking ./.build/debug/helloSwift
```
```shell
# 运行
$ .build/debug/helloSwift
```
```
Hello, world!
```

## 安装CLion以及swift插件

### 安装CLion
CLion下载地址：`http://www.jetbrains.com/clion/`，下载之后解压至安装目录`$HOME/workspace/clion-2016.3.2`，
进入`bin`文件，使用shell命令启动CLion，最好勾选创建快捷方式选项。
```shell
$ cd $HOME/workspace/clion-2016.3.2
$ sh clion.sh
```

### 安装CLion的swift插件
插件下载地址：`https://plugins.jetbrains.com/clion/plugin/8240-swift`，
CLion中 File -> Settings -> Plugins -> Install plugin form disk，
选择下载的插件，确定之后重启即可：

![plugin-dis]({{ site.baseurl }}/image/post/2017/01/12/20170112-0101-plugin-disk.png)

重启之后就能在 File -> Settings -> Build,Execution 目录下看到swift，
选中之后需要指定 SDK Home：

![swift-sdk]({{ site.baseurl }}/image/post/2017/01/12/20170112-0102-swift-sdk.png)


### 配置CLion的External tools

为了更方便的编译swift项目，创建两个 External tools： `swift-clean` 和 `swift-build`，
File -> Settings -> Tools -> External Tools -> add，具体配置信息如下：

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

配置页面展示：

![swift-clean]({{ site.baseurl }}/image/post/2017/01/12/20170112-0108-swift-clean.png)

![swift-build]({{ site.baseurl }}/image/post/2017/01/12/20170112-0109-swift-build.png)

完成之后的样子：
![swift-tools]({{ site.baseurl }}/image/post/2017/01/12/20170112-0110-swift-tools.png)


## CLion中运行项目

### 导入swift项目

将上面创建的helloSwift项目导入到CLion，需要进行如下操作：
File -> Import Project，选择创建的项目的位置：

![import-program]({{ site.baseurl }}/image/post/2017/01/12/20170112-0103-import-program.png)

选择所有文件都进行导入：
![import-select]({{ site.baseurl }}/image/post/2017/01/12/20170112-0104-import-select.png)

导入成功之后的页面展示：
![import-success]({{ site.baseurl }}/image/post/2017/01/12/20170112-0105-import-success.png)

### 配置cmake文件
因为CLion主要是C和C++项目，编译管理主要是cmake，所以需要修改CMakeLists.txt，
删除 “project(helloSwift)” 以下的所有配置，然后输入 “add_swift”，选择CLion的
自动提示模板，回车：
![cmake-template]({{ site.baseurl }}/image/post/2017/01/12/20170112-0106-cmake-template.png)

之后在 SOURCES 后增加
```
Package.swift Sources/main.swift
```
配置完成的页面展示：
![cmake-done]({{ site.baseurl }}/image/post/2017/01/12/20170112-0107-cmake-done.png)

### 配置运行条件

运行配置，需要注意的是，选择`Executable`选项的时候，刚开始不显示`.build`文件夹，
需要手动输入，之后就会看到此文件夹下的文件了，选择`.build/debug`目录下的`helloSwift`：
![execute]({{ site.baseurl }}/image/post/2017/01/12/20170112-0111-execute.png)

`Before lunch`中增加CLion中配置的External tools：`swift-clean`和`swift-build`：
![config-all]({{ site.baseurl }}/image/post/2017/01/12/20170112-0112-config-all.png)

运行成功：
![execute-done]({{ site.baseurl }}/image/post/2017/01/12/20170112-0113-execute-done.png)


## 彩蛋

### windows10下编写swift代码

如果使用的是windows10，那么是否可以在windows平台编辑swift项目？答案是可以，
方法是windows版本CLion进行代码的编写，使用 bash on ubuntu on windows 子系统
进行代码的编译和运行。

开启ubuntu子系统之后，进行如下配置：

```shell
# 安装编译组件
$ apt-get install build-essential
# 安装3.6版本之上的clang
$ sudo vim ~/.bashrc
export CLANG_HOME=$HOME/clang+llvm-3.9.0
export PATH=$CLANG_HOME/bin:$CLANG_HOME/include:$CLANG_HOME/lib:$CLANG_HOME/share:$PATH
$ source ~/.bashrc
# 使用bash，在windows文件夹/mnt/c/Users/Bob/ClionProjects下创建名为hello的helloSwift项目
$ cd /mnt/c/Users/Bob/ClionProjects
$ mkdir helloSwift && cd helloSwift
$ swift package init --type executable
# 编译及运行
$ swift build && .build/debug/helloSwift
```

然后就可以导入windows下的CLion进行代码的编写了，编写完成之后使用bash进行编译及运行。


## 参考资料

* [Installing Swift](https://swift.org/getting-started/#installing-swift)
* [CLion](http://www.jetbrains.com/clion/)
* [Swift plugin for CLion](https://blog.jetbrains.com/clion/2015/12/swift-plugin-for-clion/)
* [Setup Swift and Clion on Arch Linux](http://sayem.org/2016/09/09/running-swift-on-arch-linux/)（貌似需要科学上网才能访问）
* [手把手教你在Ubuntu上优雅地玩Swift](http://blog.csdn.net/vic_357/article/details/50786676)
* [clang下载地址](http://releases.llvm.org/download.html)
