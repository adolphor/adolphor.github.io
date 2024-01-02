---
layout:     post
title:      macOS系统下配置使用zsh和oh-my-zsh
date:       2017-09-27 10:57:44 +0800
postId:     2017-09-27-10-57-44
categories: [macOS]
keywords:   [zsh]
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

# 修改系统默认shell为zsh
$ chsh -s $(which zsh)
```

## 安装 oh-my-zsh

不能使用brew安装，使用如下命令：
```shell
$ sh -c "$(curl -fsSL https://raw.github.com/robbyrussell/oh-my-zsh/master/tools/install.sh)"
```

## 安装插件

```shell
# 主題 PowerLevel10k
git clone https://github.com/romkatv/powerlevel10k.git ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/themes/powerlevel10k
# 插件 zsh-autosuggestions
git clone https://github.com/zsh-users/zsh-autosuggestions ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-autosuggestions
# 插件 zsh-syntax-highlighting
git clone https://github.com/zsh-users/zsh-syntax-highlighting.git ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-syntax-highlighting
# (選用) 插件 Zsh-z：類似於 autojump 的插件，比 cd 更快速地直接跳到想去的資料夾，且效能更好沒有一堆依賴包。
git clone https://github.com/agkozak/zsh-z ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-z
```

然后修改配置：
> ~/.zshrc
```
ZSH_THEME="powerlevel10k/powerlevel10k"
plugins=(git zsh-autosuggestions zsh-syntax-highlighting zsh-z)
```

# PowerLevel10k 设置
```
p10k configure
```

修改后的配置文件：
> ~/.p10k.zsh

## 更新

```shell
$ omz update
```

## 问题修复

安装完毕之后，启动shell默认就启动了zsh，那么配置在 `~/.bash_profile`中的环境变量就不再生效了，
为了解决这个问题，可以执行如下操作之后重启zsh即可：

```shell
# 编辑 .zshrc 文件
$ vim ~/.zshrc
# 添加如下内容
source ~/.bash_profile
```

## 参考资料

* [ohmyz](http://ohmyz.sh/)
* [oh my zsh 的安装、更新、删除](http://www.jianshu.com/p/4eb7d5ec4515)
* [How to Use Homebrew Zsh Instead of Mac OS X Default](https://zanshin.net/2013/09/03/how-to-use-homebrew-zsh-instead-of-max-os-x-default/)
* [美化 macOS iTerm2 - Oh My Zsh + 一些好用的插件](https://www.kwchang0831.dev/dev-env/macos/oh-my-zsh)
