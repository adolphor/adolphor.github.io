---
layout:     post
title:      策略模式
date:       2016-12-17 00:10:28 +0800
postId:     2016-12-17-00-10-28
categories: [blog]
tags:       [Java]
geneMenu:   true
excerpt:    设计模式之 —— 策略模式
---

在看策略模式之前，可以先看下《[代理模式]({% post_url 2016-12-15-java-design-patterns-proxy %})》,

## 策略模式

策略模式属于对象的行为模式。其用意是针对一组算法，将每一个算法封装到具有共同接口的独立的类中，
从而使得他们可以相互替换。策略模式使得算法可以在不影响到客户端的情况下发生变化。【注-1】

需要注意的是，虽然在更改算法的时候不需要更改业务逻辑，但调用算法的时候，
必须传入各算法实例才能进行具体算法的调用。

## 使用场景
动态代理是为了在原操作之外增加相同的额外操作，策略模式是为了提供不同的实现方法。如果被代理类只有一个，
可以使用静态代理，如果有多个被代理类都具有相同的额外操作可以使用动态代理。但是策略模式下，至少要有两种
不同的实现方法策略模式才有意义。除此之外，策略模式和代理模式在代码层面是有点像的，都需要传入具体的子类的实例，
之后用此实例调用各自实例的实现方法。但代理模式下的代理类要实现与被代理类相同的接口，而策略类的容器类不用。
所以当同一类型的实例需要根据不同的条件调用不同的方法的时候就可以使用策略模式。

## 源码分析

同样使用买票的例子，有抽象类 TicketSeller，表示具有售票能力，WindowSeller 和 InternetSeller 分别表示
窗口售票和网络售票，TicketService 根据条件（传入的实例类型）调用不同的购票方法，代码如下：

首先抽象出算法共同部分——购票——作为抽象类，代表 “抽象角色”：
```java
public abstract class TicketSeller {
  abstract void sell();
}
```

继承抽象类，实现不同的算法，窗口售票和互联网售票，至于何时调用窗口售票或者互联网售票，
是由客户端调用的时候进行决定，需要调用哪种方法就传入哪种实例，代表 “具体策略角色”：
```java
public class WindowSeller extends TicketSeller {
  public void sell() {
    System.out.println("窗口售票");
  }
}
```
```java
public class InternetSeller extends TicketSeller {
  public void sell() {
    System.out.println("互联网售票");
  }
}
```

下面是持有一个Strategy类的引用的 “环境角色”：
```java
public class TicketService {
  private TicketSeller seller;
  public TicketService(TicketSeller seller) {
    this.seller = seller;
  }
  public void buyTicket() {
    seller.sell();
  }
}
```

下面进行测试，分别测试两种算法：
```java
public class Test {
  public static void main(String[] args) {
    TicketService internetService = new TicketService(new InternetSeller());
    internetService.buyTicket();
    TicketService windowServer = new TicketService(new WindowSeller());
    windowServer.buyTicket();
  }
}
```

测试结果：

```
互联网售票
窗口售票
```

## 参考资料

* 【注-1】[《Java与模式》--阎宏](https://book.douban.com/subject/1214074/)

