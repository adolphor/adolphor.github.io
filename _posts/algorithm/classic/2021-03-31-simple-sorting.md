---
layout:     post
title:      Java数据结构和算法 - 简单排序
date:       2021-03-31 10:47:04 +0800
postId:     2021-03-31-10-47-04
categories: [system]
tags:       [数据结构和算法]
geneMenu:   true
excerpt:    Java数据结构和算法 - 简单排序
---

## 冒泡排序
冒泡排序算法运行起来非常慢，但在概念上他是排序算法中最简单的，因为冒泡排序算法在刚开始研究排序技术时是一个非常好的算法。

### 排序思路
* 首先确定，最大的放在最右边
* 外层循环从右到左，逐步缩小范围
* 内层循环从小到大，一直到外层最大下标
* 每次比较大小，大的放在右边

### 动图展示

![冒泡排序]({{ site.baseurl }}/image/post/2021/03/31/冒泡排序.gif)

### 核心代码
```java
  /**
   * 冒泡核心算法
   */
  @Override
  public void sort() {
    int out, in;
    for (out = data.length - 1; out > 1; out--) {    // outer loop (backward)
      for (in = 0; in < out; in++) {                 // inner loop (forward)
        if (data[in] > data[in + 1]) {               // out of order ?
          swap(in, in + 1);                          // swap them
        }
      }
    }
  }
```
一般来说，数组中有N个数据项，则第一趟排序中有N-1次比较，第二趟中有N-2次，如此类推。这种序列的求和公式如下：
`(N-1)+(N-2)+(N-3)+...+1=N*(N-1)/2`，忽略常数1，约等价于 N*N/2，所以，算法做了约 `N*N/2` 次比较。
因为数据是随机的，所以左边数据大于右边数据的概率大概是1/2，也就是说只有一半的数据需要交换，那么交换的次数
就是比较次数的1/2，也就是 `N*N/4`（不过在最坏的情况下，即初始数据逆序时，每次比较都需要交换）。
交换和比较次数都和 `N*N` 成正比，由于常数不算在大O表示法中，可以忽略2和4，并且认为冒泡排序运行需要 `O(N*N/2)`
时间级别。


## 选择排序

选择排序的比较次数和冒泡排序一样，但是交换次数会减少很多，每次遍历最多只交换一次即可。

### 排序思路
* 首先确定，最小的放在最左边
* 外层循环从左到右，逐步缩小范围
* 内层循环从外层最小下标，一直到最后
* 比较大小，记住最小值下标，比较完毕之后交换外层和最小值

### 动图展示
![选择排序]({{ site.baseurl }}/image/post/2021/03/31/选择排序.gif)

### 核心代码

```java
  /**
   * 选择排序核心算法
   */
  @Override
  public void sort() {
    int min, out, in;
    for (out = 0; out < data.length - 1; out++) {        // outer loop
      min = out;                                         // minimum
      for (in = out + 1; in < data.length; in++) {       // inner loop
        if (data[in] < data[min]) {                      // if min greater
          min = in;                                      // we have a new min
        }
      }
      swap(out, min);                                    // swap them
    }
  }
```

## 插入排序

### 排序思路
* 外层for循环中，out变量从1开始向右移动，它标记了未排序部分的最左端的数据
* 内层的while循环中，in变量从out变量开始，向左移动，直到temp变量小于in所指的数组数据项，或者它已经不能再往左移动为止
* while循环的每一趟都向右移动了一个已排序的数据项

### 动图展示

![插入排序]({{ site.baseurl }}/image/post/2021/03/31/插入排序.gif)

### 核心代码
```java
  /**
   * 插入排序核心算法
   */
  @Override
  public void sort() {
    int out, in;
    for (out = 1; out < data.length; out++) {   // out is dividing line
      int temp = data[out];                     // remove marked item    临时存储当前操作的需要插入的值
      in = out;                                 // start shifts at out   内部循环开始位置
      while (in > 0 && data[in - 1] >= temp) {  // until one is smaller  判断是否需要移动
        data[in] = data[in - 1];                // shift item right      将左边的值右移腾出插入的位置，被覆盖的值已经存储在temp或者已经右移到新位置了
        --in;                                   // go left one position  迭代操作位置左移
      }
      data[in] = temp;                          // insert marked item    将需要插入的值放在插入的位置
    }
  }
```

## 参考资料

* [Java数据结构和算法](https://book.douban.com/subject/1144007/)
