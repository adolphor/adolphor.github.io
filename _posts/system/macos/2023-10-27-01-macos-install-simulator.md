---
layout:     post
title:      macOS 安装 Simulator 模拟器
date:       2023-10-27 09:40:27 +0800
postId:     2023-10-27-09-40-27
categories: [macOS]
keywords:   [macOS]
---

使用xcode直接下载模拟器的时候总会由于网路不稳定导致下载失败，
且不能断点续传导致一直安装不成功。
所以考虑手动下载安装的方式解决这个问题。

## 下载
点击下载地址，登陆之后搜索相关内容进行下载：
[https://developer.apple.com/download/all/](https://developer.apple.com/download/all/)

## 安装
下载之后使用如下指令进行安装：
```shell
sudo xcode-select -s /Applications/Xcode.app
xcodebuild -runFirstLaunch
xcrun simctl runtime add "~/Downloads/iOS_17.0.1_Simulator_Runtime.dmg"
```


## 参考资料
* [macOS 安装 Simulator 模拟器]({% post_url system/macos/2023-10-27-01-macos-install-simulator %})
* [Installing and managing Simulator runtimes](https://developer.apple.com/documentation/xcode/installing-additional-simulator-runtimes)
* [SDK下载地址](https://developer.apple.com/download/all/)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/10/27/01/xxx.png)
```
