---
layout:     post
title:      设计模式简介
date:       2016-11-03 22:10:38 +0800
postId:     2016-11-03-22-10-38
categories: [Java]
tags:       [Java, UML, CS]
geneMenu:   true
excerpt:    设计模式简介
---

## 设计模式

## 设计原则

### 开闭原则（OCP）
OCP，open-close principle，一个软件实体应当对扩展开放，对修改关闭。

做到“开-闭”u安泽不是一件容易的事情，但是又很多规律可循。这些规律同样以设计原则的身份出现，
但是它们都是“开-闭”原则的工具和手段，是附属于“开-闭”原则的。【注-1】

### 里氏替换原则
任何基类可以出现的地方，子类一定可以出现。

### 依赖倒转原则
要依赖于抽象，不要依赖于实现。

### 组成/聚合复用原则
尽量使用组合/聚合，而不是继承。

### 迪米特法则
一个软件实体应尽可能减少和其它实体发生相互作用

### 接口隔离原则
应当为客户端提供尽可能小的接口，而不要提供大的总接口



## 设计模式分类

### 1.实例化相关设计模式
创建型设计模式（Creational design patterns），这些模式都是跟类的实例化信相关。其实可以进一步分为类的创建和对象的创建。
类的创建通过继承来完成实例化过程，对象的创建通过授权来完成相关目标。
These design patterns are all about class instantiation. This pattern can be further divided into class-creation patterns
and object-creational patterns. While class-creation patterns use inheritance effectively in the instantiation process,
object-creation patterns use delegation effectively to get the job done.

#### 1.1 抽象工厂
Abstract Factory：Creates an instance of several families of classes

#### 1.2 建造模式
Builder：Separates object construction from its representation

#### 1.3 工厂方法
Factory Method：Creates an instance of several derived classes

#### 1.4 对象池
Object Pool：Avoid expensive acquisition and release of resources by recycling objects that are no longer in use

#### 1.5 原始模型
Prototype：A fully initialized instance to be copied or cloned

#### 1.6 单例模式
Singleton：A class of which only a single instance can exist


### 2. 结构相关设计模式
结构相关设计模式 (Structural design patterns)，
These design patterns are all about Class and Object composition. 
Structural class-creation patterns use inheritance to compose interfaces. 
Structural object-patterns define ways to compose objects to obtain new functionality.

#### 2.1 适配器
Adapter：Match interfaces of different classes

#### 2.2 桥接模式
Bridge：Separates an object’s interface from its implementation

#### 2.3 组件模式
Composite：A tree structure of simple and composite objects

#### 2.4 装饰模式
Decorator：Add responsibilities to objects dynamically

#### 2.5 门面模式
Facade：A single class that represents an entire subsystem

#### 2.6 享元模式
Flyweight：A fine-grained instance used for efficient sharing

#### 2.7 私有类数据
Private Class Data：Restricts accessor/mutator access

#### 2.8 代理模式
Proxy：An object representing another object


### 3. 行为型设计模式
行为型设计模式 (Behavioral design patterns)，
These design patterns are all about Class's objects communication. 
Behavioral patterns are those patterns that are most
specifically concerned with communication between objects.


#### 3.1 责任链模式
Chain of responsibility：A way of passing a request between a chain of objects

#### 3.2 命令模式
Command：Encapsulate a command request as an object

#### 3.3 解释器模式
Interpreter：A way to include language elements in a program

#### 3.4 迭代子模式
Iterator：Sequentially access the elements of a collection

#### 3.5 中介者模式
Mediator：Defines simplified communication between classes

#### 3.6 Memento
Memento：Capture and restore an object's internal state

#### 3.7 Null Object
Designed to act as a default value of an object

#### 3.8 观察者模式
Observer：A way of notifying change to a number of classes

#### 3.9 状态模式
State：Alter an object's behavior when its state changes

#### 3.10 策略模式
Strategy：Encapsulates an algorithm inside a class

#### 3.11 模板方法
Template method：Defer the exact steps of an algorithm to a subclass

#### 3.12 访问者模式
Visitor：Defines a new operation to a class without change

## 参考资料

* 【注-1】《Java与模式——阎宏》
* [Design Patterns](https://sourcemaking.com/design_patterns)
* [Design Patterns Tutorials](http://www.avajava.com/tutorials/categories/design-patterns)
* [Design Patterns in Java Tutorial](http://www.tutorialspoint.com/design_pattern/)
* [Design Patterns](http://www.oodesign.com/)

{% highlight java %}
{% endhighlight %}
