---
layout:     post
title:      设计模式之 —— 观察者模式进阶：监听器模式
date:       2020-03-14 21:23:26 +0800
postId:     2020-03-14-21-23-26
categories: [design-pattern]
keywords:   [设计模式]
---

## 监听器模式简介

监听器模式并不是一个新的设计模式，而是观察者模式在特定场景下的一种改造和应用。观察者模式中，`主题` 会在特定逻辑下通知所有 `观察者`。 如果这个通知不包含任何信息，那么这种实现就是通常的观察者模式。
如果 主题 通知 观察者 的过程带有一些 `其他信息`，那么主题本身已经上升成为了 `事件源`， 而通知中带有的其他信息经过封装就成为了 `事件`，对应的，观察者则进阶称为了 `监听器`。
事件监听模式的优势：在很多应用场景中，通知中附带的 其他信息 是必不可少的，事件Event则对这些 信息 进行了封装，使它本身拥有了多态的特性。每个事件对象就可以包含不同的信息。但各个 观察者 提供给 主题 的接口仍然是统一的 ：onNotify(entity, event)。【注-1】

责任链一章中曾谈到，AWT1.0的事件处理模型是基于责任链的，这种模型不适用与复杂的系统，因此在AWT1.1及以后的版本中，时间处理模型均为基于观察者模式的委派事件模型（Delegation Event Model 或 DEM）。
在DEM模型里，主题（Subject）角色负责发布（publish）事件，而观察者角色想特定的主题订阅（subscribe）他所感兴趣的事件。当一个具体主题产生一个事件时，他就会通知所有感兴趣的订阅者。
使用这种发布-订阅机制的基本设计目标是提供一种将发布者与订阅者松散地耦合在一起的联系方式，以及一种能够动态地登记、取消向一个发布者的订阅请求的办法。显然，实现这一构思的技巧是设计抽象接口，并把抽象层和具体层分开。这在观察者模式里可以清楚地看到。
使用DEM的用于，发布者叫做事件源（event source），而订阅者叫做事件监听器（event listener）。在Java里边,事件由类代表,时间的发布是通过调用成员方法做到的。 【注-2】

## 观察者模式进阶

在《[设计模式之 —— 观察者模式]({% post_url design-pattern/content/2020-01-23-java-design-patterns-Observer %})》一文中，如果使用匿名实现类的方式来写观察者模式如下所示：

```java
public abstract class Subject {
  private Vector<Observer> observersVector = new Vector<Observer>();
  public void addObserver(Observer observer) {
    observersVector.add(observer);
  }
  public void notifyObservers(Object msg) {
    for (Observer observer : observersVector) {
      observer.update(msg);
    }
  }
}
public class ConcreteSubject extends Subject {
  private String state;
  public void changeState(String newState) {
    state = newState;
    notifyObservers(state);
  }
}
public interface Observer {
  void update(Object state);
}

public class PublishSubscribeDemo {
  public static void main(String[] args) {
    // 初始化主题
    ConcreteSubject subject = new ConcreteSubject();
    // 将监听者添加到通知列表
    subject.addObserver(new Observer() {
      @Override
      public void update(Object state) {
        System.out.println(state);
      }
    });
    // 当主题发生变化或者需要通知监听者的时候进行全量通知
    subject.changeState("I'm changed, will notify all observers");
    subject.notifyObservers("I'm changed, will notify all observers");
  }
}
```

如果将原主题中的 `addObserver` 方法更名为 `addListener`，以及 `changeState()` 更名为 `click()`；将原观察者 `Observer` 对象更名为 `Listener`，以及 `update()` 方法更名为 `onClick()`。即便代码逻辑不做任何改变，我们也可以看出这就是监听者模式的初级版本：

