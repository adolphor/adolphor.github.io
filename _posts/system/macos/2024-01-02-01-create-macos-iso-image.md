---
layout:     post
title:      制作macOS的ISO系统镜像
date:       2024-01-02 16:44:13 +0800
postId:     2024-01-02-16-44-13
categories: [macOS]
keywords:   [macOS]
---

在不使用外界U盘的时候，可以使用如下方式创建ISO系统镜像：

```shell
hdiutil create -o /tmp/Ventura -size 13750m -volname Ventura -layout SPUD -fs HFS+J
hdiutil attach /tmp/Ventura.dmg -noverify -mountpoint /Volumes/Ventura
sudo /Applications/Install\ macOS\ Ventura.app/Contents/Resources/createinstallmedia --volume /Volumes/Ventura --nointeraction
sudo hdiutil detach -force /Volumes/Install\ macOS\ Ventura
hdiutil convert /tmp/Ventura.dmg -format UDTO -o ~/Desktop/Ventura.cdr
mv ~/Desktop/Ventura.cdr ~/Desktop/Ventura.iso
rm /tmp/Ventura.dmg
```

## 参考资料
* [制作macOS的ISO系统镜像]({% post_url system/macos/2024-01-02-01-create-macos-iso-image %})
* [VMware Fusion安装macOS Ventura](https://www.javatang.com/archives/2023/04/21/31323780.html)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2024/01/02/01/xxx.png)
```
