---
layout:     post
title:      算法汇总
date:       2020-01-20 17:24:57 +0800
postId:     2020-01-20-17-24-57
categories: []
keywords:   [其他]
---

## 算法评估

* 如何计算算法时间复杂度和空间复杂度
* 算法优缺点
* 每个算法都需要考虑如下几个问题
    - 如何新增一个元素
    - 如何删除一个元素
    - 如何查找一个元素
    - 如何遍历所有元素

### 时间复杂度
* [时间复杂度]({% post_url algorithm/classic/2021-03-30-01-time-complexity %})
### [数组 Array]({% post_url algorithm/classic/2021-03-30-02-array %})
* [无序数组]({% post_url algorithm/classic/2021-03-30-02-array %}#无序数组)
* [有序数组]({% post_url algorithm/classic/2021-03-30-02-array %}#有序数组)
### [简单排序]({% post_url algorithm/classic/2021-03-31-simple-sorting %})
* [冒泡排序]({% post_url algorithm/classic/2021-03-31-simple-sorting %}#冒泡排序)
* [选择排序]({% post_url algorithm/classic/2021-03-31-simple-sorting %}#选择排序)
* [插入排序]({% post_url algorithm/classic/2021-03-31-simple-sorting %}#插入排序)
### 栈和队列
* [栈]({% post_url algorithm/classic/2021-04-07-stack %})
* [队列]({% post_url algorithm/classic/2021-04-10-queue %})
* [中缀、后缀 表达式及计算]({% post_url algorithm/classic/2021-04-13-infix-postfix-expression %})
### [链表]({% post_url algorithm/classic/2021-04-14-01-link %})
* [单链表]({% post_url algorithm/classic/2021-04-14-02-single-ended-list %})
* [双端链表]({% post_url algorithm/classic/2021-04-14-03-double-ended-list %})
* [使用链表实现栈和队列]({% post_url algorithm/classic/2021-04-14-04-stack-queue-by-link %})
* [有序链表]({% post_url algorithm/classic/2021-04-15-01-sorted-link %})
* [双向链表]({% post_url algorithm/classic/2021-04-15-02-doubly-link %})
* [迭代器]({% post_url algorithm/classic/2021-04-15-03-link-iterator %})
### 分治算法
* [递归]({% post_url algorithm/classic/2021-04-15-04-recursion %})
* 归并排序：[参考](https://zhuanlan.zhihu.com/p/95080265)
### [高级排序]({% post_url algorithm/classic/2021-04-23-01-advanced-sorting %})
* [希尔排序]({% post_url algorithm/classic/2021-04-23-01-advanced-sorting %}#希尔排序)
* [快速排序]({% post_url algorithm/classic/2021-04-23-01-advanced-sorting %}#快速排序)
* [基数排序]({% post_url algorithm/classic/2021-04-23-01-advanced-sorting %}#基数排序)
### 二叉树

## 关系图

原文件在boot-note项目存储：

![data-structures-and-algorithm.jpg]({{ site.baseurl }}/image/post/2020/01/20/data-structures-and-algorithm.jpg)

## 模板参数

```java
private static String postTitle = "Java数据结构和算法 - 时间复杂度";
private static String urlTitle = "data-structure-and-algorithms-time-complexity";
private static String categories = "[system]";
private static String tags = "[数据结构和算法]";
private static String folder = "algorithm";
private static Configuration cfg;
```

## 参考资料

* [五分钟学算法：算法与数据结构文章详细分类与整理！](https://www.cxyxiaowu.com/7072.html)
* [Java数据结构和算法](https://book.douban.com/subject/1144007/)
* [算法网](http://ddrv.cn/a/88315) ：比较全面的算法教程，而且是java代码实现
