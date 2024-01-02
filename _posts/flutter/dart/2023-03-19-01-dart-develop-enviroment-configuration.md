---
layout:     post
title:      Dart和Flutter环境配置
date:       2023-03-19 21:01:11 +0800
postId:     2023-03-19-21-01-11
categories: [Flutter]
keywords:   [Flutter,Dart]
---

Dart基础环境配置等

## macOS 环境配置
### dart和flutter安装
因为Flutter依赖于Dart，所以直接安装Flutter就好了，Dart会包含在内。
```shell
brew install flutter
```
```
==> flutter: 3.7.7 (auto_updates)
https://flutter.dev/
/usr/local/Caskroom/flutter/3.7.7 (18,787 files, 2.4GB)
From: https://github.com/Homebrew/homebrew-cask/blob/HEAD/Casks/flutter.rb
==> Name
Flutter SDK
==> Description
UI toolkit for building applications for mobile, web and desktop
==> Artifacts
flutter/bin/dart (Binary)
flutter/bin/flutter (Binary)
==> Analytics
install: 583 (30 days), 9,134 (90 days), 41,737 (365 days)
```

### idea 配置
idea 安装 flutter插件，安装后，进入idea配置页面，进行相关配置，
由上面brew安装flutter的信息可知，flutter的SDK所在目录是：
`/usr/local/Caskroom/flutter/3.7.7/flutter`。

![flutter-sdk]({{ site.baseurl }}/image/post/2023/03/19/01/flutter-sdk.png)

## 创建项目

### Dart测试项目
![create-dart-project]({{ site.baseurl }}/image/post/2023/03/19/01/create-dart-project.png)
![dart-project-main]({{ site.baseurl }}/image/post/2023/03/19/01/dart-project-main.png)

### Flutter测试项目
![create-flutter-project]({{ site.baseurl }}/image/post/2023/03/19/01/create-flutter-project.png)
![flutter-project-main]({{ site.baseurl }}/image/post/2023/03/19/01/flutter-project-main.png)

## 参考资料
* [Dart和Flutter环境配置]({% post_url flutter/dart/2023-03-19-01-dart-develop-enviroment-configuration %})
* [官方 Dart documentation](https://dart.dev/guides)
* [Introducing Dart 3 alpha](https://medium.com/dartlang/dart-3-alpha-f1458fb9d232)

