---
layout:     post 
title:      spring 循环依赖解决方案 
date:       2021-09-05 20:01:49 +0800 
postId:     2021-09-05-20-01-49
categories: [framework]
keywords:   [Spring]
---

简简单单一个循环依赖问题，其实蕴含的是Spring 最核心的两个点： Bean的生命周期 与 AOP原理。

## Bean创建的几个关键点

此处只是为了讲清楚循环依赖，列出Bean的几个重要的阶段，Spring 创建Bean的过程，大致和对象的 初始化有点类似吧。有几个关键的步骤：

1. createBeanInstance ：实例化，此处要强调的是，Bean的早期引用在此出现了。
2. populateBean ： 填充属性，此处我们熟悉的@Autowired属性注入就发生在此处
3. initializeBean : 调用一些初始化方法，例如init ,afterPropertiesSet

此外：BeanPostProcessor作为一个扩展接口，会穿插在Bean的创建流程中，留下很多钩子，让我们可以 去影响Bean的创建过程。其中最主要的就属AOP代理的创建了。

## AOP的原理

AOP是以一个 InstantiationAwareBeanPostProcessor 类型的 BeanPostProcessor,参与到Bean 的创建逻辑中，并根据是否需要代理当前Bean，决定是否创建代理对象。主要逻辑在(
BeanPostProcessor)
AbstractAutoProxyCreator类中，有三个重要方法：

```java
//早期bean创建代理用
public Object getEarlyBeanReference(Object bean,String beanName)throws BeansException{
  Object cacheKey=getCacheKey(bean.getClass(),beanName);
  this.earlyProxyReferences.put(cacheKey,bean);
  return wrapIfNecessary(bean,beanName,cacheKey);
  }
//bean创建代理用
public Object postProcessAfterInitialization(Object bean,String beanName)throws BeansException{
  if(bean!=null){
  Object cacheKey=getCacheKey(bean.getClass(),beanName);
  if(this.earlyProxyReferences.remove(cacheKey)!=bean){
  return wrapIfNecessary(bean,beanName,cacheKey);
  }
  }
  return bean;
  }
protected Object wrapIfNecessary(Object bean,String beanName,Object cacheKey){
  //创建代理的逻辑
  }
```

> getBean()返回的是什么?

当我们尝试按name从BeanFactory.getBean(beanname)一个Bean时，返回的一定是A类对应的实例吗？ 答案是否， 当A需要需要创建代理对象时，我们getBean 得到是 代理对象的引用。

## 解析循环依赖

```java
public class A {
  @Autowired
  B b;

  public A() {
  }
}

class B {
  @Autowired
  A a;

  public B() {
  }
}
```

### 没有缓存

直接创建A，发现需要引入B，此时B还没有初始化，找不到直接报错

### 一级缓存

直接创建A，发现需要引入B，此时去一级缓存中查找，找不到，创建B，发现需要A，死循环

### 二级缓存

先创建A，初始化属性之前就放入一级缓存，再初始化属性发现需要引入B，去一级缓存中查找，找不到再去 二级缓存中查找，此时还找不到，那么先创建B的实例，放入二级缓存，再初始化B的相关属性，发现需要A，
那么在二级级缓存中可以找到A，则B初始化完毕，此时返回给A，则A也可以初始化完毕。

### 三级缓存

此时发现二级缓存已经完美解决了循环依赖的问题，那么三级缓存是否有必要呢？这是因为二级缓存没有 考虑AOP的场景，如果有AOP，俺么就需要三级缓存来实现。详见如下分析。

## 源码分析

三级缓存，就是把创建好的Bean缓存起来：

```java
/** Cache of singleton objects: bean name --> bean instance */
private final Map<String, Object> singletonObjects=new ConcurrentHashMap<String, Object>(256);

/** Cache of early singleton objects: bean name --> bean instance */
private final Map<String, Object> earlySingletonObjects=new HashMap<String, Object>(16);

/** Cache of singleton factories: bean name --> ObjectFactory */
private final Map<String, ObjectFactory<?>>singletonFactories=new HashMap<String, ObjectFactory<?>>(16);
```

* singletonObjects：第一级缓存，里面存放的都是创建好的成品Bean。
* earlySingletonObjects : 第二级缓存，里面存放的都是半成品的Bean。
* singletonFactories ：第三级缓存， 不同于前两个存的是 Bean对象引用，此缓存存的bean 工厂对象，也就存的是 专门创建Bean的一个工厂对象。此缓存用于解决循环依赖


```java
public abstract class AbstractAutowireCapableBeanFactory
  extends AbstractBeanFactory
  implements AutowireCapableBeanFactory {
  protected Object doCreateBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) throws BeanCreationException {
    BeanWrapper instanceWrapper = null;
    if (mbd.isSingleton()) {
      instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
    }
    if (instanceWrapper == null) {
      instanceWrapper = createBeanInstance(beanName, mbd, args);
    }
    // 没有代理时候此bean就是需要暴露的对象
    Object bean = instanceWrapper.getWrappedInstance();
    Class<?> beanType = instanceWrapper.getWrappedClass();
    // 判断是否允许暴露早期引用
    boolean earlySingletonExposure = (mbd.isSingleton() &&
      this.allowCircularReferences &&
      isSingletonCurrentlyInCreation(beanName));
    
    if (earlySingletonExposure) {
      // 覆写getObject方法：getEarlyBeanReference(beanName, mbd, bean)
      addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
    }
  }

  protected Object getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean) {
    // 对于普通类，需要暴露的类就是之前创建的实例
    Object exposedObject = bean;
    if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
      for (SmartInstantiationAwareBeanPostProcessor bp : getBeanPostProcessorCache().smartInstantiationAware) {
        // 对于需要代理的类，需要暴露的就是通过代理工厂创建的类
        exposedObject = bp.getEarlyBeanReference(exposedObject, beanName);
      }
    }
    return exposedObject;
  }

  // DefaultSingletonBeanRegistry.java
  protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
    Assert.notNull(singletonFactory, "Singleton factory must not be null");
    synchronized (this.singletonObjects) {
      if (!this.singletonObjects.containsKey(beanName)) {
        this.singletonFactories.put(beanName, singletonFactory);
        this.earlySingletonObjects.remove(beanName);
        this.registeredSingletons.add(beanName);
      }
    }
  }


  // 获取对象流程分析
  public Object getSingleton(String beanName) {
    return getSingleton(beanName, true);
  }

  protected Object getSingleton(String beanName, boolean allowEarlyReference) {
    // Quick check for existing instance without full singleton lock
    Object singletonObject = this.singletonObjects.get(beanName);
    if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
      singletonObject = this.earlySingletonObjects.get(beanName);
      if (singletonObject == null && allowEarlyReference) {
        synchronized (this.singletonObjects) {
          // Consistent creation of early reference within full singleton lock
          singletonObject = this.singletonObjects.get(beanName);
          if (singletonObject == null) {
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null) {
              ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
              if (singletonFactory != null) {
                singletonObject = singletonFactory.getObject();
                this.earlySingletonObjects.put(beanName, singletonObject);
                this.singletonFactories.remove(beanName);
              }
            }
          }
        }
      }
    }
    return singletonObject;
  }

}
```

## 参考资料
* [Spring 循环依赖解决方案]({% post_url framework/spring/2021-09-05-02-spring-circular-dependency %})
* [程序员黑哥 —— Spring循环依赖](https://zhuanlan.zhihu.com/p/382066829)
* [Spring循环依赖三级缓存是否可以去掉第三级缓存](https://segmentfault.com/a/1190000023647227)
