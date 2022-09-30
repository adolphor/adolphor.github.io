---
layout:     post
title:      brew 使用相关
date:       2018-04-29 20:58:37 +0800
postId:     2018-04-29-20-58-37
categories: [macOS]
keywords:   [brew]
---
## 安装

## 一般应用

```shell
# 检查过时应用
brew outdated
# 升级
brew update && brew upgrade
# 清除安装包
brew cleanup --prune=10
```

## 可视化应用

```shell
# 首先确保本命令执行过
brew tap buo/cask-upgrade
# 检查过时
brew cask outdated
# 升级
brew cu -a -y --cleanup
# 清除安装包
brew cask cleanup --outdated
```

## 清理缓存
新版本会超过30天自动清理，当然也可以手动清理：
```shell
brew cleanup
```

## xcode
软件更新的时候可能提示需要xcode相关工具包，那么使用如下方式解决：

### 安装了xcode
```shell
xcode-select --install
sudo xcode-select -s /Applications/Xcode.app/Contents/Developer
sudo xcodebuild -license accept
```

### 没有安装xcode
按照提示安装独立的工具包即可

## 手动安装

2021年10月的时候，发现brew不再支持macOS 10.13.6 High Serria版本，但如果把MacBook Pro 2016
的macOS 升级到 10.15，会感觉系统性能不够，idea开多个实例的时候会卡，所以如果退回10.13.6，
那么部分brew软件既没有编译完成的包可直接使用，从源码编译安装也会遇到各种问题，所以有些软件需要
使用其他方式安装，安装完成之后，可以软链到brew，作为brew其他软件依赖的时候使用。

### 软链命令
```shell
app=rust
ver=1.56.1
home=/usr/local/Cellar/DIY/$app/$ver

mkdir -p /usr/local/Cellar/$app
cd /usr/local/Cellar/$app
ln -s $home $ver
cd /usr/local/opt
ln -s ../Cellar/$app/$ver $app

app=openjdk
ver=17.0.1
home=/usr/local/Cellar/DIY/$app/$ver

mkdir -p /usr/local/Cellar/$app
cd /usr/local/Cellar/$app
ln -s $home $ver
cd /usr/local/opt
ln -s ../Cellar/$app/$ver $app

app=ffmpeg
ver=4.4.1_3
home=/usr/local/Cellar/DIY/$app/$ver

mkdir -p /usr/local/Cellar/$app
cd /usr/local/Cellar/$app
ln -s $home $ver
cd /usr/local/opt
ln -s ../Cellar/$app/$ver $app

```

如果想在命令行使用，需要添加环境变量：
```shell
# ffmpeg
export PATH="/usr/local/opt/ffmpeg:$PATH"
```

## 参考资料
* [Github - cask-upgrade-workflow](https://github.com/NotAlexNoyle/cask-upgrade-workflow)

