---
layout:     post
title:      spring bean 的生命周期
date:       2021-09-06 19:51:54 +0800
postId:     2021-09-06-19-51-55
categories: [framework]
keywords:   [Spring]
---
在传统的Java应用中，bean的生命周期很简单。使用Java关键字new进行bean实例化，然后该bean就可以使用了。
一旦该bean不再被使用，则由Java自动进行垃圾回收。

相比之下，Spring容器中的bean的生命周期就显得相对复杂多了。正确理解Spring bean的生命周期非常重要。
因为你或许要利用Spring提供的扩展点来自定义bean的创建过程。

## 生命周期的流程

![Spring中bean的生命周期]({{ site.baseurl }}/image/post/2021/09/06/01/Spring中bean的生命周期.png)

### 简单版本
1. 构建 (构造函数：或者说是 construct)
2. 初始化（init）
3. 销毁 （destory）

### 复杂版本
1. 构建
2. 设置属性
3. 后置处理器 的前置处理方法: BeanPostProcessor.postProcessBeforeInitialization
4. 初始化（init）
5. 后置处理器 的后置处理方法: BeanPostProcessor.postProcessAfterInitialization
6. 销毁

### 完整版本

bean在Spring容器中从创建到销毁经历了若干阶段，每一阶段都可以针对Spring如何管理Bean进行个性化定制。
在bean准备就绪之前，bean工厂执行了以下启动步骤：

1. Spring对bean进行实例化；
2. Spring将值和bean的引用注入到bean对应的属性中；
3. 如果bean实现了BeanNameAware接口，Spring将bean的ID传递给setBeanName()方法；
4. 如果bean实现了BeanFactoryAware接口，Spring将调用setBeanFactory()方法，将BeanFactory容器实列传入；
5. 如果bean实现了ApplicationContextAware接口，Spring将调用setApplicationContext()方法，将bean所在的应用上下文的引用传入进来；
6. 如果bean实现了BeanPostProcessor接口，Spring将调用它们的postProcessBeforeInitialization()方法；
7. 如果bean实现了InitializingBean接口，Spring将调用它们的afterPropertiesSet()方法。类似地，如果bean使用init-method声明了初始化方法，该方法也会被调用；
8. 如果bean实现了BeanPostProcessor接口，Spring将调用它们的postProcessAfterInitialization()方法；
9. 此时，bean已经准备就绪，可以被应用程序使用了，它们将一直驻留在应用上下文中，直到该应用上下文被销毁；
10. 如果bean实现了DisposableBean接口，Spring将调用它的destroy()接口方法。同样，如果bean使用destroy-method声明了销毁方法，该方法也会被调用。

## 源码探析
springboot的启动类中，都会执行 `SpringApplication.run` 方法。在创建上下文环境之后，最核心的方法
就是`refreshContext`，对上下文环境进行初始化操作，本段就着重说明这一过程。

> spring-beans-5.3.8.jar

```java
public abstract class AbstractApplicationContext extends DefaultResourceLoader 
  implements ConfigurableApplicationContext {
  synchronized (this.startupShutdownMonitor) {
    StartupStep contextRefresh = this.applicationStartup.start("spring.context.refresh");

    /**
     * 准备环境
     * 1. 记录容器的启动时间startupDate，标记容器为激活
     * 2. 初始化上下文环境如文件路径信息
     * 3. 验证必填属性是否填写
     */
    prepareRefresh();

    /** 告诉子类去刷新beanFactory */
    ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

    /** 准备上下文要使用的 bean factory */
    prepareBeanFactory(beanFactory);

    try {
      /** 允许子类上下文中对 beanFactory 做后期处理 */
      postProcessBeanFactory(beanFactory);

      StartupStep beanPostProcess = this.applicationStartup.start("spring.context.beans.post-process");

      /**
       * 执行 context 中注册的 BeanFactoryPostProcessor 中的 postProcessBeanFactory() 方法
       * 1. BeanFactoryPostProcessor 是 bean 属性处理容器。即管理 bean 工厂中的 BeanDefinition
       * 2. BeanDefinition在 spring mvc中就是xml文件中对应的bean标签(注意，是标签，而不是真实的 java bean)
       */
      invokeBeanFactoryPostProcessors(beanFactory);

      /**
       * 注册 BeanPostProcessor 的实现类
       * 1. BeanPostProcessor接口包含两个方法：postProcessBeforeInitialization 和 postProcessAfterInitialization
       * 2. 这里只是注册，两个方法实际分别在 Bean 初始化之前和初始化之后得到执行
       */
      registerBeanPostProcessors(beanFactory);
      beanPostProcess.end();

      /** 初始化ApplicationContext的MessageSource,MessageSource是国际化相关的接口 */
      initMessageSource();

      /** 初始化ApplicationContext事件广播器 */
      initApplicationEventMulticaster();

      /** 初始化子类特殊bean（钩子方法） */
      onRefresh();

      /** 注册事件监听器 */
      registerListeners();

      /** 初始化所有的单例java bean（不包含懒加载的类） */
      finishBeanFactoryInitialization(beanFactory);

      /** 广播事件，ApplicationContext初始化完成 */
      finishRefresh();
    } catch (BeansException ex) {
      // Destroy already created singletons to avoid dangling resources.
      destroyBeans();
      // Reset 'active' flag.
      cancelRefresh(ex);
      // Propagate exception to caller.
      throw ex;
    }
    finally {
      // Reset common introspection caches in Spring's core, since we
      // might not ever need metadata for singleton beans anymore...
      resetCommonCaches();
      contextRefresh.end();
    }
  }
}
```

