---
layout:     post
title:      设计模式之 —— 观察者模式
date:       2020-01-23 12:21:17 +0800
postId:     2020-01-23-12-21-17
categories: [article,design-pattern]
tags:       [设计模式]
geneMenu:   true
excerpt:    设计模式之 —— 观察者模式
---

观察者模式是对象的行为模式 [GOF95]，又叫做发布/订阅（Publish/Subscribe）模式、视图/模型（View/Model）模式、源/监听（Source/Listener）模式 或 从属者（Dependents）模式。观察者模式定义了一种一对多的依赖关系，让多个观察者对象同时监听某一个主题对象。这个主题对象在发生变化时，会通知所有观察者对象，使他们能够更新自己。【注-1】

本文从最基础的观察者模式出发，逐步迭代出比较复杂和完善的观察者模式，第一能够比较深入理解观察者模式，第二逐步推导出监听器模式以及后续的事件驱动模型。

## 初级使用

定义观察者为Observer，此对象有一个（回调）函数；被观察者为Subject（主题），被观察者有一个列表vector容器，可以存储所有观察者对象的实例，这样当主题发生变化或者其他情况需要通知观察者的时候，可以通过遍历vector列表获取每一个观察者实例，来调用实例对应的回调方法：

![发布订阅模式-UML](/image/post/2020/01/23/PublishSubscribe-UML.png)


![发布订阅模式](/image/post/2020/01/23/PublishSubscribe.png)

具体代码实现如下：

```java
/** 被观察者 */
public abstract class Subject {
  // 存储所有观察者对象的实例
  private Vector<Observer> observersVector = new Vector<>();
  public void addObserver(Observer observer) {
    observersVector.add(observer);
  }
  public void notifyObservers() {
    for (Observer observer : observersVector) {
      observer.update();
    }
  }
}
public class ConcreteSubject extends Subject {
  private String state;
  public void changeState(String newState) {
    state = newState;
    notifyObservers();
  }
}
/** 观察者 */
public interface Observer {
  void update();
}
public class ConcreteObserver implements Observer {
  @Override
  public void update() {
    System.out.println("I'm called to update");
  }
}
/** 使用方法 */
public class PublishSubscribeDemo {
  public static void main(String[] args) {
    // 初始化主题
    ConcreteSubject subject = new ConcreteSubject();
    // 实例化一个监听者
    Observer observer = new ConcreteObserver();
    // 将监听者添加到通知列表
    subject.addObserver(observer);
    // 当主题发生变化或者需要通知监听者的时候进行全量通知
    subject.changeState("I'm changed, will notify all observers");
    subject.notifyObservers();
  }
}
```

## 增加通知参数

上面的例子中，主题发生变化的时候只是单纯的调用了一下notify方法，且没有传递任何参数，那如果需要传递参数，就需要考虑传递的参数类型，以及这个参数如何进行封装，最简单的就是使用Object进行封装：

```java
// 主题发生变化通知观察者的时候，传递发生变化的内容信息
// Subject.java
public void notifyObservers(Object state) {
  for (Observer observer : observersVector) {
    observer.update(state);
  }
}
// ConcreteSubject.java
private Object state;
public void changeState(Object newState) {
  state = newState;
  notifyObservers(state);
}
// PublishSubscribeDemo.java
// changeState方法不变，主动调用通知方法的时候则需要填充通知内容
subject.changeState("I'm changed, will notify all observers");
subject.notifyObservers("I'm changed, will notify all observers");
```

## JDK中的使用

在JDK中，subject对应的是Observale.java，基本内容和上面类似，只不过回调订阅者update方法的时候，也将主题对象作为参数传递给了订阅者，不再赘述，使用范例如下：

