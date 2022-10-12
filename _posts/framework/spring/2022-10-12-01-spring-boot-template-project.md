---
layout:     post
title:      spring-boot 项目模板
date:       2022-10-12 11:34:05 +0800
postId:     2022-10-12-11-34-05
categories: [framework]
keywords:   [Spring]
---

简便SpringBoot项目开发，直接参考下面配置即可。


## Java编译
```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
</properties>
```
以及：
```xml
<build>
    <finalName>${project.artifactId}</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## build设置

### 项目名称
配置 `<finalName>${project.artifactId}</finalName>`

### Java版本
增加 `maven-compiler-plugin` 插件

### 源码打包
增加 `maven-source-plugin` 插件

### deploy过滤
增加 `maven-deploy-plugin` 插件

### spring boot 启动入口
增加 `spring-boot-maven-plugin` 插件

### build汇总配置

```xml
<build>
    <finalName>${project.artifactId}</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-source-plugin</artifactId>
            <version>3.2.0</version>
            <executions>
                <execution>
                    <id>attach-sources</id>
                    <phase>verify</phase>
                    <goals>
                        <goal>jar-no-fork</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>2.1.3.RELEASE</version>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-deploy-plugin</artifactId>
            <version>2.8.2</version>
            <configuration>
                <skip>true</skip>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 返回值自动封装

### 配置全局过滤

> GlobalResponseHandler.java
```java
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private static final JSONObject empty = new JSONObject();

    //判断支持的类型
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 检查注解是否存在，存在则忽略拦截
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        // 忽略 springfox-swagger 相关请求，解决swagger请求异常的BUG
        if (methodParameter.getDeclaringClass().getName().contains("springfox")) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 判断为null构建ResponseData对象进行返回
        if (o == null) {
            return Result.success(empty);
        }
        // 避免重复封装
        if (o instanceof Result) {
            return o;
        }
        return Result.success(o);
    }
}
```
### 手动去掉封装

> IgnoreResponseAdvice.java
```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {
}
```

## 单元测试

> pom.xml
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

> StudentTest.java
```java
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JoyoungFunctionsDemoApplication.class)
public class StudentTest {

    @Autowired
    private StudentDao StudentDao;

    @Test
    public void select(){
        Student user = StudentDao.selectById(1);
        Assertions.assertEquals("aa", user.getName());
    }
}
```

## logback
增加 `janino` 依赖：
```xml
<!--logback依赖包-->
<dependency>
    <groupId>org.codehaus.janino</groupId>
    <artifactId>janino</artifactId>
    <version>2.6.1</version>
</dependency>
```
配置文件参考：
[Java项目logback日志组件]({% post_url java/log/content/2022-07-07-01-logback-component-for-java %})

## swagger
如果配置了全局过滤，比如返回值自动封装code和msg信息，可能会导致swagger请求异常，需要过滤
swagger相关请求，不需要进行结果的封装：

```java
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 忽略 springfox-swagger 相关请求，解决swagger请求异常的BUG
        if (methodParameter.getDeclaringClass().getName().contains("springfox")) {
            return false;
        }
        return true;
    }
}
```

## 参考资料
* [spring-boot 项目模板]({% post_url framework/spring/2022-10-12-01-spring-boot-template-project %})