###  BeanDefinition
BeanDefinition 是 spring bean 的建模对象，即把一个bean实例化出来的模型对象。有人会问把一个
bean 实例化出来有Class就行了啊，Class也就是我们通常说的类对象，就是一个普通对象的建模对象，
那么为什么spring不能用Class来建立bean呢?很简单，因为Class无法完成bean的抽象，比如bean的作用域，
bean的注入模型，bean是否是懒加载等等信息，Class是无法抽象出来的，故而需要一个BeanDefinition类
来抽象这些信息，以便于spring能够完美的实例化一个bean。

spring扫描到某个bean的类时，除了要存储类名、构造器等Class的基础信息以外，还要存储scope，lazy，
dependsOn等spring的属性。BeanDefintion是一个类，专门用来存储上述bean中的各种属性。因此当
spring扫描到一个符合规则的类时，首先是实例化一个BeanDefinition的对象，并且把根据类的类名生成
一个bean的名字，继而以beanName为key，BeanDefinition对象为value，存放在一个map中。

值得注意的是，这里都是讲在扫描bean后，BeanDefinition的实例化，以及放入BeanDefinition的map中。
还没有讲到Bean的实例化和装载。

BeanDefinition 的属性方法如下：

* String: `getBeanClassName`: 返回当前bean definition定义的类名
* ConstructorArgumentValues: `getConstructorArgumentValues`:返回bean的构造函数参数
* String[]: `getDependsOn`:返回当前bean所依赖的其他bean的名称
* String: `getFactoryBeanName`: 返回factory bean的名称
* String: `getFactoryMethodName`: 返回工厂方法的名称
* BeanDefinition: `getOriginatingBeanDefinition`: 返回原始的BeanDefinition,如果不存在返回null
* String: `getParentName`: 返回当前bean definition的父definition的名字
* MutablePropertyValues: `getPropertyValues`: 返回一个用于新的bean实例上的属性值
* String: `getScope`: 返回当前bean的目标范围
* boolean: `isAbstract`: 当前bean是否是abstract,意味着不能被实例化
* boolean: `isLazyInit`: bean是否是延迟初始化
* boolean: `isPrimary`: bean是否为自动装配的主要候选bean
* boolean: `isPrototype`: bean是否是多实例
* boolean: `isSingleton`: bean是否是单例
* void: `setAutowiredCandidate(boolean)`: 设置bean是否对其他bean是自动装配的候选bean
* void: `setBeanClassName(String)`: 指定bean definition的类名
* void: `setDependsOn(String ...)`: 设置当前bean初始化所依赖的beans的名称
* void: `setFactoryBeanName(String)`: 如果factory bean的名称
* void: `setFactoryMethodName(String)`: 设置工厂的方法名
* void: `setLazyInit(boolean lazyInit)`: 设置是否延迟初始化
* void: `setParentName(String)`: 设置父definition的名称
* void: `setPrimary(boolean)`: 设置是否主要的候选bean
* void: `setScope(String)`: 设置bean的范围,如:单例,多实例

### BeanFactoryPostProcessor
BeanFactoryPostProcessor 是实现spring容器功能扩展的重要接口，例如修改BeanDefinition，
实现bean动态代理等。很多框架都是通过此接口实现对spring容器的扩展，例如mybatis与spring集成时，
只定义了mapper接口，无实现类，接口也没有添加@Component等注解，但spring却可以完成自动注入，
也是基于BeanFactoryPostProcessor接口来实现的。

前文说到 BeanDefinition 在实例化之后，被放入 BeanDefinition map 中，而 BeanFactoryPostProcessor
就是在 `BeanDefinition map 初始化完成后，Bean 实例构造方法执行之前` 执行的。

