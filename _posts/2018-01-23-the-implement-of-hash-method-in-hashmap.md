---
layout:   post
title:    HashMap中的hash方法实现解析
date:     2018-01-23 16:00:14 +0800
postId:   2018-01-23-16-00-14
categories: [Java]
tags:     [Java]
geneMenu:   true
excerpt:  HashMap中的hash方法实现解析
---

## 简介
HashMap使用的是哈希表形式的数据结构，为了尽可能的减少哈希碰撞，尽量将对象均匀分布在哈希表中，hash算法就至关重要。

```java
public class HashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
  // 调用方式
  static int indexFor(int h, int length) {
    return h & (length - 1);
  }
}
```

HashMap的默认初始容器大小是16，16进制表示形式就是`0x0000000f`（int最大值占4个字节），如果使用Object默认的hash算法，h值在高位不同但低位
相同的情况下，经过`h & (length - 1)`计算中会后，得到的下标位置却相同。

比如：`0x0f470000`和`0x14bc0000`经过计算之后，会得到相同的结果0。具体过程如下:

```java
public class IndexForDemo {
  public static void main(String[] args) {
    // 随便两个高位不同低位相同的int数据作为key的原始hash值
    int key1 = 0x0f470000;
    int key2 = 0x14bc0000;
    printBin(key1);
    printBin(key2);
    // 16 的 16进制表示
    int lenght = 0x0000000f;
    int hk1 = indexFor(key1, lenght);
    int hk2 = indexFor(key2, lenght);
    System.out.println(hk1);
    System.out.println(hk2);
  }
  static int indexFor(int h, int length) {
    return h & (length - 1);
  }
  static void printBin(int h) {
    System.out.println(String.format("%32s", Integer.toBinaryString(h)).replace(' ', '0'));
  }
}
```

结果：

```text
00001111010001110000000000000000 <= 0x0f470000
00010100101111000000000000000000 <= 0x14bc0000
0 => 下标位置 0
0 => 下标位置 0
```

也就是说 `0x0f470000` 和 `0x14bc0000` 虽然hash值不同，但计算出来的下标的位置却是相同的，产生了hash碰撞。为了解决这种问题，JDK中对此
hash值 h 进行了二次hash（"扰动函数"），混合原始哈希码的高位和低位，以此来加大低位的随机性。而且混合后的低位掺杂了高位的部分特征，这样高位
的信息也被变相保留下来。

## JDK6 HashMap的hash方法实现

```java
public class HashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
  // 实现方式
  static int hash(int h) {
    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h >>> 7) ^ (h >>> 4);
  }
  // 调用方式
  static int indexFor(int h, int length) {
    return h & (length - 1);
  }
}
```

这段hash函数实现代码是为了对key的hashCode进行扰动计算，防止不同hashCode的高位不同但低位相同导致的hash冲突。将上述实现拆分成单个操作步骤
如下所示：

```java
public class HashMethodOfJDK6 {
  public static void main(String[] args) {
    int h = 0xffffffff;
    // bit shift 右移操作
    int h1 = h >>> 20;
    int h2 = h >>> 12;
    // bitwise XOR 异或操作
    int h3 = h1 ^ h2;
    int h4 = h ^ h3;
    int h5 = h4 >>> 7;
    int h6 = h4 >>> 4;
    int h7 = h5 ^ h6;
    int h8 = h4 ^ h7;

    printBin(h);
    printBin(h1);
    printBin(h2);
    printBin(h3);
    printBin(h4);
    printBin(h5);
    printBin(h6);
    printBin(h7);
    printBin(h8);
  }
  static void printBin(int h) {
    System.out.println(String.format("%32s", Integer.toBinaryString(h)).replace(' ', '0'));
  }
}
```

每步操作之后的结果如下：

```text
11111111111111111111111111111111 <= 0xffffffff 对应的二进制
00000000000000000000111111111111 <= h1：右移20位
00000000000011111111111111111111 <= h2：右移12位
00000000000011111111000000000000 <= h3：h1和h2异或结果
11111111111100000000111111111111 <= h4：h和h3异或结果
00000001111111111110000000011111 <= h5：h4右移7位
00001111111111110000000011111111 <= h6：h4右移4位
00001110000000001110000011100000 <= h7：h5和h6异或结果
11110001111100001110111100011111 <= h8：h4和h7异或结果
```

使用此扰动函数对上面的`0x0f470000`和`0x14bc0000`进行处理：

```java
public class ReHashDemo {
  public static void main(String[] args) {
    int key1 = 0x0f470000;
    int key2 = 0x14bc0000;
    int length = 0x0000000f;
    int rk1 = hash(key1);
    int rk2 = hash(key2);
    printBin(rk1);
    printBin(rk2);
    int index1 = indexFor(rk1, length);
    int index2 = indexFor(rk2, length);
    System.out.println(index1);
    System.out.println(index2);
  }
  // 实现方式
  static int hash(int h) {
    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h >>> 7) ^ (h >>> 4);
  }
  // 调用方式
  static int indexFor(int h, int length) {
    return h & (length - 1);
  }
  static void printBin(int h) {
    System.out.println(String.format("%32s", Integer.toBinaryString(h)).replace(' ', '0'));
  }
}
```

结果：

```text
00001111101011010000010000100101 <= 0x0f470000
00010101110111111110010010110110 <= 0x14bc0000
4 => 下标值 4
6 => 下标值 6
```

可以看出，扰动函数已经产生了效果，将之前全部是0位置的对象重新规划到了4和6位置。

## JDK7 HashMap的hash方法实现

```java
public class HashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
  transient int hashSeed = 0;
  final int hash(Object k) {
    int h = hashSeed;
    if (0 != h && k instanceof String) {
      return Hashing7.stringHash32((String) k);
    }
    h ^= k.hashCode();
    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h >>> 7) ^ (h >>> 4);
  }
}
```
在这里可以看到，基本算法和JDK6一致，多了个hashSeed判断，暂且没有深入研究，后续再看吧。

## JDK8 HashMap的hash方法实现

```java
public class HashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
	static final int hash(Object key) {
		int h;
		return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	}
}
```

与上面JDK6和JDK7的实现方式相比，进行了大量的简化，将四次唯一操作和两次异或操作，简化成了一次一位和一次异或操作。直接将哈希码对半分之后进行
异或操作，也覆盖到了全部的高低位。

## JDK9 HashMap的hash方法实现

与JDK8实现相同，略略略……

## 总结

本文主要是学习了JDK源码中hash方法实现，目的是尽可能让对象均匀分布在哈希表中，手段是混淆高低位重新计算哈希码，DONE。


## 参考资料

* [StackOverflow - Understanding strange Java hash function](https://stackoverflow.com/questions/9335169/understanding-strange-java-hash-function)
* [知乎 - HashMap中的hash实现](https://www.zhihu.com/question/51784530)