```java
public abstract class Subject {
  private Vector<Listener> observersVector = new Vector<Listener>();
  public void addListener(Listener observer) {
    observersVector.add(observer);
  }
  public void notifyObservers(Object msg) {
    for (Listener observer : observersVector) {
      observer.onClick(msg);
    }
  }
}
public class ConcreteSubject extends Subject {
  private Object state;
  public void click(Object newState) {
    state = newState;
    notifyObservers(state);
  }
}
public interface Listener {
  void onClick(Object msg);
}

public class PublishSubscribeDemo {
  public static void main(String[] args) {
    ConcreteSubject subject = new ConcreteSubject();
    subject.addListener(new Listener() {
      @Override
      public void onClick(Object msg) {
        System.out.println(msg);
      }
    });
    subject.click("I'm changed, will notify all observers");
  }
}
```

## 监听器模式中的3个角色

先用一张图，来比较观察者模式和监听器模式的联系和区别：

![观察者模式和监听器模式]({{ site.baseurl }}/image/post/2020/03/14/publish-vs-listener.png)

一句话，事件源产生事件，事件带有事件源，监听器监听事件：
* 事件源：上面的 `ConcreteSubject`
* 事件对象：上面的 `Object` 信息
* 事件监听器：上面 `Listener` 类型的匿名实现类

### 事件源
具体的事件源，注册特定的监听，才可以对事件进行响应。

### 事件对象
封装了事件源对象以及事件相关的信息，是在事件源和事件监听器之间传递信息的角色。基本上你不包含可执行方法，大部分都是承载各种信息的自定义属性。

### 事件监听器
监听事件，并进行事件处理或者转发，必须注册在事件源上。

### 代码实现

按照这个逻辑来修改上面的雏形代码，主要是增加 事件对象，如下所示：

```java
// 新增加的 事件对象
public class MyMouseEvent {
  int x;
  int y;
  public MyMouseEvent() {
    x = new Random().nextInt();
    y = new Random().nextInt();
  }
  public void move(int x, int y) {
    this.x = x;
    this.y = y;
  }
  @Override
  public String toString() {
    return "MyMouseEvent{" + "x=" + x + ", y=" + y + '}';
  }
}
// 相应的，使用的时候修改参数类型：
public abstract class Subject {
  private Vector<Listener> observersVector = new Vector<Listener>();
  public void addListener(Listener observer) {
    observersVector.add(observer);
  }
  public void notifyObservers(MyMouseEvent event) {
    for (Listener observer : observersVector) {
      observer.onClick(event);
    }
  }
}
public class ConcreteSubject extends Subject {
  public void click(MyMouseEvent event) {
    notifyObservers(event);
  }
}
public interface Listener {
  void onClick(MyMouseEvent event);
}

public class PublishSubscribeDemo {
  public static void main(String[] args) {
    ConcreteSubject subject = new ConcreteSubject();
    subject.addListener(new Listener() {
      @Override
      public void onClick(MyMouseEvent event) {
        System.out.println(event);
      }
    });
    MyMouseEvent event = new MyMouseEvent();
    subject.click(event);
  }
}
```

## 标准监听器模式

如下类图所示，实际使用的时候，事件源可能产生不止一种事件类型，那么实现的时候需要根据不同事件类型做出不同响应（监听器实现类中需要定义不同的方法来对应不同的事件处理）：

![监听器模式类图]({{ site.baseurl }}/image/post/2020/03/14/MouseListener.png)

## 总结

1. 监听器模式的本质就是观察者模式，先将回调函数注册到被观察对象，当被观察对象发生变化的时候，就会通过回调函数告知观察者/监听者
2. 订阅者模式的本质也是观察者模式，只不过绑定注册的时候使用了自然语义定义的 subscribe 方法，而且订阅者模式更加侧重强调广播式的消息传递方式
3. 监听器模式在实际使用中一般通过匿名实现类来实现监听器的具体定义（疑问：为什么不显示定义来复用？或者说为什么这种场景下的逻辑不需要复用？）

## 参考资料

* 【注-1】[监听模式和观察者模式的区别](https://juejin.im/post/5aee749bf265da0b71562ac1)
* 【注-2】[《Java与模式》--阎宏](https://book.douban.com/subject/1214074/)
