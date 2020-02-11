---
layout:     post
title:      Java源码-并发包 ScheduledExecutorService
date:       2020-02-09 14:46:19 +0800
postId:     2020-02-09-14-46-19
categories: [Java]
tags:       [Java,concurrent]
geneMenu:   true
excerpt:    Java源码-并发包 ScheduledExecutorService
---

```java
public class ScheduledExecutorServiceDemo {

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    BasicThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build();
    ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, threadFactory);

    ScheduledFuture scheduledFuture = executorService.schedule((Callable) () -> {
      System.out.println("Executed!");
      return "Called!";
    }, 5, TimeUnit.SECONDS);

    Object result = scheduledFuture.get();
    System.out.println("result = " + result);

  }
}
```

## 参考资料

* [Java并发包：ScheduledExecutorService](https://blog.csdn.net/zxc123e/article/details/51911652)