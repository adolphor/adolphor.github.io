---
layout:     post
title:      Java并发 - CAS与原子变量
date:       2021-08-06 17:53:51 +0800
postId:     2021-08-06-17-53-51
categories: [concurrent]
keywords:   [Java,concurrent]
---

在 Java 并发领域，我们解决并发安全问题最粗暴的方式就是使用 synchronized 关键字了，但它是
一种独占形式的锁，属于悲观锁机制，性能会大打折扣。volatile 貌似也是一个不错的选择，但 
volatile 只能保持变量的可见性，并不保证变量的原子性操作。

CAS 全称是 compare and swap，即比较并交换，它是一种原子操作，同时 CAS 是一种乐观机制。
java.util.concurrent 包很多功能都是建立在 CAS 之上，如 ReenterLock 内部的 AQS，各种
原子类，其底层都用 CAS来实现原子操作。

## 如何解决并发问题
在我们认识 CAS 之前，我们是通过什么来解决并发带来的安全问题呢？
volatile 关键字可以保证变量的可见性，但保证不了原子性；
synchronized 关键字利用 JVM 字节码层面来实现同步机制，它是一个悲观锁机制。

```java
public class AddTest {
  public volatile int i;
  public void add() {
    i++;
  }
}
```

先编译：`javac AddTest.java`，然后通过 `javap -c AddTest` 看看 add 方法的字节码指令：

```java
  public void add();
    Code:
       0: aload_0
       1: dup
       2: getfield      #2                  // Field i:I
       5: iconst_1
       6: iadd
       7: putfield      #2                  // Field i:I
      10: return
```

可以看出 `i++` 被拆分成了几个指令：
* 执行 `getfield` 拿到原始 i；
* 执行 `iadd` 进行加 1 操作；
* 执行 `putfield` 写把累加后的值写回 i。

当线程 1 执行到加 1 步骤时，由于还没有执行赋值改变变量的值，这时候并不会刷新主内存区中的变量，
如果此时线程 2 正好要拷贝该变量的值到自己私有缓存中，问题就出现了，当线程 2 拷贝完以后，线程
1 正好执行赋值运算，立马更新主内存区的值，那么此时线程 2 的副本就是旧的了，脏读又出现了。

怎么解决这个问题呢？在 add 方法加上 synchronized 修饰解决。
```java
public class AddTest {
  public volatile int i;
  public synchronized void add() {
    i++;
  }
}
```

现在完美解决了并发安全问题了，但是这样做性能也会大打折扣。至于 `synchronized` 的具体工作原理，请参考：
[Java并发 - synchronized 关键字]({% post_url java/concurrent/content/2021-07-29-01-concurrent-keyword-synchronized %})

实际上,这里使用锁来保障原子性显得有点杀鸡用牛刀的样子！锁固然是功能最强大、适用范围也很广泛的
同步机制，但是毕竟它的开销也是最大的。另外，volatile 虽然开销小一点，但是它无法保障`count++`
这种自增操作的原子性(这也是我们在前文的代码中使用锁的一个原因)。事实上，保障像自增这种比较简单的
操作的原子性我们有更好的选择一CAS。CAS能够将 `read-modify-write` 和 `check-and-act` 
之类的操作转换为原子操作。

## CAS 底层原理
CAS 的思想很简单：三个参数，一个当前内存值 V、旧的预期值 A、即将更新的值 B，当且仅当预期值 A 
和内存值 V 相同时，将内存值修改为 B 并返回 true，否则什么都不做，并返回 false。

下面以 `AtomicInteger` 的实现为例，分析一下CAS是如何实现的。

```java
public class AtomicInteger extends Number implements java.io.Serializable {

  private static final long serialVersionUID = 6214790243416807050L;

  // setup to use Unsafe.compareAndSwapInt for updates
  private static final Unsafe unsafe = Unsafe.getUnsafe();
  private static final long valueOffset;

  static {
    try {
      valueOffset = unsafe.objectFieldOffset(AtomicInteger.class.getDeclaredField("value"));
    } catch (Exception ex) {
      throw new Error(ex);
    }
  }

  private volatile int value;

  public AtomicInteger() {
  }
  public AtomicInteger(int initialValue) {
    value = initialValue;
  }

  public final int get() {
    return value;
  }
}
```

