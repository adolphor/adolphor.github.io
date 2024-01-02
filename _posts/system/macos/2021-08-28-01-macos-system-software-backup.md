---
layout:     post
title:      macOS 系统软件备份
date:       2021-08-28 18:48:55 +0800
postId:     2021-08-28-18-48-55
categories: [macOS]
keywords:   [macOS]
---

## brew安装软件备份
```
# 备份
$ brew install htop
$ brew tap Homebrew/bundle
$ brew bundle dump
$ mv Brewfile ~/cloud
# 还原
$ brew bundle
```
[](https://tomlankhorst.nl/brew-bundle-restore-backup/)

## iter2 备份和还原

```
iTerm -> Preferences -> Profiles -> Other Actions -> Copy All Profiles as JSON
```

## maven 备份和还原
setting.xml备份一下即可

## ssh
文件夹备份即可

## application

软链备份：
```
lrwxr-xr-x   1 adolphor  staff    29B Dec 19  2019 gradle -> /usr/local/opt/gradle/libexec
lrwxr-xr-x   1 adolphor  staff    28B Dec 19  2019 maven -> /usr/local/opt/maven/libexec
lrwxr-xr-x   1 adolphor  staff    32B Dec 19  2019 tomcat8 -> /usr/local/opt/tomcat@8/libexec/
lrwxr-xr-x   1 adolphor  staff    31B Mar 11  2020 tomcat9 -> /usr/local/opt/tomcat@9/libexec
```

项目备份：
```
BaiduExporter
Downie
UnblockNeteaseMusic
YAAW-for-Chrome
crackJetbrain
dbeaver-agent
diy-shell
flutter
istio-1.10.0
loginShell.sh
playFramework
surge
v2ray
yaaw
```

## 文档备份
工作空间

## 项目备份
IdeaProjects

## xcode
在 macOS 10.13.6（High Sierra）上可以正常安装的版本是 xcode 9.4.1，如果想要安装 xcode
10.3 需要按照 [Xcode 10.2 on High Sierra – Step by Step](https://codewithchris.com/xcode-update/) 
进行操作（10.4及其后续版本是否可用尚未验证）。

1. 修改 文件的 Minimum System Version 为 10.13.6
    * Xcode.app/Contents/Info.plist
    * Xcode.app/Contents/Applications/FileMerge/Contents/Info.plist
    * Xcode.app/Contents/Developer/Applications/Simulator.app/Contents/Info.plist
2. 拷贝老版本 Xcode.app/Contents/Developer/usr/bin/xcodebuild 覆盖新版本


安装后，需要指定命令行所使用的xcode版本：
```
sudo xcode-select --switch path/to/Xcode.app
```

## docker启动应用

```
docker run --name nacos-standalone -e MODE=standalone -p 8848:8848 -d nacos/nacos-server:latest

docker run -d \
-e MODE=standalone \
-e SPRING_DATASOURCE_PLATFORM=mysql \
-e MYSQL_SERVICE_HOST=sie-saaf-mysql \
-e MYSQL_SERVICE_PORT=3313 \
-e MYSQL_SERVICE_USER=app \
-e MYSQL_SERVICE_PASSWORD='!Apps@61' \
-e MYSQL_SERVICE_DB_NAME=nacos \
-p 8848:8848 \
--name nacos-sie \
nacos/nacos-server:latest

docker run --name oracle12g -d -p 1521:1521 truevoly/oracle-12c
# Orale服务器连接参数:   
hostname: localhost   
port: 1521   
sid: xe   
service name: xe   
username: system   
password: oracle   
```

## 参考资料
* [macOS 系统软件备份]({% post_url system/macos/2021-08-28-01-macos-system-software-backup %})
* [macOS resource Downloads](https://developer.apple.com/download/all/)
