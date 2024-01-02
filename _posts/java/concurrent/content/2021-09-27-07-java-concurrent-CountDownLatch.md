---
layout:     post
title:      Java并发 - CountDownLatch
date:       2021-09-27 15:57:19 +0800
postId:     2021-09-27-15-57-19
categories: [Concurrent]
keywords:   [Java,concurrent]
---

CountDownLatch 的作用就是 允许 count 个线程阻塞在一个地方，直至所有线程的任务都执行完毕。
之前在项目中，有一个使用多线程读取多个文件处理的场景，我用到了 CountDownLatch 。具体场景是下面这样的：

我们要读取处理 6 个文件，这 6 个任务都是没有执行顺序依赖的任务，但是我们需要返回给用户的时候
将这几个文件的处理的结果进行统计整理。

为此我们定义了一个线程池和 count 为 6 的CountDownLatch对象 。使用线程池处理读取任务，
每一个线程处理完之后就将 count-1，调用CountDownLatch对象的 await()方法，直到所有文件读取完之后，
才会接着执行后面的逻辑。


开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2021/09/27/07/xxx.jpg)
```

## 参考资料
* [Java并发 - CountDownLatch]({% post_url java/concurrent/content/2021-09-27-07-java-concurrent-CountDownLatch %})
* [用过 CountDownLatch 么？什么场景下用的？](https://snailclimb.gitee.io/javaguide/#/docs/java/multi-thread/Java并发进阶常见面试题总结?id=_64-用过-countdownlatch-么？什么场景下用的？)
