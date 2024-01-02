---
layout:     post
title:      macOS 已损坏软件安装
date:       2023-03-02 10:32:16 +0800
postId:     2023-03-02-10-32-16
categories: [macOS]
keywords:   [macOS]
---

提示已损坏软件的安装方式。

## 允许第三方软件安装
隐私与安全性：允许第三方软件安装

## 对第三方软件签名
### 签名权限配置

赋予terminal或者iterm完全磁盘访问权限，否则可能签名失败

![完全磁盘访问权限]({{ site.baseurl }}/image/post/2023/03/02/01/完全磁盘访问权限.png)

### 签名指令

对需要签名的app进行签名，替换成需要的app名称即可：

```shell
sudo xattr -rd com.apple.quarantine /Applications/MyApp.app
```
## 选择仍要打开

隐私与安全性：选择仍要打开

## 参考资料
* [macOS 已损坏软件安装]({% post_url system/macos/2023-03-02-01-macos-system-crash-software %})
* [升级macOS 13 Ventura后打开某软件显示“已损坏，无法打开”怎么解决](https://mac.macsc.com/news/979.html)
* [Fix Terminal “Operation not permitted” Error in macOS Monterey, Big Sur, Catalina, Mojave](https://osxdaily.com/2018/10/09/fix-operation-not-permitted-terminal-error-macos/)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/03/02/01/xxx.jpg)
```
