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

主体思想就是被观察者保存所有观察者对象集合，当状态变化的时候，遍历集合通知每一个观察者对象即可，太简单没啥需要说的，直接贴源码吧：

>> Subject.java

```java
public interface Subject {

  /** 注册登记一个新的观察者对象 */
  void attach(Observer observer);

  /** 删除已注册登记的对象 */
  void detach(Observer observer);

  /** 通知所有已注册登记对象 */
  void notifyObservers();

}
```

>> ConcreteSubject.java

```java
public class ConcreteSubject implements Subject {

  private Vector<Observer> observersVector = new Vector<>();

  @Override
  public void attach(Observer observer) {
    observersVector.add(observer);
  }

  @Override
  public void detach(Observer observer) {
    observersVector.remove(observer);
  }

  @Override
  public void notifyObservers() {
    Enumeration observers = observers();
    while (observers.hasMoreElements()) {
      ((Observer) observers.nextElement()).update();
    }
  }

  /** 使用拷贝，从而使外界不能修改主题自己所使用的拷贝（TODO 为什么不让修改？） */
  private Enumeration observers() {
    return ((Vector) observersVector.clone()).elements();
  }
}
```

>> Observer.java

```java
public interface Observer {
  /** 更新状态等信息 */
  void update();
}
```

>> ConcreteObserver.java

```java
public class ConcreteObserver implements Observer {
  @Override
  public void update() {
    System.out.println("I'm notified!");
  }
}
```

## 参考资料

* 【注-1】[《Java与模式》--阎宏](https://book.douban.com/subject/1214074/)