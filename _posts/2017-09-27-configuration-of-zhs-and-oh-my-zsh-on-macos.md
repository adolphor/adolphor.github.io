---
layout:     post
title:      macOS系统下配置使用zsh和oh-my-zsh
date:       2017-09-27 10:57:44 +0800
postId:     2017-09-27-10-57-44
categories: [blog]
tags:       [macOS]
geneMenu:   true
excerpt:    macOS系统下配置使用zsh和oh-my-zsh
---

## 安装 zsh


```shell
# 使用brewhome安装
$ brew install zsh

# 配置
$ sudo vim /etc/shells
# 添加如下配置：
/bin/zsh
/usr/local/bin/zsh

# 查看配置结果
$ cat /etc/shells
/bin/bash
/bin/csh
/bin/sh
/bin/tcsh
/bin/zsh
/usr/local/bin/zsh

# 修改系统默认shell为zsh
$ chsh -s /usr/local/bin/zsh
```

## 安装 oh-my-zsh

不能使用brew安装，使用如下命令：
```shell
$ sh -c "$(curl -fsSL https://raw.github.com/robbyrussell/oh-my-zsh/master/tools/install.sh)"
```

## 更新

```shell
$ brew update
$ brew upgrade
$ upgrade_oh_my_zsh
```

## 卸载

```shell
$ brew uninstall zsh
$ uninstall_oh_my_zsh
```

## 参考资料

* [ohmyz](http://ohmyz.sh/)
* [oh my zsh 的安装、更新、删除](http://www.jianshu.com/p/4eb7d5ec4515)
* [How to Use Homebrew Zsh Instead of Mac OS X Default](https://zanshin.net/2013/09/03/how-to-use-homebrew-zsh-instead-of-max-os-x-default/)
