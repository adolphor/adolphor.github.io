---
layout:     post
title:      Java数据结构和算法 - 高级排序
date:       2021-04-23 15:04:03 +0800
postId:     2021-04-23-15-04-03
categories: [algorithm]
keywords:   [数据结构和算法]
---
## 高级排序
前面学习过简单排序：冒泡排序、选择排序、插入排序，都是一些容易实现但速度比较慢的排序算法；
在递归学习中，讲述了 归并排序，速度比简单排序要快，但是他需要的空间是原始数组空间的两倍，通常这是一个严重的缺点。

## 希尔排序
希尔排序是因计算机科学家Donald L. Shell 而得名，他在1959年发现了希尔排序算法。
希尔排序基于插入排序，但是增加了一个新的特性，大大地提高了插入排序的执行效率。

### 排序思路
增加一个间隔区间，这样不需要一个一个的移动过来，而是可以直接移动间隔区间的距离；
但是有另外一问题，就是虽然可以移动一个间隔区间的距离，但可能出现的情况是，
移动间隔区间之后却超过了需要移动的距离，那么下次迭代的时候，还需要再移动回来。
所以，效率的提升有限。

### 动图展示
暂无

### 核心代码
```java
  @Override
  public void sort() {
    int in, out;
    int temp;

    int h = 1;
    while (h <= data.length / 3) {
      h = h * 3 + 1;
    }
    while (h > 0) {
      for (out = h; out < data.length; out++) {
        temp = data[out];
        in = out;
        while (in > h - 1 && data[in - h] >= temp) {
          data[in] = data[in - h];
          in -= h;
        }
        data[in] = temp;
      }
      h = (h - 1) / 3;
    }
  }
```

## 划分
划分是后面将要讨论的快速排序的根本机制，但是划分本身也是一个有用的操作，因此把它作为单独的一节
在这里讲解。
划分，就是把所有数据分为两组，使所有关键字大于特定值的数据项在一组，使所有关键字小于特定值的
数据项在另一组。
注意：划分的两组的元素数量可以不相等，这取决于枢纽以及数据的关键字的值。

### 排序思路
* 设定一个分组值，作为分组的标准
* 从左往右找到比分组值小的值的下标，从右往左找到比分组值大的值的下标，并进行交换
* 循环执行此操作，直至小的值全在左边，大的值全在右边

### 动图展示

### 核心代码
```java
  public int partitionIt(int left, int right, long pivot) {
    int leftPtr = left - 1;
    int rightPtr = right + 1;
    while (true) {
      while (leftPtr < right && data[++leftPtr] < pivot) {
      }
      while (rightPtr > left && data[--rightPtr] > pivot) {
      }
      if (leftPtr >= rightPtr) {
        break;
      } else {
        swap(leftPtr, rightPtr);
      }
    }
    return leftPtr;
  }
```

## 快速排序
快排基于划分的思想，唯一的区别就是：
* 指定数组的最右边元素为比较值，也就是下标为 right
* 分区算法中的分组完毕之后，需要交换一下leftPtr和right值，这样

### 排序思路


### 动图展示
### 核心代码
```java
  @Override
  public void sort() {
    reQuickSort(0, data.length - 1);
  }

  public void reQuickSort(int left, int right) {
    if (right - left <= 0) {
      return;
    } else {
      int pivot = data[right];
      int partition = partitionIt(left, right, pivot);
      reQuickSort(left, partition - 1);
      reQuickSort(partition + 1, right);
    }
  }

  public int partitionIt(int left, int right, int pivot) {
    int leftPtr = left - 1;
    int rightPtr = right;
    while (true) {
      while (leftPtr < data.length && data[++leftPtr] < pivot) {
      }
      while (rightPtr > 0 && data[--rightPtr] > pivot) {
      }
      if (leftPtr >= rightPtr) {
        break;
      } else {
        swap(leftPtr, rightPtr);
      }
    }
    swap(leftPtr, right);
    return leftPtr;
  }
```

## 基数排序

### 排序思路
### 动图展示
### 核心代码


## 参考资料
* [简单排序]({% post_url algorithm/classic/2021-03-31-simple-sorting %})
* [Java数据结构和算法](https://book.douban.com/subject/1144007/)
* [可视化排序过程](https://visualgo.net/zh/sorting)
