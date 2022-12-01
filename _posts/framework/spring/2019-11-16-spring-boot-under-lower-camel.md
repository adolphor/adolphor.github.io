---
layout:     post
title:      spring-boot 下划线和驼峰转换
date:       2019-11-16 09:39:32 +0800
postId:     2019-11-16-09-39-32
categories: [Spring]
keywords:   [Microservice, Spring]
---

spring boot 框架中，下划线和驼峰相互转换问题：

* 返回数据：驼峰转下划线
    - 如果使用的是Java bean，只需要配置文件配置即可全局生效
    
* 接收前端传参：下划线转驼峰，使用实体类接收
    - POST请求且使用RAW的json形式：自动转换，无需配置
    - 其他请求（包括POST请求的form表单提交形式）：需要自己特殊处理

## 返回数据
配置内容：

```yaml
spring:
  jackson:
    default-property-inclusion: NON_NULL # 过滤null属性
    property-naming-strategy: SNAKE_CASE # 驼峰转为下划线
    date-format: yyyy-MM-dd HH:mm:ss     # 日期格式化
    time-zone: GMT+8                     # 时区
```

## 接收前端传参

### POST请求且RAW请求
无需自己处理，参考[为spring get请求添加自定义的参数处理（如下划线转驼峰）](https://blog.csdn.net/qq_36752632/article/details/90665221)。

> 为了减少使用 @RequestPath  将get参数封装到实体类中 重写ModelAttributeMethodProcessor。
> 注：由于get请求为非raw请求，spring默认使用@ModelArrtribute注解，不会自动将下划线的数据转为驼峰数据
> 所以需要自定义一个处理器，进行该操作 *

### 其他请求

需要自己写处理器，如果通过继承 `WebMvcConfigurationSupport` 来实现，就会引发了另外一个问题，yaml配置文件中的配置信息会失效，原因如下：

* [继承WebMvcConfigurationSupport后自动配置不生效的问题及如何配置拦截器](https://blog.csdn.net/qq_36850813/article/details/87859047)
* [springboot 2.0配置文件不生效原因](https://www.dockop.com/article/17)

解决方法是通过继承 `WebMvcConfigurer` 来覆写 `addArgumentResolvers` 实现：

```java
import com.joyoung.base.boot.utils.spring.UnderlineToCamelArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Hongbo.Zhu
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 添加参数解析，将参数的形式从下划线转化为驼峰
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new UnderlineToCamelArgumentResolver());
    }
}
```

## 其他JSONObject配置

在spring mvc框架中默认的是jackson工具包进行序列化相关转换，
在实际项目中，还使用了阿里的JSONObject工具类进行了下划线和驼峰的转换，用于数据中心数据查询后的转换，

```java
@JSONField(name = "create_time")
private String createUser;

// 转换的时候根据上面的配置进行转换
MenuInfoVo menu = dataCenter.query(sql, sqlParam(id), MenuInfoVo.class);
```

## 参考资料


## 代码

> UnderlineToCamelArgumentResolver.java

```java
import com.joyoung.smart.base.exceptions.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.joyoung.smart.base.constant.ResponseError.PARAM_RESOLVE_ERR;

@Slf4j
public class UnderlineToCamelArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 匹配下划线的格式
     */
    private static final Pattern pattern = Pattern.compile("_(\\w)");

    private static String underLineToCamel(String source) {
        Matcher matcher = pattern.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        // return methodParameter.hasParameterAnnotation(ParamModel.class);
        // 全局使用的话，直接返回true
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws ParamException {
        Object obj;
        try {
            obj = parameter.getParameterType().newInstance();
        } catch (Exception e) {
            log.error("无法实例化的数据类型：{}", parameter.getParameterType());
            throw new ParamException(PARAM_RESOLVE_ERR);
        }
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        Iterator<String> paramNames = webRequest.getParameterNames();
        while (paramNames.hasNext()) {
            String paramName = paramNames.next();
            Object o = webRequest.getParameter(paramName);
            try {
                wrapper.setPropertyValue(underLineToCamel(paramName), o);
            } catch (BeansException e) {
                log.info("下划线转驼峰时出错，实体类 {} 中无对应属性：{}", o.getClass().getName(), paramName);
            }
        }
        return obj;
    }
}
```

* [test](test.html)
