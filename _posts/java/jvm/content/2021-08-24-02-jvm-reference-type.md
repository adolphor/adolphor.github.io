---
layout:     post
title:      JVM - 引用类型
date:       2021-08-24 14:29:48 +0800
postId:     2021-08-24-14-29-48
categories: [JVM]
keywords:   [Java,JVM]
---

在 JDK 1.2 版之前，Java 里面的引用是很传统的定义：如果 reference 类型的数据中存储的数值
代表的是另外一块内存的起始地址，就称该 reference 数据是代表某块内存、某个对象的引用。这种
定义并没有什么不对，只是现在看来有些过于狭隘了，一个对象在这种定义下只有“被引用”或 者“未被引
用”两种状态，对于描述一些“食之无味，弃之可惜”的对象就显得无能为力。譬如我们希望能描述一类对
象：当内存空间还足够时，能保留在内存之中，如果内存空间在进行垃圾收集后仍然非常紧张，那就可以
抛弃这些对象 —— 很多系统的缓存功能都符合这样的应用场景。

## 引用类型
在 JDK 1.2 版之后，Java 对引用的概念进行了扩充，将引用分为强引用(Strongly Re-ference)、
软引用(Soft Reference)、弱引用(Weak Reference)和虚引用(Phantom Reference)4 种，这 4
种引用强度依次逐渐减弱。

### 强引用
强引用是最传统的“引用”的定义，是指在程序代码之中普遍存在的引用赋值，即类 似“Object obj=new
Object()”这种引用关系。无论任何情况下，只要强引用关系还存在， 垃圾收集器就永远不会回收掉被引
用的对象。

### 软引用
软引用是用来描述一些还有用，但非必须的对象。只被软引用关联着的对象，在系统将要发生内存溢出异常
前，会把这些对象列进回收范围之中进行第二次回收，如果这次回收还没有足够的内存，才会抛出内存溢出
异常。在 JDK 1.2 版之后提供了 SoftReference 类来实现软引用。

### 弱引用
弱引用也是用来描述那些非必须对象，但是它的强度比软引用更弱一些，被弱引用关联的对象只能生存到下
一次垃圾收集发生为止。当垃圾收集器开始工作，无论当前 内存是否足够，都会回收掉只被弱引用关联的
对象。在 JDK 1.2 版之后提供了 WeakReference 类来实现弱引用。

### 虚引用
虚引用也称为“幽灵引用”或者“幻影引用”，它是最弱的一种引用关系。一个对象是否有虚引用的存在，完
全不会对其生存时间构成影响，也无法通过虚引用来取得一个对象实例。为一个对象设置虚引用关联的唯一
目的只是为了能在这个对象被收集器回收时收到一个系统通知。在 JDK 1.2 版之后提供了 PhantomReference
类来实现虚引用。

## 使用范例

### 使用软引用实现缓存

首先创建一个String类型的软引用对象方法如下：
```java
SoftReference<String> ref = new SoftReference<String>("Hello world");
```

获取引用对象中的具体值只需调用get()方法即可：
```java
String value = ref.get();
```

所以只需要创建一个SoftReferenceCache工具类，内部留个HashMap来保存内容：
```java
public class SoftReferenceCache<K, V> {
  private final HashMap<K, SoftReference<V>> mCache;
  public SoftReferenceCache() {
    mCache = new HashMap<K, SoftReference<V>>();
  }
}
```

完整实现代码如下：
```java
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * SoftRefenceCache
 * @param <K> key的类型.
 * @param <V> value的类型.
 */
public class SoftReferenceCache<K, V> {
  private final HashMap<K, SoftReference<V>> mCache;
  public SoftReferenceCache() {
    mCache = new HashMap<>();
  }

  /**
   * 将对象放进缓存中，这个对象可以在GC发生时被回收
   * @param key key的值.
   * @param value value的值型.
   */
  public void put(K key, V value) {
    mCache.put(key, new SoftReference<V>(value));
  }

  /**
   * 从缓存中获取value
   * @param key
   * @return 如果找到的话返回value，如果被回收或者压根儿没有就返* 回null
   */
  public V get(K key) {
    V value = null;
    SoftReference<V> reference = mCache.get(key);
    if (reference != null) {
      value = reference.get();
    }
    return value;
  }
}
```

## 参考资料
* [JVM - 引用类型]({% post_url java/jvm/content/2021-08-24-02-jvm-reference-type %})
* [深入理解Java虚拟机（第3版）](https://book.douban.com/subject/34907497/)
* [软引用SoftReference介绍以及简单用法cache](https://www.jianshu.com/p/8c634f10ed1a)