* Unsafe，是CAS的核心类，由于Java方法无法直接访问底层系统，需要通过本地（native）方法来访问，
  Unsafe相当于一个后门，基于该类可以直接操作特定内存的数据。
* 变量valueOffset，表示该变量值在内存中的偏移地址，因为Unsafe就是根据内存偏移地址获取数据的。
* 变量value用volatile修饰，保证了多线程之间的内存可见性。

看看 `AtomicInteger` 如何实现并发下的累加操作：
```java
    // AtomicInteger 的 getAndAdd 方法：
    public final int getAndAdd(int delta) {
        return unsafe.getAndAddInt(this, valueOffset, delta);
    }
    // unsafe 的 getAndAddInt 方法实现：
    public final int getAndAddInt(Object var1, long var2, int var4) {
      int var5;
      do {
        var5 = this.getIntVolatile(var1, var2);
      } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
      return var5;
    }
    // unsafe 的 compareAndSwapInt 是native方法，该方法的实现位于unsafe.cpp中
    public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
```
```c
UNSAFE_ENTRY(jboolean, Unsafe_CompareAndSwapInt(JNIEnv *env, jobject unsafe, jobject obj, jlong offset, jint e, jint x))
  UnsafeWrapper("Unsafe_CompareAndSwapInt");
  oop p = JNIHandles::resolve(obj);
  jint* addr = (jint *) index_oop_from_field_offset_long(p, offset);
  return (jint)(Atomic::cmpxchg(x, addr, e)) == e;
UNSAFE_END
```

1. 先想办法拿到变量value在内存中的地址。
2. 通过 `Atomic::cmpxchg` 实现比较替换，其中参数x是即将更新的值，参数e是原内存的值。

如果是Linux的x86，`Atomic::cmpxchg` 方法的实现如下：

```c
inline jint Atomic::cmpxchg (jint exchange_value, volatile jint* dest, jint compare_value) {
  int mp = os::is_MP();
  __asm__ volatile (LOCK_IF_MP(%4) "cmpxchgl %1,(%3)"
                    : "=a" (exchange_value)
                    : "r" (exchange_value), "a" (compare_value), "r" (dest), "r" (mp)
                    : "cc", "memory");
  return exchange_value;
}
```

* `__asm__`表示汇编的开始
* `volatile`表示禁止编译器优化
* `LOCK_IF_MP`是个内联函数：
  `#define LOCK_IF_MP(mp) "cmp $0, " #mp "; je 1f; lock; 1: "`

`LOCK_IF_MP`根据当前系统是否为多核处理器决定是否为`cmpxchg`指令添加`lock前缀`。

如果是多处理器，为`cmpxchg`指令添加`lock前缀`。
反之，就省略`lock前缀`。（单处理器会不需要`lock前缀`提供的`内存屏障`效果）

intel手册对`lock前缀`的说明如下：

* 确保后续指令执行的原子性。
  在Pentium及之前的处理器中，带有lock前缀的指令在执行期间会锁住总线，使得其它处理器暂时无法通过总线访问内存，很显然，这个开销很大。在新的处理器中，Intel使用缓存锁定来保证指令执行的原子性，缓存锁定将大大降低lock前缀指令的执行开销。
* 禁止该指令与前面和后面的读写指令重排序。
* 把写缓冲区的所有数据刷新到内存中。

上面的第2点和第3点所具有的内存屏障效果，保证了CAS同时具有volatile读和volatile写的内存语义。


## 参考资料

* [Java并发 - CAS与原子变量]({% post_url java/concurrent/content/2021-08-06-01-concurrent-cas-atomic %})
* [后端进阶 - Java并发之CAS原理分析](https://objcoding.com/2018/11/29/cas/)
* [简书-占小狼 - 深入浅出CAS](https://www.jianshu.com/p/fb6e91b013cc)
* [JVM - javap指令]({% post_url java/jvm/tools/2021-08-22-01-jvm-javap %})
