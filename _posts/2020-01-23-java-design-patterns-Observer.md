---
layout:     post
title:      设计模式之 —— 观察者模式
date:       2020-01-23 12:21:17 +0800
postId:     2020-01-23-12-21-17
categories: [blog]
tags:       [设计模式]
geneMenu:   true
excerpt:    设计模式之 —— 观察者模式
---

观察者模式又叫做发布/订阅（Publish/Subscribe）模式、视图/模型（View/Model）模式、源/监听（Source/Listener）模式 或 
从属者（Dependents）模式。观察者模式定义了一种一对多的依赖关系，让多个观察者对象同时监听某一个主题对象。这个主题对象在发生变化时，
会通知所有观察者对象，使他们能够更新自己。【注-1】

主体思想就是被观察者保存所有观察者对象集合，当状态变化的时候，遍历集合通知每一个观察者对象即可。



## 参考资料

* 【注-1】[《Java与模式》--阎宏](https://book.douban.com/subject/1214074/)