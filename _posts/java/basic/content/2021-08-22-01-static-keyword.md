---
layout:     post
title:      Java基础 - static 关键字
date:       2021-08-22 19:09:50 +0800
postId:     2021-08-22-19-09-50
categories: [Java]
keywords:   [Java]
---

static关键字可以用来修饰代码块表示静态代码块，修饰成员变量表示全局静态成员变量，修饰方法表示静态方法。

```java
class A {
	static {
		System.out.println("A : 静态代码块");
	}
	
	static int i ;  // 静态变量
	
	static void method() {
		System.out.println("A: 静态方法");
	}
}
```

## 修饰 变量
类中用static关键字修饰的成员变量称为静态成员变量，因为static不能修饰局部变量。因此静态成员变量
也能称为静态变量。静态变量跟代码块类似，在类加载到JVM内存中，JVM会把静态变量放入方法区并分配内存，
也由线程共享。访问形式是：类名.静态成员名。

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

## 修饰 方法
用static关键字修饰的方法称为静态方法，否则称为实例方法。通过类名.方法名调用，但需要注意
静态方法可以直接调用类的静态变量和其他静态方法，不能直接调用成员变量和实例方法(除非通过对象调用)。


## 修饰 类
如果一个类要被声明为static的，只有一种情况，就是静态内部类。如果在外部类声明为static，
程序会编译都不会过。在一番调查后个人总结出了3点关于内部类和静态内部类（俗称：内嵌类）

1. 静态内部类跟静态方法一样，只能访问静态的成员变量和方法，不能访问非静态的方法和属性，但是
普通内部类可以访问任意外部类的成员变量和方法。
2. 静态内部类可以声明普通成员变量和方法，而普通内部类不能声明static成员变量和方法。
3. 静态内部类可以单独初始化


```java
// 静态内部类初始化：
Inner i = new Outer.Inner();

// 普通内部类初始化：
Outer o = new Outer();
Inner i = o.new Inner();
```

静态内部类使用场景一般是当外部类需要使用内部类，而内部类无需外部类资源，并且内部类可以单独创建
的时候会考虑采用静态内部类的设计，在知道如何初始化静态内部类，在《Effective Java》第二章
所描述的静态内部类builder阐述了如何使用静态内部类。

## 修饰 代码块
类中用static关键字修饰的代码块称为静态代码，反之没有用static关键字修饰的代码块称为实例代码块。

实例代码块会随着对象的创建而执行，即每个对象都会有自己的实例代码块，表现出来就是实例代码块的运行
结果会影响当前对象的内容，并随着对象的销毁而消失(内存回收)；而静态代码块是当Java类加载到JVM
内存中而执行的代码块，由于类的加载在JVM运行期间只会发生一次，所以静态代码块也只会执行一次。

因为静态代码块的主要作用是用来进行一些复杂的初始化工作，所以静态代码块跟随类存储在方法区的表现
形式是静态代码块执行的结果存储在方法区，即初始化量存储在方法区并被线程共享。

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
