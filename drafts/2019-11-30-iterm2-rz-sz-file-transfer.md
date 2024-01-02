---
layout:     post
title:      iterm2 配合 rz & sz 实现文件上传下载
date:       2019-11-30 09:20:13 +0800
postId:     2019-11-30-09-20-13
categories: [blog]
tags:       [shell]
geneMenu:   true
excerpt:    iterm2 配合 rz & sz 实现文件上传下载
---

## 准备条件

* 已经安装iterm2
* 已经安装homebrew

## 安装并配置 lrzsz

### 安装
```shell
brew install lrzsz
```

```log
==> Downloading https://homebrew.bintray.com/bottles/lrzsz-0.12.20_1.catalina.bottle.tar.gz
######################################################################## 100.0%
==> Pouring lrzsz-0.12.20_1.catalina.bottle.tar.gz
🍺  /usr/local/Cellar/lrzsz/0.12.20_1: 18 files, 473.9KB
```

### 配置

1) `/usr/loal/bin` 增加如下两个配置文件
    * iterm2-recv-zmodem.sh
    * iterm2-send-zmodem.sh
2) 为上面两个配置增加权限
    * chmod +777 iterm2-*

## iTerm2 配置添加rz sz 功能

点击 iTerm2 的设置界面 Perference-> Profiles -> Default -> Advanced -> Triggers 的 Edit 按钮




## 具体配置代码

### iterm2-recv-zmodem.sh
```shell
#!/bin/bash
# Author: Matt Mastracci (matthew@mastracci.com)
# AppleScript from http://stackoverflow.com/questions/4309087/cancel-button-on-osascript-in-a-bash-script
# licensed under cc-wiki with attribution required 
# Remainder of script public domain

osascript -e 'tell application "iTerm2" to version' > /dev/null 2>&1 && NAME=iTerm2 || NAME=iTerm
if [[ $NAME = "iTerm" ]]; then
    FILE=`osascript -e 'tell application "iTerm" to activate' -e 'tell application "iTerm" to set thefile to choose folder with prompt "Choose a folder to place received files in"' -e "do shell script (\"echo \"&(quoted form of POSIX path of thefile as Unicode text)&\"\")"`
else
    FILE=`osascript -e 'tell application "iTerm2" to activate' -e 'tell application "iTerm2" to set thefile to choose folder with prompt "Choose a folder to place received files in"' -e "do shell script (\"echo \"&(quoted form of POSIX path of thefile as Unicode text)&\"\")"`
fi

if [[ $FILE = "" ]]; then
    echo Cancelled.
    # Send ZModem cancel
    echo -e \\x18\\x18\\x18\\x18\\x18
    sleep 1
    echo
    echo \# Cancelled transfer
else
    cd "$FILE"
    /usr/local/bin/rz -E -e -b
    sleep 1
    echo
    echo
    echo \# Sent \-\> $FILE
fi
```
### iterm2-send-zmodem.sh

```shell
#!/bin/bash
# Author: Matt Mastracci (matthew@mastracci.com)
# AppleScript from http://stackoverflow.com/questions/4309087/cancel-button-on-osascript-in-a-bash-script
# licensed under cc-wiki with attribution required 
# Remainder of script public domain

osascript -e 'tell application "iTerm2" to version' > /dev/null 2>&1 && NAME=iTerm2 || NAME=iTerm
if [[ $NAME = "iTerm" ]]; then
    FILE=`osascript -e 'tell application "iTerm" to activate' -e 'tell application "iTerm" to set thefile to choose file with prompt "Choose a file to send"' -e "do shell script (\"echo \"&(quoted form of POSIX path of thefile as Unicode text)&\"\")"`
else
    FILE=`osascript -e 'tell application "iTerm2" to activate' -e 'tell application "iTerm2" to set thefile to choose file with prompt "Choose a file to send"' -e "do shell script (\"echo \"&(quoted form of POSIX path of thefile as Unicode text)&\"\")"`
fi
if [[ $FILE = "" ]]; then
    echo Cancelled.
    # Send ZModem cancel
    echo -e \\x18\\x18\\x18\\x18\\x18
    sleep 1
    echo
    echo \# Cancelled transfer
else
    /usr/local/bin/sz "$FILE" -e -b
    sleep 1
    echo
    echo \# Received $FILE
fi 
```

### iterm2 配置信息

```log
Regular expression: rz waiting to receive.\*\*B0100
Action:             Run Silent Coprocess
Parameters:         /usr/local/bin/iterm2-send-zmodem.sh

Regular expression: \*\*B00000000000000
Action:             Run Silent Coprocess
Parameters:         /usr/local/bin/iterm2-recv-zmodem.sh
```

## 参考资料

* [Mac osx 下安装iTerm2，并使用rz sz上传下载（附homebrew配置）](https://segmentfault.com/a/1190000012166969)