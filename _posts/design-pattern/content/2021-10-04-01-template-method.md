---
layout:     post
title:      设计模式之 - 模板方法
date:       2021-10-04 20:42:29 +0800
postId:     2021-10-04-20-42-29
categories: [设计模式]
keywords:   [设计模式]
---

## 需求
定义一系列方法执行流程，子类可以重新实现部分方法，但不能改变方法的流程。

## 实现
流程方法定义为final，不能被覆写；
需要被覆写的步骤方法定义为abstract，让子类重写。
执行的时候，父类会调用子类的方法实现，也就实现了方法的灵活扩充效果。

## 范例
Java 中的 ReentrantLock 锁，就是基于 AQS 的模板方法来实现的。

## 参考资料
* [设计模式之 - 模板方法]({% post_url design-pattern/content/2021-10-04-01-template-method %})
* [设计模式之模板模式](https://zhuanlan.zhihu.com/p/55379659)
