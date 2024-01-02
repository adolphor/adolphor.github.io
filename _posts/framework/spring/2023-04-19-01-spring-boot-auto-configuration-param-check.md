---
layout:     post
title:      SpringBoot 自动加载配置的参数校验
date:       2023-04-19 14:57:16 +0800
postId:     2023-04-19-14-57-16
categories: [Spring]
keywords:   [Spring]
---

想要SpringBoot项目在启动的时候就检验配置文件中配置的参数是否正确，可以采用如下的参数验证方式。

## 使用方法
* @Validated：类注解，`import org.springframework.validation.annotation.Validated;`
* @NotEmpty：属性注解，`import javax.validation.constraints.NotEmpty;`
* @Valid：嵌套子类对接的属性的注解，`import javax.validation.Valid;`

## 范例

### 配置范例
```java
@Data
@Validated
@ConfigurationProperties(prefix = "rocketmq")
public class ConsumerConfig extends CommonConfig{
    // 消费者身份标志
    @NotEmpty
    private String consumerId;
    // 消费模式：CLUSTERING 或 BROADCASTING
    private String MessageModel;
    // 消费者线程数
    private String consumeThreadNums;
    // 消费者主题和标签
    @Valid
    private List<TopicEntity> topics;
    // 是否顺序消费
    private Boolean orderly = false;
}
```

### 报错信息
```log
***************************
APPLICATION FAILED TO START
***************************

Description:

Binding to target org.springframework.boot.context.properties.bind.BindException: Failed to bind properties under 'rocketmq' to com.adolphor.cloud.common.rocketmq.conf.RocketMqAutoConfig failed:

    Property: rocketmq.consumer[0].consumerId
    Value: null
    Reason: 不能为空


```

## 参考资料
* [SpringBoot 自动加载配置的参数校验]({% post_url framework/spring/2023-04-19-01-spring-boot-auto-configuration-param-check %})
* [Validate Spring Boot Configuration Parameters at Startup](https://reflectoring.io/validate-spring-boot-configuration-parameters-at-startup/)
