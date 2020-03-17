---
layout:     post
title:      初识 java agent
date:       2018-12-11 22:11:52 +0800
postId:     2018-12-11-22-11-53
categories: [article]
tags:       [Java]
geneMenu:   true
excerpt:    初识 java agent
---

## 起由

网易技术人员来介绍轻舟微服务架构的时候，介绍底层架构中使用了 spring-boot 和 java-agent结合的方式，
来进行代码层面的解耦，具体服务项目就是单纯的spring boot 项目，而服务注册和发现等微服务架构相关逻辑实现，
使用java agent 的形式进行加载和融合，所以就去了解下 java agent 相关的实现原理。

## 

JavaAgent 是JDK 1.5 以后引入的，也可以叫做Java代理。

JavaAgent 是运行在 main方法之前的拦截器，它内定的方法名叫 premain ，也就是说先执行 premain 方法然后再执行 main 方法。

那么如何实现一个 JavaAgent 呢？很简单，只需要增加 premain 方法即可。

```java
package demo;
import java.lang.instrument.Instrumentation;
public class MyAgent {
  public static void premain(String agentOps, Instrumentation inst) {
    System.out.println("=========premain方法执行========");
    System.out.println(agentOps);
  }
  public static void premain(String agentOps) {
    System.out.println("=========premain方法执行2========");
    System.out.println(agentOps);
  }
}
```

在 src 目录下添加 `META-INF/MANIFEST.MF` 文件，内容按如下定义：
```log
Manifest-Version: 1.0
Premain-Class: com.shanhy.demo.agent.MyAgent
Can-Redefine-Classes: true

```
要特别注意，一共是四行，第四行是空行，还有就是冒号后面的一个空格。



## 参考资料

* [JavaAgent 简单例子](https://blog.csdn.net/catoop/article/details/51034739)