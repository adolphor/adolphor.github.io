---
layout:     post
title:      Java JDK 代理
date:       2021-09-26 16:59:45 +0800
postId:     2021-09-26-16-59-45
categories: [proxy]
keywords:   [Java, proxy]
---

## JDK 动态代理
在静态代理中我们需要对哪个接口和哪个被代理类创建代理类，所以我们在编译前就需要代理类实现与被代理类相同的接口，
并且直接在实现的方法中调用被代理类相应的方法；但是动态代理则不同，我们不知道要针对哪个接口、
哪个被代理类创建代理类，因为它是在运行时被创建的。也就是：JDK静态代理是通过直接编码创建的，
而JDK动态代理是利用反射机制在运行时创建代理类的。

## JDK 动态代理机制
在 Java 动态代理机制中 InvocationHandler 接口和 Proxy 类是核心。

### Proxy 类
Proxy 类中使用频率最高的方法是：newProxyInstance() ，这个方法主要用来生成一个代理对象，三个参数：
* loader：类加载器，用于加载代理对象
* interfaces：被代理类实现的一些接口
* h：实现了 InvocationHandler 接口的对象

### InvocationHandler 类
要实现动态代理的话，还必须需要实现InvocationHandler 来自定义处理逻辑。 
当我们的动态代理对象调用一个方法时，这个方法的调用就会被转发到实现InvocationHandler 接口类的 invoke 方法来调用。
三个参数：
* proxy：动态生成的代理类
* method：与代理类对象调用的方法相对应
* args：当前 method 方法的参数

也就是说：你通过Proxy 类的 newProxyInstance() 创建的代理对象在调用方法的时候，
实际会调用到实现InvocationHandler 接口的类的 invoke()方法。 你可以在 invoke() 
方法中自定义处理逻辑，比如在方法执行前后做什么事情。

## JDK 动态代理类使用步骤
1. 定义一个接口及其实现类；
2. 自定义 InvocationHandler 并重写invoke方法，在 invoke 方法中我们会调用原生方法（被代理类的方法）并自定义一些处理逻辑；
3. 通过 Proxy.newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h) 方法创建代理对象；

## 代码示例
### 定义发送短信的接口
```java
public interface SmsService {
    String send(String message);
}
```
### 实现发送短信的接口
```java
public class SmsServiceImpl implements SmsService {
    public String send(String message) {
        System.out.println("send message:" + message);
        return message;
    }
}
```
### 定义一个 JDK 动态代理类
```java
public class DebugInvocationHandler<T> implements InvocationHandler {

  /**
   * 代理类中的真实对象
   */
  private final T target;

  public DebugInvocationHandler(T target) {
    this.target = target;
  }

  /**
   * @param proxy  动态生成的代理类
   * @param method 与代理类对象调用的方法相对应
   * @param args   当前 method 方法的参数
   * @return 原方法返回值
   * @throws Throwable
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    //调用方法之前，可以添加自己的操作
    System.out.println("before method " + method.getName());
    Object result = method.invoke(target, args);
    //调用方法之后，同样可以添加自己的操作
    System.out.println("after method " + method.getName());
    return result;
  }
}
```

### 获取代理对象的工厂类
```java
public class JdkProxyFactory {
    public static Object getProxy(Object target) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(), // 目标类的类加载
                target.getClass().getInterfaces(),  // 代理需要实现的接口，可指定多个
                new DebugInvocationHandler(target)   // 代理对象对应的自定义 InvocationHandler
        );
    }
}
```

### 实际使用 
```java
SmsService smsService = (SmsService) JdkProxyFactory.getProxy(new SmsServiceImpl());
smsService.send("java");
```


## 参考资料
* [Java JDK 代理]({% post_url java/proxy/2021-09-26-01-java-jdk-proxy %})
* [代理模式详解](https://github.com/Snailclimb/JavaGuide/blob/master/docs/java/basis/代理模式详解.md)
