---
layout:     post
title:      go2shell安装和使用
date:       2023-03-01 09:52:55 +0800
postId:     2023-03-01-09-52-55
categories: [macOS]
keywords:   [macOS]
---

安装完go2Shell之后发现finder的toolbar上找不到快捷图标了，蛮多人第一反应是该应用已经失效了。
其实没有失效，现在我们就操作找回图标。

## 使用brew安装go2shell

```shell
brew install go2shell
```

## 找到Go2ShellHelper 文件

![图标路径]({{ site.baseurl }}/image/post/2023/03/01/01/文件路径.png)

该快捷方式的路径为 `/Applications/Go2Shell.app/Contents/MacOS`，
按下快捷键command + shift + g 输入上面的路基即可：

![前往文件夹]({{ site.baseurl }}/image/post/2023/03/01/01/前往文件夹.png)

## 将Go2ShellHelper 拖动到 toolbar上去即可
有人尝试过拖不上去，可以尝试 按下 command 键试一下：

![拖动到菜单栏]({{ site.baseurl }}/image/post/2023/03/01/01/动图操作示意.gif)

## 参考资料
* [go2shell安装和使用]({% post_url system/macos/2023-03-01-01-macos-software-go2shell %})
* [go2shell失效 finder的toolbar上找不到快捷图标的方法](https://juejin.cn/post/6904454394914291725)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/03/01/01/xxx.jpg)
```