BeanFactoryPostProcessor的主要作用是，开发者可以修改容器中所有BeanDefinition的信息，
接口方法的入参是ConfigurrableListableBeanFactory，使用该参数，可以获取到相关bean的定义信息。
但绝对不允许进行bean实例化相关的操作！因为中spring加载机制中，BeanFactoryPostProcessor是在
Bean 构造方法之前执行的，如果这个时候提前实例化bean，很可能会一连串的问题。

```java
@FunctionalInterface
public interface BeanFactoryPostProcessor {
  void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
```

### BeanPostProcessor

如果说 BeanFactoryPostProcessor 是对 BeanDefinition 的扩展处理，那么 BeanPostProcessor
更多的是对Bean的扩展处理。BeanPostProcessor 的触发时间是在 Bean 的实例化之后（执行了构造方法，
并且对属性进行了赋值），在 Bean 的 init 方法执行之前（@PostConstruct注解方法、InitializeBean
接口方法、@Bean中initMethod方法）。

```java
public interface BeanPostProcessor {
  @Nullable
  default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }
  @Nullable
  default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }
}
```

BeanPostProcessor接口中定义两个方法，根据名字可以推测，分别在Bean的init方法之前和之后执行。
这点很好的体现了spring的AOP思想，可以理解为BeanPostProcessor是对Bean的init方法执行前后，做了一层切面拦截。

有一个实现Spring aop 的BeanPostProcessor 叫 AnnotationAwareAspectJAutoProxyCreator。
在postProcessBeforeInitialization或者postProcessAfterInitialization方法中，对对象进行判断，
看他需不需要织入切面逻辑，如果需要，那我就根据这个对象，生成一个代理对象，然后返回这个代理对象，
那么最终注入容器的，自然就是代理对象了。

### finishBeanFactoryInitialization

该方法会实例化所有剩余的非懒加载单例 bean。值得注意的是“所有剩余的”，就是有一些特殊的bean，这该方法之前就已经加载了，如：

* 一些spring容器内部的bean；
* 实现了 BeanFactoryPostProcessor 接口的bean;
* 实现了 BeanPostProcessor 接口的 bean（但是这些bean会在这个方法中触发）。

其他的非懒加载单例 bean 都会在这个方法中被实例化，并且 BeanPostProcessor 的触发也是在这个方法中。

### Bean的生命履历
基于spring的refresh过程，我们把关于bean的部分拎出来，就形成了下面的bean的加载过程：
1. spring基于@ComponentScan等注解扫描到bean，并将每个bean实例化成包含bean各种属性的`BeanDefinition`的对象，放入beanDefinitionMap中。
2. spring 中的 bean 简单分为：特殊bean和普通bean，beanFactory实际会先实例化特殊bean。
3. 实现`BeanFactoryPostProcessor`接口的就是一种特殊bean，在beanDefinitionMap加载后触发，可以自定义实现类，对其中的BeanDefinition进行修改。
4. `construct过程`：BeanFactory 执行getBean方法生产其他的普通bean（调用类的构造方法，或FactoryBean的getObject方法，以及@Bean注解的方法）。
5. 此时bean获取会受`三级缓存`（singletonFactories、earlySingletonObjects、singletonObjects）影响，如earlySingletonObjects会提前曝光尚未populate属性数据的单例对象，可解决循环依赖问题。
6. `populate过程`：设置bean的依赖关系（基于属性注入）以及属性值。
7. 对于实现`BeanPostProcessor`接口的类，执行接口中的postProcessBeforeInitialization 方法。
8. `initialze过程`：执行bean的各种初始化方法，initialze方法的优先级如下：
   1. @PostConstruct指定的方法
   2. InitializingBean接口的afterPropertiesSet()方法 
   3. @Bean中initMethod指定的方法。
9. 对于实现`BeanPostProcessor`接口的类，执行接口中的postProcessAfterInitialization 方法。
10. destroy过程，容器销毁，执行bean的各种销毁方法，destroy方法的优先级如下：
    1. @PostDestroy指定的方法
    2. DisposableBean接口的destroy()方法
    3. @Bean中destroyMethod指定的方法。


开始写作吧
```

```

## 参考资料
* [spring bean 的生命周期]({% post_url framework/spring/2021-09-06-01-spring-bean-life-cycle %})
* [KerryWu —— Spring-Bean的生命周期](https://segmentfault.com/a/1190000023712428)
* [天真真不知路漫漫 —— Spring bean 的生命周期](https://segmentfault.com/a/1190000021554606)
