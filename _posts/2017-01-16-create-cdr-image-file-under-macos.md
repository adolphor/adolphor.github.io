---
layout:     post
title:      macOS下制作cdr格式的系统镜像
date:       2017-01-16 20:16:51 +0800
postId:     2017-01-16-20-16-51
categories: []
tags:       [macOS]
geneMenu:   true
excerpt:    macOS下制作cdr格式的系统镜像
---

在macOS系统下，将官方的macOS系统安装包转换为cdr镜像文件，这样可以在别的电脑上或者虚拟机中使用。

## 下载
打开 App Store，搜索 macOS 进行下载即可，当前最新版本是 10.12.2：
![system-info](/image/post/2017/01/16/20170116-00-system-info.png)

下载完毕之后，可以在应用程序目录中看到：
![download-done](/image/post/2017/01/16/20170116-01-download-done.png)

## 打开下载文件
进入"应用程序"，选中下载好的文件，右键选择"显示包内容"，会看到Contents目录：
![show-contents-file](/image/post/2017/01/16/20170116-02-show-contents-file.png)

在此目录中查找 "SharedSupport" -> "InstallESD.dmg"，双击打开，
![contents-dmg](/image/post/2017/01/16/20170116-03-contents-dmg.png)

之后就可以在文件夹左边目录看到加载出来的文件：
![loaded-dmg](/image/post/2017/01/16/20170116-04-loaded-dmg.png)

## 磁盘工具
打开磁盘工具，左边菜单栏选中刚才加载的文件：
![disk-util](/image/post/2017/01/16/20170116-05-disk-util.png)

导航菜单栏中选择 "文件" -> "新建映像" -> "'OS X install ESD'的映像""：
![order-path](/image/post/2017/01/16/20170116-06-order-path.png)

修改保存位置（比如桌面），文件格式为DVD：
![personal-config](/image/post/2017/01/16/20170116-07-personal-config.png)

修改完毕点击制作，制作完成之后就可以在桌面上看到制作好的镜像文件了：
![make-cdr-done](/image/post/2017/01/16/20170116-08-make-cdr-done.png)

## 格式转换
如果想要将cdr转换为ISO格式，在"终端"中执行如下命令：
```shell
$ hdiutil convert ~/Desktop/macOS\ 10.12.2.cdr -format UDTO -o ~/Desktop/macOS\ 10.12.2.iso
$ mv ~/Desktop/macOS\ 10.12.2.iso.cdr ~/Desktop/macOS\ 10.12.2.iso
```

执行成功会显示如下信息：
![converted-to-iso](/image/post/2017/01/16/20170116-09-converted-to-iso.png)

## 参考资料

* [Tip to convert Yosemite.app to Yosemite.ISO](http://macdrug.com/convert-yosemite-app-to-yosemite-iso/)
* [create-iso.sh —— julianxhokaxhiu](https://gist.github.com/julianxhokaxhiu/6ed6853f3223d0dd5fdffc4799b3a877)

