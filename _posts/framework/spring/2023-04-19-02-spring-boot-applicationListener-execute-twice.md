---
layout:     post
title:      SpringBoot ApplicationListener 监听事件触发两次的问题
date:       2023-04-19 15:07:22 +0800
postId:     2023-04-19-15-07-22
categories: [Spring]
keywords:   [Spring]
---

封装RocketMQ公共组件的时候，首先尝试使用Spring的IOC托管机制，后来发现配置文件读取、Spring实例工厂定义、bean实例初始化 等时间节点较难控制，
所以考虑使用工具类的静态方法中托管相关实例进行实现，但在项目启动的 ApplicationListener 中配置监听事件的时候，发现会被执行两次，采用如下的
方式可以解决这个问题。

## 问题原因
在web项目中如果同时集成了Spring和SpringMVC的话，上下文中会存在两个容器，即Spring的applicationContext.xml的父容器和
SpringMVC的applicationContext-mvc.xml的子容器。这两个容器有相同的生命周期，所以同一个事件，在不同容器启动过程中都会发送一次。
如此，明白了多出的一次事件是引入了SpringMVC造成的。基于第一节中的时间执行顺序，我们可以得出这样的结论：

root容器启动开始 –> 创建子容器mvc并启动 –> 子容器mvc启动完成 –> root容器继续启动 –> root容器启动完成

## 原方案
```java
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqConsumeInit implements ApplicationListener<ApplicationReadyEvent> {
    private final MqListenerDemo mqListenerDemo;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        RocketMqUtil.consumer("platform-consumer", mqListenerDemo);
    }
}
```

## 改正后的方案
通过比对 ApplicationContext 的实例类型来判断：

```java
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqConsumeInit implements ApplicationListener<ApplicationReadyEvent>, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private final MqListenerDemo mqListenerDemo;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (event.getApplicationContext().equals(this.applicationContext)) {
            RocketMqUtil.consumer("platform-consumer", mqListenerDemo);
        }
    }
}
```

## 参考资料
* [SpringBoot ApplicationListener 监听事件触发两次的问题]({% post_url framework/spring/2023-04-19-02-spring-boot-applicationListener-execute-twice %})
* [Why Spring Boot Application logs that it started twice](https://stackoverflow.com/questions/47344882/why-spring-boot-application-logs-that-it-started-twice-after-adding-spring-cloud)
* [Spring事件处理——onApplicationEvent执行两次](https://blog.csdn.net/u014453515/article/details/85268526)
* [Spring事件发布与监听机制](https://bbs.huaweicloud.com/blogs/281015)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/04/19/02/xxx.png)
```
