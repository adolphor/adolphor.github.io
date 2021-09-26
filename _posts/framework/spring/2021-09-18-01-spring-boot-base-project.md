---
layout:     post
title:      Spring boot 脚手架相关
date:       2021-09-18 15:55:50 +0800
postId:     2021-09-18-15-55-50
categories: [framework]
keywords:   [Spring]
---

## 扫描注解
再spring整合mybatis的时候，有两个注解需要注意：

### @SpringBootApplication(scanBasePackages ={})
scanBasePackages 中配置的扫描路径，只加载 @RestController、@Service、@Repository
等Spring中的注解，但是Mybatis的组件的注解，Spring扫描到了也不会加载解析。

### @MapperScan
所有MyBatis的接口，都需要配置在这个扫描路径中，这样的话，所有Dao接口类，才会被加载生成操作
数据库的代理类


## UTF-8编码
从Spring boot 2.2 开始，对于HTTP请求的默认编码不再是UTF-8，即便在 `HttpMessageConverters`
等序列化类中指定了UTF-8编码，但返回的HTTP返回头中的值依然是 `Content-Type: application/json`，
而不是 `Content-Type: application/json;charset=UTF-8`，如果需要显示指定返回头中的编码是
UTF-8，需要在项目的 `bootstrap.yml` 配置文件中，添加如下配置：
```yaml
spring:
  http:
    encoding:
      enabled: true
      force: true
      charset: UTF-8
```









```
![image-alter]({{ site.baseurl }}/image/post/2021/09/18/01/xxx.jpg)
```

## 参考资料
* [Spring boot 脚手架相关]({% post_url framework/spring/2021-09-18-01-spring-boot-base-project %})
