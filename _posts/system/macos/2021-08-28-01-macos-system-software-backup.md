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

## 参考资料
* [macOS 系统软件备份]({% post_url system/macos/2021-08-28-01-macos-system-software-backup %})

