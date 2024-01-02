---
layout:     post
title:      记录接口请求日志
date:       2023-10-08 09:41:19 +0800
postId:     2023-10-08-09-41-19
categories: [Spring]
keywords:   [Spring]
---

第三方系统调用的时候需要记录相关请求日志，包括请求参数、请求路径、返回值等信息，
方便后续接口调用次数统计以及异常问题排查，当前主流方式有一下三种：

* Spring AOP
* Spring Interceptor
* Filter

本文主要介绍前两种实现方式。


## 线程缓存
为了记录完整请求时间、以及方便更新请求结果，可以使用ThreadLocal缓存当前日志ID以及请求开始时间。

## Spring AOP

在如下场景使用AOP更加方便：
* HTTP异步调用
* MQ消息调用

### AOP简介
* `@Before`：前置通知，在方法执行之前执行
* `@After`：后置通知，在方法完成后调用通知，无论方法是否执行成功
* `@AfterRunning`：返回通知，在方法返回结果之后执行
* `@AfterThrowing`：异常通知，在方法抛出异常之后
* `@Around`：环绕通知，围绕着方法执行；只有这个注解可以使用 `ProceedingJoinPoint` 参数

### 关键实现
* 定义 HttpReqLogAspect
* 添加注解：@Component、@Aspect、@Order(1)
* 定义切面：@Pointcut("execution(* y2023.m10.d08.reqlog.mvc.controller..*(..))")
* 请求开始时记录请求参数：@Before("webLog()")
  * 获取httpRequest对象：((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest()
  * 获取url参数：request.getParameterMap()
  * 获取body参数：joinPoint.getArgs() 比对 url 参数后多出来的参数就是body参数
  * 缓存当前日志ID：LogParamUtil.setLogId(douyinLog.getId());
* 异常时记录异常结果：@AfterThrowing
* 请求结束时记录返回结果：@AfterReturning

具体代码参考：[HttpReqLogAspect.java]()

## Spring interceptor
使用interceptor可以借助SpringMVC相关工具类，比较方便的获取返回结果、请求Body参数，
在如下场景使用interceptor更加方便：
* HTTP同步调用

### 关键实现
* 定义 LogInterceptor：extends WebRequestHandlerInterceptorAdapter implements ResponseBodyAdvice, RequestBodyAdvice
  * webmvc5以下版本，需要继承 HandlerInterceptorAdapter
  * webmvc6以上版本，需要继承 WebRequestHandlerInterceptorAdapter
  * 实现两个接口：ResponseBodyAdvice, RequestBodyAdvice，借助入口方便获取需要记录的信息
* HandlerInterceptor
  * preHandle
    * 获取url参数：request.getParameterMap()
    * 缓存当前日志ID：LogParamUtil.setLogId(douyinLog.getId());
  * afterCompletion：请求结束，属于 LogInterceptor 最后一个环节
* RequestBodyAdvice：这里处理请求参数
  * supports：只有包含@RequestBody注解的Controller才会进入 supports 方法；只有返回ture才会进入后续body解析
  * beforeBodyRead：解析body参数
  * afterBodyRead：解析body完成后的方法，有需要记录请求参数body的操作，放在这里执行
  * handleEmptyBody：是否特殊处理body为null的参数
* ResponseBodyAdvice：这里处理返回结果
  * supports：都会进入supports方法，只要 supports为true 都会生效
  * beforeBodyWrite：结果返回之前的方法入口，有需要记录返回结果的操作，放在这里执行

具体代码参考：[LogInterceptor.java]()

## 持久化保存
日志文件容易丢失信息，所以考虑采用数据库存储。

> 表结构设计

```mysql
create table request_log
(
    id           bigint PRIMARY KEY auto_increment DEFAULT 1,
    uri          varchar(256)                       not null,
    method       varchar(16)                        not null,
    req_param    text                               not null,
    resp_param   text                               null,
    async_result text                               null,
    was_success  tinyint                            not null,
    create_time  datetime default CURRENT_TIMESTAMP not null,
    update_time  datetime default CURRENT_TIMESTAMP not null
)
comment '接口调用日志' collate = utf8mb4_general_ci;
```

代码参考：
* [RequestLog.java]()
* [RequestLogDao.java]()
* [RequestLogDao.xml]()

## 参考资料
* [使用AOP记录接口请求日志]({% post_url framework/spring/2023-10-08-01-request-log-by-spring %})

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/10/08/01/xxx.png)
```
