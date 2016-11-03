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




## 1.Creational design patterns

These design patterns are all about class instantiation. This pattern can be further divided into class-creation patterns
and object-creational patterns. While class-creation patterns use inheritance effectively in the instantiation process,
object-creation patterns use delegation effectively to get the job done.


### 1.1 Abstract Factory
Creates an instance of several families of classes

### 1.2 Builder
Separates object construction from its representation

### 1.3 Factory Method
Creates an instance of several derived classes

### 1.4 Object Pool
Avoid expensive acquisition and release of resources by recycling objects that are no longer in use

### 1.5 Prototype
A fully initialized instance to be copied or cloned

### 1.6 Singleton
A class of which only a single instance can exist


## 2. Structural design patterns
These design patterns are all about Class and Object composition. Structural class-creation patterns use inheritance to
compose interfaces. Structural object-patterns define ways to compose objects to obtain new functionality.


### 2.1 Adapter
Match interfaces of different classes

### 2.2 Bridge
Separates an object’s interface from its implementation

### 2.3 Composite
A tree structure of simple and composite objects

### 2.4 Decorator
Add responsibilities to objects dynamically

### 2.5 Facade
A single class that represents an entire subsystem

### 2.6 Flyweight
A fine-grained instance used for efficient sharing

### 2.7 Private Class Data
Restricts accessor/mutator access

### 2.8 Proxy
An object representing another object


## 3. Behavioral design patterns
These design patterns are all about Class's objects communication. Behavioral patterns are those patterns that are most
specifically concerned with communication between objects.


### 3.1 Chain of responsibility
A way of passing a request between a chain of objects

### 3.2 Command
Encapsulate a command request as an object

### 3.3 Interpreter
A way to include language elements in a program

### 3.4 Iterator
Sequentially access the elements of a collection

### 3.5 Mediator
Defines simplified communication between classes

### 3.6 Memento
Capture and restore an object's internal state

### 3.7 Null Object
Designed to act as a default value of an object

### 3.8 Observer
A way of notifying change to a number of classes

### 3.9 State
Alter an object's behavior when its state changes

### 3.10 Strategy
Encapsulates an algorithm inside a class

### 3.11 Template method
Defer the exact steps of an algorithm to a subclass

### 3.12 Visitor
Defines a new operation to a class without change

## 参考资料

* 【注-1】《Java与模式——阎宏》

* [test](test.html)

* Demo0x 是具有全套范例的示例
* Demo1x 是网上搜索的零散示例

* Demo01 - All language
https://sourcemaking.com/design_patterns
* Demo02 - Java language
http://www.avajava.com/tutorials/categories/design-patterns
* Demo03 - Java language
http://www.tutorialspoint.com/design_pattern/
* Demo04 - Java language
http://www.oodesign.com/observer-pattern.html
* C++ language
http://gameprogrammingpatterns.com/contents.html
* video
http://www.newthinktank.com/videos/design-patterns-tutorial/


* 动态代理:
http://blog.csdn.net/a596620989/article/details/6927487





## Q&A
### 1.静态代理类需要将被代理类的所有方法都覆写一遍吗？
Thinking in Java 的代码范例中复用使用的代理是否具有普遍适用性？
SpaceShipDelegation.java

{% highlight java %}
{% endhighlight %}
