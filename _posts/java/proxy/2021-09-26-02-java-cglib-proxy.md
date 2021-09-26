---
layout:     post
title:      Java cglib 代理
date:       2021-09-26 17:00:00 +0800
postId:     2021-09-26-17-00-00
categories: [proxy]
keywords:   [Java, proxy]
---
JDK 动态代理有一个最致命的问题是其只能代理实现了接口的类，CGLIB 动态代理机制就可以避免这个限制。
CGLIB(Code Generation Library)是一个基于ASM的字节码生成库，它允许我们在运行时对字节码进行修改和动态生成。CGLIB 通过继承方式实现代理。
很多知名的开源框架都使用到了CGLIB， 例如 Spring 中的 AOP 模块中：如果目标对象实现了接口，
则默认采用 JDK 动态代理，否则采用 CGLIB 动态代理。

## CGLIB 动态代理机制

在 CGLIB 动态代理机制中 MethodInterceptor 接口和 Enhancer 类是核心。

### MethodInterceptor
你需要自定义 MethodInterceptor 并重写 intercept 方法，intercept 用于拦截增强被代理类的方法。
1. obj :被代理的对象（需要增强的对象）
2. method :被拦截的方法（需要增强的方法）
3. args :方法入参
4. proxy :用于调用原始方法

```java
public interface MethodInterceptor extends Callback{
    // 拦截被代理类中的方法
    public Object intercept(Object obj, Method method, Object[] args,
                               MethodProxy proxy) throws Throwable;
}
```

### Enhancer

你可以通过 Enhancer类来动态获取被代理类，当代理类调用方法的时候，实际调用的是 MethodInterceptor 中的 intercept 方法。

## CGLIB 动态代理类使用步骤
1. 定义一个类；
2. 自定义 MethodInterceptor 并重写 intercept 方法，intercept 用于拦截增强被代理类的方法，和 JDK 动态代理中的 invoke 方法类似；
3. 通过 Enhancer 类的 create()创建代理类；

## 代码示例
不同于 JDK 动态代理不需要额外的依赖。CGLIB(Code Generation Library) 实际是属于一个开源项目，
如果你要使用它的话，需要手动添加相关依赖。

```xml
<dependency>
  <groupId>cglib</groupId>
  <artifactId>cglib</artifactId>
  <version>3.3.0</version>
</dependency>
```

### 实现一个使用阿里云发送短信的类

```java
public class AliSmsService {
  public String send(String message) {
    System.out.println("send message: " + message);
    return message;
  }
}
```

### 自定义 MethodInterceptor（方法拦截器）
```java
public class DebugMethodInterceptor implements MethodInterceptor {

  /**
   * @param o           代理对象（增强的对象）
   * @param method      被拦截的方法（需要增强的方法）
   * @param args        方法入参
   * @param methodProxy 用于调用原始方法
   */
  @Override
  public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
    //调用方法之前，我们可以添加自己的操作
    System.out.println("before method " + method.getName());
    Object object = methodProxy.invokeSuper(o, args);
    //调用方法之后，我们同样可以添加自己的操作
    System.out.println("after method " + method.getName());
    return object;
  }
}
```
### 获取代理类
```java
public class CglibProxyFactory {
  public static Object getProxy(Class<?> clazz) {
    // 创建动态代理增强类
    Enhancer enhancer = new Enhancer();
    // 设置类加载器
    enhancer.setClassLoader(clazz.getClassLoader());
    // 设置被代理类
    enhancer.setSuperclass(clazz);
    // 设置方法拦截器
    enhancer.setCallback(new DebugMethodInterceptor());
    // 创建代理类
    return enhancer.create();
  }
}
```

### 实际使用
```java
AliSmsService aliSmsService = (AliSmsService) CglibProxyFactory.getProxy(AliSmsService.class);
aliSmsService.send("java");
```

## JDK 动态代理和 CGLIB 动态代理对比

* JDK 动态代理只能代理实现了接口的类或者直接代理接口，而 CGLIB 可以代理未实现任何接口的类。 另外， CGLIB 动态代理是通过生成一个被代理类的子类来拦截被代理类的方法调用，因此不能代理声明为 final 类型的类和方法。
* 就二者的效率来说，大部分情况都是 JDK 动态代理更优秀，随着 JDK 版本的升级，这个优势更加明显。



```
![image-alter]({{ site.baseurl }}/image/post/2021/09/26/02/xxx.jpg)
```

## 参考资料
* [Java cglib 代理]({% post_url java/proxy/2021-09-26-02-java-cglib-proxy %})
* [Java JDK 代理]({% post_url java/proxy/2021-09-26-01-java-jdk-proxy %})
* [代理模式详解](https://github.com/Snailclimb/JavaGuide/blob/master/docs/java/basis/代理模式详解.md)