```java
public class ConcreteSubject extends Observable {
  private String state;
  public void changeState(String newState) {
    state = newState;
    setChanged();
    notifyObservers(state);
  }
}
public class ConcreteObserver implements Observer {
  private Object newState;
  @Override
  public void update(Observable o, Object state) {
    newState = state;
    System.out.println(state);
  }
}
public class PublishSubscribeDemo {
  public static void main(String[] args) {
    // 初始化主题
    ConcreteSubject subject = new ConcreteSubject();
    // 实例化一个监听者
    ConcreteObserver observer = new ConcreteObserver();
    // 将监听者添加到通知列表
    subject.addObserver(observer);
    // 当主题发生变化或者需要通知监听者的时候进行全量通知
    subject.changeState("I'm changed, will notify all observers");
  }
}
```

## 使用匿名函数进行调用

上面的例子中可以看到，`Observer.java` 接口只是一个订阅者身份标记接口，订阅主题的时候如果传入的实例使用匿名实现类，如下所示：

```java
public class PublishSubscribeDemo {
  public static void main(String[] args) {
    // 初始化主题
    ConcreteSubject subject = new ConcreteSubject();
    // 将监听者添加到通知列表
    subject.addObserver(new Observer() {
      // 匿名观察者实现类
      @Override
      public void update(Object msg) {
        System.out.println(msg);
      }
    });
    // 当主题发生变化或者需要通知监听者的时候进行全量通知
    subject.changeState("I'm changed, will notify all observers");
    subject.notifyObservers("I'm changed, will notify all observers");
  }
}
```

* 第一，更深刻理解了匿名实现类中的方法是如何接收参数的；
* 第二，这里的代码形式已经具有了监听器模式的雏形，具体参考《[设计模式之 —— 观察者模式进阶：监听器模式]({% post_url design-pattern/2020-03-14-java-design-patterns-listener %})》。

## 按照自然语义改写

### subscribe 订阅

如果按照正常的语义来编写代码的话，应该是 观察者 主动订阅（subscribe）被观察者（主题），所以可以在观察着对象中增加一个 subscribe 方法：

```java
/** Observer.java */
void subscribe(Subject subject);

/** ConcreteObserver.java */
@Override
public void subscribe(Subject subject) {
  subject.addObserver(this);
}
```
这样当观察者需要订阅某主体的时候直接调用subscribe方法，而不是操作被订阅者然后将自己添加进去，更符合自然语义。然后调用的时候进行如下更改即可：

```java
// 将 subject.addObserver(observer); 更改为主动订阅：
observer.subscribe(subject);
```

## 观察者模式优缺点 

观察者模式优缺点 【注-2】

### 优点
1. 观察者模式在被观察者和观察者之间建立一个抽象的耦合，被观察者并不认识任何一个具体观察者，它只知道它们都有一个共同的接口。
2. 观察者模式支持广播通讯，被观察者会向所有的登记过的观察者发出通知。

### 缺点
1. 如果一个被观察者对象有很多的直接和间接的观察者的话，将所有的观察者都通知到会花费很多时间。
2. 如果在被观察者之间有循环依赖的话，被观察者会触发它们之间进行循环调用，导致系统崩溃，在使用观察者模式是要特别注意这一点。
3. 如果对观察者的通知是通过另外的线程进行异步投递的话，系统必须保证投递是以自恰的方式进行的。
4. 虽然观察者模式可以随时使观察者知道所观察的对象发生了变化，但观察者模式没有相应的机制使观察者知道所观察的对象是怎么发生变化的

## 参考资料

* 【注-1】[《Java与模式》--阎宏](https://book.douban.com/subject/1214074/)
* 【注-2】[《Java与模式》--阎宏](https://book.douban.com/subject/1214074/)
* [java设计模式-回调、事件监听器、观察者模式](https://my.oschina.net/u/923324/blog/792857)
* [JDK自带的观察者模式](https://www.cnblogs.com/duanxz/archive/2013/01/09/2853139.html)
* [JDK自带的监听器模式](https://www.cnblogs.com/duanxz/archive/2013/01/11/2855979.html)
* [JAVA设计模式—观察者模式和Reactor反应堆模式](https://www.cnblogs.com/ssskkk/p/9703926.html)