---
layout:     post
title:      Java基础 - static 关键字
date:       2021-08-22 19:09:50 +0800
postId:     2021-08-22-19-09-50
categories: [Java]
keywords:   [Java]
---

static 表示静态的，


## static 修饰变量
### 基本类型变量

```java
public class ClassLayInitDemo {
  public static void main(String[] args) {
    Debug.info(Collaborator.class.hashCode()); // 语句1
    Debug.info(Collaborator.number); // 语句2
    Debug.info(Collaborator.flag);
  }

  static class Collaborator {
    static int number = 1;
    static boolean flag = true;
    static {
      Debug.info("Collaborator initial izing. ..");
    }
  }
}
```

可见，访问Collaborator类本身(语句1)仅仅使该类被Java虚拟机加载，而并没有使其被初始化(此时，
从输出上看我们并没有看到static初始化块被调用)。从"Collaborator initializing.."在number
的初始值1之前被输出可以看出，当一个线程(这里是main线程)初次访问类Collaborator的静态变量
(语句2)时这个类才被初始化。

### 引用类型变量

对于引用型静态变量，static关键字还能够保障一个线程读取到该变量的初始值时，这个值所指向(引用)的
对象已经初始化完毕，这也是使用静态变量方式实现单例模式的原理，详见
[设计模式之 - 单例模式]({% post_url design-pattern/content/2021-08-06-01-singleton %})。

### 多线程下的static关键字

static关键字在多线程环境下有其特殊的涵义，它能够保证一个线程即使在未使用其他同步机制的情况下
也总是可以读取到一个类的静态变量的初始值(而不是默认值)。但是, 这种可见性保障仅限于线程初次
读取该变量。如果这个静态变量在相应类初始化完毕之后被其他线程更新过，那么一个线程要读取该变量的
相对新值仍然需要借助锁、volatile关键字等同步机制。
所以，static关键字仅仅保障读线程能够读取到相应字段的初始值，而不是相对新值。

## static修饰类

## 与其他关键字的联合使用

### final 
static是静态变量，final是常量， 但是 static 可以和 final 一起使用。
static 表示当前变量、方法可以通过类直接调用，而不必创建对象的实例；
而final 表示一经定义之后，不可以改变，基本类型的话不可以改变属性的值，引用类型不可以更改引用地址。

### abstract
static 可以不通过创建对象就进行引用，而 abstract 是表示父类只定义方法而具体实现通过子类覆写来
实现。对于一个抽象方法，如果声明为static则表示可以直接通过类进行调用，而调用的时候却发现没有定义
方法的具体内容，那么明显是不合理的。而且，抽象类是不能通过构造方法构造实例的，也就是说也不能通过
创建抽象方法的实例来调用抽象方法，这也是避免调用未定义的方法。

## 参考资料
* [static关键字]({% post_url java/basic/content/2021-08-22-01-static-keyword %})
* [8.8' - Java多线程编程实战指南-核心篇](https://book.douban.com/subject/27034721/)
* [设计模式之 - 单例模式]({% post_url design-pattern/content/2021-08-06-01-singleton %})
