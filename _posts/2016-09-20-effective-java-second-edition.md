---
layout:     post
title:      《Effective Java —— Joshua Bloch》读书笔记
date:       2016-09-20 10:47:57 +0800
postId:     2016-09-20-10-47-57
categories: [blog]
tags:       [Java, 读书笔记]
geneMenu:   true
excerpt:    《Effective Java —— Joshua Bloch》读书笔记
---

## 第1章 引言
虽然本书中的规则不会百分百的适用于任何时刻和任何场合，但他们体现了绝大多数啊
情况下的最佳程序设计实践。你不应该盲目遵从这些规则，但也应该只在偶尔情况下，
有了充分的理由之后才去打破这些规则。

## 第2章 创建和销毁对象

* 何时以及如何创建对象
* 何时以及如何避免创建对象
* 如何确保他们能够适时的销毁
* 如何管理对象销毁之前必须进行的各项清理动作

### 第1条：考虑用静态工厂方法代替构造器
静态工厂方法与构造器相比有三大优势：

* 它们有名称
    - 这样在相同参数的情况下也能构造不同的实例出来
* 不必在每次调用它们的时候都创建一个新对象
    - 这个应该是使用静态工厂方法的最主要的原因和情景
* 它们可以返回原返回类型的任何子类型的对象，可以要求客户端使用通过接口来引用被返回的对象
    - （TODO：暂时不及理解什么意思）
    - 参考 [第四条](#tip4) 和 [第五十二条](#tip52)
    - 静态工厂返回的对象所属的类，在编写包含该静态工厂方法的类时可以不必存在，这种灵活的静态工厂方法
    构成了服务提供者框架（Service Provider Framework）的基础。

静态工厂方法的缺点：

* 类如果不含有公有的或者受保护的构造器，就不能被子类化    
* 他们与其他的静态方法，实际上没有任何区别
    - 这样的话就不像构造器那样容易被找到，一般使用如下的命名方式来区分别的静态方法
        - valueOf
        - of
        - getInstance
        - newInstance
        - getType
        - newType


### 第2条：遇到多个构造器参数时要考虑用构建器
如果有多个参数，而且有些参数必填，有些参数可选，在编写构造器的时候有如下几种方式：

* JavaBeans模式
    - 使用默认的无参构造器，其余的参数通过set方法设置
* 使用重叠构造器模式
    - 多个构造器每个构造器多一个参数
* Builder构建器模式
    - 创建一个内部类Builder，作为构造器的参数

构建器模式返利代码：
{% highlight java %}
package Y2016.M09.D20_effectiv_java.tip02;
public class NutritionFacts {
  private final int servingSize;
  private final int servings;
  private final int calories;
  private final int fat;
  public static class Builder {
    // 必填属性
    private final int servingSize;
    private final int servings;
    // 可选属性
    private int calories = 0;
    private int fat = 0;
    public Builder(int servingSize, int servings) { // 必填属性构造器
      this.servingSize = servingSize;
      this.servings = servings;
    }
    public Builder calories(int val) { // 可选属性set方法
      calories = val;
      return this;
    }
    public Builder fat(int val) {
      this.fat = val;
      return this;
    }
    public NutritionFacts build() { // 实例化目标对象
      return new NutritionFacts(this);
    }
  }
  public NutritionFacts(Builder builder) { // 供内部类构造方法调用
    servingSize = builder.servingSize;
    servings = builder.servings;
    calories = builder.calories;
    fat = builder.fat;
  }
}
{% endhighlight %}

### 第3条：用私有构造器或者枚举类型强化Singleton属性

### 第4条：通过私有构造器强化不可实例化的能力 {#tip4}

### 第5条：避免创建不必要的对象

### 第6条：消除过期的对象引用

### 第7条：避免使用终结方法

## 第3章 对于所有对象都通用的方法

### 第8条：覆盖equals时请遵守通用约定

### 第9条：覆盖equals时总要覆盖hashCode

### 第10条：始终要覆盖toString

### 第11条：谨慎地覆盖clone

### 第12条：考虑实现Comparable接口

## 第4章 类和接口

### 第13条：使类和成员的可访问性最小化

### 第14条：在公有类中使用访问方法而非公有域

### 第15条：使可变性最小化

### 第16条：复合优先于继承

### 第17条：要么为继承而设计，并提供文档说明，要么就禁止继承

### 第18条：接口优于抽象类

### 第19条：接口只用于定义类型

### 第20条：类层次优于标签类

### 第21条：用函数对象表示策略

### 第22条：优先考虑静态成员类

## 第5章 泛型

### 第23条：请不要在新代码中使用原生态类型

### 第24条：消除非受检警告

### 第25条：列表优先于数组

### 第26条：优先考虑泛型

### 第27条：优先考虑泛型方法

### 第28条：利用有限制通配符来提升API的灵活性

### 第29条：优先考虑类型安全的异构容器

## 第6章 枚举和注解

### 第30条：用enum代替int常量

### 第31条：用实例域代替序数

### 第32条：用EnumSet代替位域

### 第33条：用EnumMap代替序数索引

### 第34条：用接口模拟可伸缩的枚举

### 第35条：注解优先于命名模式

### 第36条：坚持使用Override注解

### 第36条：坚持使用Override注解

## 第7章 方法

### 第38条：检查参数的有效性

### 第39条：必要时进行保护性拷贝

### 第40条：谨慎设计方法签名

### 第41条：慎用重载

### 第42条：慎用可变参数

### 第43条：返回零长度的数组或者集合，而不是null

### 第44条：为所有导出的API元素编写文档注释

## 第8章 通用程序设计

### 第45条：将局部变量的作用域最小化

### 第46条：for-each循环优先于传统的for循环

### 第47条：了解和使用类库

### 第48条：如果需要精确的答案，请避免使用float和double

### 第49条：基本类型优先于装箱基本类型

### 第50条：如果其他类型更适合，则尽量避免使用字符串

### 第51条：当心字符串连接的性能

### 第52条：通过接口引用对象 {#tip52}

### 第53条：接口优先于反射机制

### 第54条：谨慎地使用本地方法

### 第55条：谨慎地进行优化

### 第56条：遵守普遍接受的命名惯例

## 第9章 异常

### 第57条：只针对异常的情况才使用异常

### 第58条：对可恢复的情况使用受检异常，对编程错误使用运行时异常

### 第59条：避免不必要地使用受检的异常

### 第60条：优先使用标准的异常

### 第61条：抛出与抽象相对应的异常

### 第62条：每个方法抛出的异常都要有文档

### 第63条：在细节消息中包含能捕获失败的信息

### 第64条：努力使失败保持原子性

### 第65条：不要忽略异常

## 第10章 并发

### 第66条：同步访问共享的可变数据

### 第66条：同步访问共享的可变数据

### 第68条：executor和task优先于线程

### 第69条：并发工具优先于wait和notify

### 第70条：线程安全性的文档化

### 第71条：慎用延迟初始化

### 第72条：不要依赖于线程调度器

### 第73条：避免使用线程组

## 第11章 序列化

### 第74条：谨慎地实现Serializable接口

### 第75条：考虑使用自定义的序列化形式

### 第76条：保护性地编写readObject方法

### 第77条：对于实例控制，枚举类型优先于readResolve

### 第78条：考虑用序列化代理代替序列化实例

## 参考资料

* [test](test.html)

{% highlight java %}
{% endhighlight %}
