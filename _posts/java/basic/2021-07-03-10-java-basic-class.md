---
layout:     post
title:      Java基础 - 面向对象：类
date:       2021-07-03 15:13:20 +0800
postId:     2021-07-03-15-13-20
categories: [Java]
tags:       [Java]
geneMenu:   true
excerpt:    Java基础 - 面向对象：类
---

## 面向对象三大特征
* 封装
* 继承
* 多态

## == 和 equals 的区别

### ==
对于基本数据类型来说，==比较的是值；对于引用数据类型来说，==比较的是对象的内存地址。

### equals
`equals()` 作用不能用于判断基本数据类型的变量，只能用来判断两个对象是否相等。equals方法存在
于`Object`类中，而Object类是所有类的直接或间接父类。`Object` 类 `equals()` 方法：
```java
public boolean equals(Object obj) {
     return (this == obj);
}
```

equals() 方法存在两种使用情况：
* 类没有覆盖 equals()方法 ：
  通过equals()比较该类的两个对象时，等价于通过“==”比较这两个对象，使用的默认是 Object类
  equals()方法。
* 类覆盖了 equals()方法 ：
  一般我们都覆盖 equals()方法来比较两个对象中的属性是否相等；若它们的属性相等，则返回 true
  (即，认为这两个对象相等)。

String 中的 equals 方法是被重写过的，所以 Object 的 equals 方法是比较的对象的内存地址，
而 String 的 equals 方法比较的是对象的值。

### 重写equals的约定
重写equals，应当遵守JavaSE的通用约定，如果你违反了上面的约定，你的程序也许就会变现的不正常。
因为所有的集合类都依赖于传递给他的对象是否遵守equals约定。

* 自反性（reflexive）：
  对于任何非null的引用值x, x.equals(x) 必须返回 true。
* 对称性（symmetric）：
  对于任何非null的引用值x和y，当且仅当x.equals(y) 返回true时，y.equals(x)必须返回true。
* 传递性（transitive）：
  对于任何非null的引用值x、y和z，如果x.equals(y)返回true，并且y.equals(z)也返回true，
  那么x.equals(z)也必须返回true。
* 一致性（consistent）：
  对于任何非null的引用值x和y，只要equals的比较操作在对象中所用的信息没有被修改，多次调用
  x.equals(y)就会一致的返回true，或者一致的返回false。 对于任何非null的引用值x，
  x.equals(null)必须返回false。

## hashCode()与 equals()
`重写equals方法一定要重写hashCode方法`，如果不遵守上面这条规则，会给我们的程序带来意想不到的
结果。关于重写hashCode方法， JavaSE 一样给出了约定，如下：

* 在应用程序的执行期间，只要对象的equals方法的比较操作所用到的信息没有被修改，那么对这同一个
  对象调用多次hashCode方法，它必须始终如一地返回同一个整数。在同一个应用程序的多次执行过程中，
  这个整数可以不同。
* 如果两个对象根据equals(Object)方法是相等的，那么调用这两个对象中任一个对象的hashCode方法
  必须产生同样的整数结果。
* 如果两个对象根据equals(Object)方法是不相等的，那么调用这两个对象中任一个对象的hashCode方
  法，不要求必须产生不同的整数结果。然而，程序员应该意识到这样的事实，对于不相等的对象产生截然
  不同的整数结果，有可能提高散列表（hash table）的性能。

如果只重写了equals方法而没有重写hashCode方法的话，则会违反约定的第二条：相等的对象必须具有相
等的散列码 hashCode。两个逻辑相等的对象，如果产生不同的hashCode 将会给HashMap等容器带来意
想不到的结果。因为HashMap是根据对象的散列值确定在HashMap中的存储位置的。如果你不重写hashCode
方法，那么在JVM中是两个完全不同的对象，他们的hashCode也将会不同，所以在HashMap看来他们就不是
同一对象。

## 参考资料

* [test](test.html)
