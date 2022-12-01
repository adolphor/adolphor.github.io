---
layout:     post
title:      Java并发 - ThreadLocal
date:       2021-09-05 18:18:29 +0800
postId:     2021-09-05-18-18-29
categories: [Concurrent]
keywords:   [Java,concurrent]
---

ThreadLocal 提供了线程本地的实例。它与普通变量的区别在于，每个使用该变量的线程都会初始化一个
完全独立的实例副本。ThreadLocal 变量通常被private static修饰。当一个线程结束时，它所使用的
所有 ThreadLocal 相对的实例副本都可被回收。总的来说，ThreadLocal 适用于每个线程需要自己独立
的实例且该实例需要在多个方法中被使用，也即变量在线程间隔离而在方法或类间共享的场景。

## ThreadLocal范例

多个线程访问同一个ThreadLocal对象，相同的key获取的是不同的值，也就是获取的是各自线程独有的值：
1. 访问的是同一个ThreadLocal对象
2. 通过 ThreadLocal 的 get() 方法拿到的是不同的值
3. 通过 ThreadLocal 的 set() 方法不会改变其他线程相同key所对应的值

> ThreadLocalDemo.java

```java
public class ThreadLocalDemo {

  private static ThreadLocal<StringBuilder> counter = ThreadLocal.withInitial(() -> new StringBuilder());

  public static void main(String[] args) throws InterruptedException {
    int threads = 3;
    CountDownLatch countDownLatch = new CountDownLatch(threads);
    InnerClass innerClass = new InnerClass();

    Runnable runnable = () -> {
      innerClass.add(String.valueOf(RandomUtils.nextInt()));
      innerClass.set("hello world");
      countDownLatch.countDown();
    };

    for (int i = 1; i <= threads; i++) {
      new Thread(runnable, "thread - " + i).start();
    }
    countDownLatch.await();
  }

  private static class InnerClass {

    public void add(String newStr) {
      StringBuilder str = counter.get();
      counter.set(str.append(newStr));

      System.out.printf("Thread name:%s , ThreadLocal hashcode:%s, Instance hashcode:%s, Value:%s\n",
        Thread.currentThread().getName(),
        counter.hashCode(),
        counter.get().hashCode(),
        counter.get().toString());
    }

    public void set(String words) {
      counter.set(new StringBuilder(words));
      System.out.printf("Set, Thread name:%s , ThreadLocal hashcode:%s,  Instance hashcode:%s, Value:%s\n",
        Thread.currentThread().getName(),
        counter.hashCode(),
        counter.get().hashCode(),
        counter.get().toString());
    }
  }

}
``` 

## 实现原理

Thread 对象里面会有个 map，用来保存本线程变量：
```java
public class Thread implements Runnable {
  ThreadLocal.ThreadLocalMap threadLocals = null;
}
```
这个 map 是 ThreadLocal 的静态内部类：`ThreadLocalMap`，
现在我们来看一下 ThreadLocalMap 的定义：

```java
static class ThreadLocalMap {
  static class Entry extends WeakReference<ThreadLocal<?>> {
    Object value;
    Entry(ThreadLocal<?> k, Object v) {
      super(k);
      value = v;
    }
  }
  private Entry[] table;
}
```
一般情况下非静态内部类用在内部类，跟其他类无任何关联，专属于这个外部类使用，并且也便于调用外部类的成员变量和方法，比较方便。
而静态外部类其实就等于一个顶级类，可以独立于外部类使用，所以更多的只是表明类结构和命名空间。
所以说这样定义的用意就是说明 ThreadLocalMap 是和 ThreadLocal 强相关的，专用于保存线程本地变量。

说回 ThreadLocalMap，那么 ThreadLocal中变量的set和get操作，主要就是 ThreadLocalMap 的 set 和 get 操作：
```java
// ThreadLocal set 方法：
public void set(T value) {
  Thread t = Thread.currentThread();
  ThreadLocalMap map = getMap(t);
  if (map != null)
  // 这里的set主要就是调用 ThreadLocalMap 的set方法，原理类似于HashMap的hash求址，只不过这里用的是开放寻址法
    map.set(this, value);
  else
    createMap(t, value);
}
void createMap(Thread t, T firstValue) {
  t.threadLocals = new ThreadLocalMap(this, firstValue);
}

// ThreadLocal get 方法：
public T get() {
  Thread t = Thread.currentThread();
  // 调用 ThreadLocalMap 的 get 方法
  ThreadLocalMap map = getMap(t);
  if (map != null) {
    ThreadLocalMap.Entry e = map.getEntry(this);
    if (e != null) {
      @SuppressWarnings("unchecked")
      T result = (T)e.value;
      return result;
    }
  }
  return setInitialValue();
}
```

## 内存泄露
上面的实现源码中可以看到，Map 由 ThreadLocal 类的静态内部类 ThreadLocalMap 提供。该类的
实例维护某个 ThreadLocal 与具体实例的映射。与 HashMap 不同的是，ThreadLocalMap 的每个 
Entry 都是一个对 键 的弱引用，这一点从super(k)可看出。另外，每个 Entry 都包含了一个对 值 
的强引用。

使用弱引用的原因在于，当没有强引用指向 ThreadLocal 变量时，它可被回收，从而避免上文所述 
ThreadLocal 不能被回收而造成的内存泄漏的问题。

但是，这里又可能出现另外一种内存泄漏的问题。ThreadLocalMap 维护 ThreadLocal 变量与具体
实例的映射，当 ThreadLocal 变量被回收后，该映射的键变为 null，该 Entry 无法被移除。从而
使得实例被该 Entry 引用而无法被回收造成内存泄漏。

针对该问题，ThreadLocalMap 的 set 方法中，通过 replaceStaleEntry 方法将所有键为 null 
的 Entry 的值设置为 null，从而使得该值可被回收。另外，会在 rehash 方法中通过 expungeStaleEntry 
方法将键和值为 null 的 Entry 设置为 null 从而使得该 Entry 可被回收。通过这种方式，ThreadLocal 
可防止内存泄漏。

```java
private void set(ThreadLocal<?> key, Object value) {
    Entry[] tab = table;
    int len = tab.length;
    int i = key.threadLocalHashCode & (len-1);

    for (Entry e = tab[i];
         e != null;
         e = tab[i = nextIndex(i, len)]) {
        ThreadLocal<?> k = e.get();

        if (k == key) {
            e.value = value;
            return;
        }

        if (k == null) {
            replaceStaleEntry(key, value, i);
            return;
        }
    }

    tab[i] = new Entry(key, value);
    int sz = ++size;
    if (!cleanSomeSlots(i, sz) && sz >= threshold)
        rehash();
}
```

![weakreference]({{ site.baseurl }}/image/post/2021/09/05/01/weak-reference.jpg)

## 参考资料
* [Java并发 - ThreadLocal]({% post_url java/concurrent/content/2021-09-05-01-java-concurrent-threadLocal %})
* [是Yes呀 —— ThreadLocal 能问的，都写了](https://zhuanlan.zhihu.com/p/404403218)
* [万字详解ThreadLocal关键字](https://snailclimb.gitee.io/javaguide/#/docs/java/multi-thread/万字详解ThreadLocal关键字)
