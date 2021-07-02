---
layout:     post
title:      设计模式
date:       2016-11-03 22:10:38 +0800
postId:     2016-11-03-22-10-38
categories: [design-pattern]
tags:       [UML, 设计模式]
geneMenu:   true
excerpt:    设计模式简介
---

## 概述

一般而言，一个模式有四个基本要素：
* 模式名称(pattern name)
* 问题(problem) 
* 解决方案(solution) 
* 效果(consequences) 
 
为了更加理解模式使用，需要增加开源项目使用范例内容。

三类设计模式：
* 创建型模式提供生存环境：为其他两种模式使用提供了环境
* 结构型模式提供生存理由：侧重于接口的使用，它做的一切工作都是对象或是类之间的交互，提供一个门
* 行为型模式提供如何生存：顾名思义，侧重于具体行为，所以概念中才会出现职责分配和算法通信等内容

## 1.创建型模式设计模式

创建型模式为其他两种模式使用提供了环境

These design patterns are all about class instantiation. This pattern can be further divided into class-creation patterns and object-creational patterns. While class-creation patterns use inheritance effectively in the instantiation process, object-creation patterns use delegation effectively to get the job done.
创建设计模式（Creational design patterns），这些模式都是跟类的实例化信相关。其实可以进一步分为类的创建和对象的创建。类的创建通过继承来完成实例化过程，对象的创建通过授权来完成相关目标。

### 1.1 简单工厂模式（Simple Factory）

### 1.2 工厂方法（Factory Method）
Factory Method：Creates an instance of several derived classes

### 1.3 抽象工厂（Abstract Factory）
Abstract Factory：Creates an instance of several families of classes

### 1.4 建造模式（Builder）
Builder：Separates object construction from its representation

### 1.5 原始模型（Prototype）
Prototype：A fully initialized instance to be copied or cloned

### 1.6 单例模式（Singleton）
Singleton：A class of which only a single instance can exist

### 1.7 对象池 ？
Object Pool：Avoid expensive acquisition and release of resources by recycling objects that are no longer in use

## 2. 结构型设计模式

结构型模式侧重于接口的使用，它做的一切工作都是对象或是类之间的交互，提供一个门

结构设计模式 (Structural design patterns)，描述如何将类或者对象结合在一起形成更大的结构。分为类的结构模式 和 对象的结构模式

* 类的结构模式
    类的结构模式使用继承来把类、接口等组合在一起，以形成更大的结构。当一个类从父类继承并实现某接口时，这个心的类就把父类的结构和接口的结构结合起来。类的结构模式是静态的。
* 对象的结构模式
    对象的结构模式描述怎样把各种不同的类型的对象组合在一起，以实现新的功能的方法。对象的结构模式是动态的。

These design patterns are all about Class and Object composition. Structural class-creation patterns use inheritance to compose interfaces. Structural object-patterns define ways to compose objects to obtain new functionality.

### 2.1 适配器模式（Adapter）

Adapter：Match interfaces of different classes

### 2.2 桥接模式（Bridge）
Bridge：Separates an object’s interface from its implementation

### 2.3 组合模式（Composite）
Composite：A tree structure of simple and composite objects

### 2.4 装饰模式（Decorator）
Decorator：Add responsibilities to objects dynamically

* [设计模式之 —— 装饰者模式]({% post_url design-pattern/2017-02-23-java-design-patterns-decorator %})

### 2.5 门面模式（Facade）
Facade：A single class that represents an entire subsystem

### 2.6 享元模式（Flyweight）
Flyweight：A fine-grained instance used for efficient sharing

### 2.7 私有类数据
Private Class Data：Restricts accessor/mutator access

### 2.8 代理模式（Proxy）
详见 [代理模式]({% post_url design-pattern/2016-12-15-java-design-patterns-proxy %})。

## 3. 行为型设计模式
行为型设计模式 (Behavioral design patterns)，
These design patterns are all about Class's objects communication. Behavioral patterns are those patterns that are most specifically concerned with communication between objects.

### 3.1 责任链模式（Chain of Responsibility）
Chain of responsibility：A way of passing a request between a chain of objects

### 3.2 命令模式（Command）
Command：Encapsulate a command request as an object

### 3.3 解释器模式（Interpreter）
Interpreter：A way to include language elements in a program

### 3.4 迭代器模式（Iterator）
Iterator：Sequentially access the elements of a collection

### 3.5 中介者模式/调停者模式（Mediator）
Mediator：Defines simplified communication between classes

### 3.6 备忘录模式（Memento）
Memento：Capture and restore an object's internal state

### 3.7 Null Object
Designed to act as a default value of an object

### 3.8 观察者模式（Observer）
Observer：A way of notifying change to a number of classes。观察者模式也称为事件驱动模式，比如著名的Netty框架。

详见：
* [设计模式之 —— 观察者模式]({% post_url design-pattern/2020-01-23-java-design-patterns-Observer %})
* [设计模式之 —— 观察者模式进阶：监听器模式]({% post_url design-pattern/2020-03-14-java-design-patterns-listener %})

### 3.9 状态模式（State）
State：Alter an object's behavior when its state changes

### 3.10 策略模式（Strategy）
Strategy：Encapsulates an algorithm inside a class

详见 [策略模式]({% post_url design-pattern/2016-12-17-java-design-patterns-strategy %})。

### 3.11 模板方法模式（Template Method）
Template method：Defer the exact steps of an algorithm to a subclass

### 3.12 访问者模式（Visitor）
Visitor：Defines a new operation to a class without change

## 参考资料

* 【注-1】[《Java与模式》--阎宏](https://book.douban.com/subject/1214074/)
* [设计模式-可复用面向对象软件的基础 --[美]Erich Gamma/Richard Helm/Ralph Johnson/John Vlissides](https://book.douban.com/subject/1052241/)
* [Design Patterns](https://sourcemaking.com/design_patterns)
* [Design Patterns Tutorials](http://www.avajava.com/tutorials/categories/design-patterns)
* [Design Patterns in Java Tutorial](http://www.tutorialspoint.com/design_pattern/)
* [Design Patterns](http://www.oodesign.com/)
