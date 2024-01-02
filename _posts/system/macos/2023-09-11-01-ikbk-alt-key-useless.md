---
layout:     post
title:      【转载】IKBK 键盘 ALT键 失效的解决办法
date:       2023-09-11 14:33:10 +0800
postId:     2023-09-11-14-33-10
categories: [macOS]
keywords:   [macOS]
---

## 引子
今天敲代码时发现键盘win键失灵了，因为最近刚从windows系统切换到mac系统编程，所以一直以为是mac系统下键盘配置问题或者兼容性问题，反复尝试了很久没有解决。

后来问了购买店铺的客服，原来是win键被锁住了。

## 原因

ikbc键盘支持快捷键一键锁定/解锁win键。

如下表所示：

|键盘款式   |锁定快捷键   |解锁快捷键   |
|---|---|---|
|非静音款   |Fn + 左Win   |Fn + 右Win   |
|静音款   |Fn + 左Win   |Fn + PrtSc   |

上述方式支持的型号：c87、s200。不同型号的键盘可能方式不一样，可以去官方旗舰店对应型号的商品里找客服问下。
注意：我的键盘是非静音款。

## 锁win键的作用
在windows下锁win键还是有用的，尤其是全屏打游戏的时候。

windows下win键按一下是默认弹出开始菜单，在全屏时也会自动弹出，并且将焦点也移到了开始菜单上。

试想在英雄联盟团战时误操作按到了win键，然后窗口失焦，qwer技能失效，队友？？？

## mac修饰键
mac笔记本默认键盘左下角的布局从左往右是：control，option，command。
而外接键盘一般是：ctrl，win，alt。

接入外接键盘后按键盘指引配置完，键位匹配默认为：ctrl = control，win = command，alt = option。
win和alt匹配的键位正好是反的，来回切换键盘时很难受。

其实可以使用“修饰键”的功能来修改这俩键的顺序。

### 修改方式
打开系统偏好设置 - 键盘- 修饰键，如图，调换option和command键保存即可，改完就是：ctrl = control，win = option，alt = command。

![修改键盘修饰符]({{ site.baseurl }}/image/post/2023/09/11/01/修改键盘修饰符.png)

## 参考资料
* [IKBK 键盘 ALT键 失效的解决办法]({% post_url system/macos/2023-09-11-01-ikbk-alt-key-useless %})
* [ikbc键盘win键失效的解决方法](https://blog.csdn.net/u010059669/article/details/125725621)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/09/11/01/xxx.png)
```
