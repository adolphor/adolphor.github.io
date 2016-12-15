---
layout:     post
title:      代理模式
date:       2016-12-15 22:26:22 +0800
postId:     2016-12-15-22-26-22
categories: [java]
tags:       [java]
geneMenu:   true
excerpt:    设计模式之 —— 代理模式
---

## 代理模式
代理模式是对象的结构模式。代理模式给某一个对象提供一个代理对象，并由代理对象控制对原对象的引用。【注-1】

## 使用场景
第一个使用场景就是在不改动源代码的前提下，对原有功能进行增强。比如可以在原方法调用之前和调用之后
增加额外的业务逻辑，比如在插入数据到数据库之前进行日志记录。第二个场景就是如果有多个对象有相同类
型的逻辑，但是每个方法各自的具体实现不同，那么也可以使用动态代理模式进行处理。

## 源码分析

### 静态代理模式

有一个售票窗口（TicketSeller），具有售票功能（sell()方法）：

{% highlight java %}
public class TicketSeller{
  public void sell(int price) {
    System.out.println("TicketSeller 以价格 $" + price + " 卖了一张票 ...");
  }
}
{% endhighlight %}

如果票价是 $30，正常情况下是这样运行的：

{% highlight java %}
public class Test {
  public static void main(String[] args) {
    TicketSeller ticketSeller = new TicketSeller();
    ticketSeller.sell(30);
  }
}
{% endhighlight %}

运行结果：
{% highlight shell %}
TicketSeller 以价格 $30 卖了一张票 ...
{% endhighlight %}

有时候我们去不了车站售票口，可以去代理窗口买票，但是代理窗口要收取 $5 的手续费，实现如下：
{% highlight java %}
public class ProxySeller{
  private TicketSeller ticketSeller;
  public ProxySeller(TicketSeller ticketSeller) {
    this.ticketSeller = ticketSeller;
  }
  public void sell(int price) {
    // 代理扣除手续费（手续费设为 $5）
    beforeSell(price / 3);
    // 代理以扣除手续费之后的价格从原渠道买票
    ticketSeller.sell(price - price / 3);
    // 代理把票给客户
    afterSell();
  }
  private void beforeSell(int price) {
    System.out.println("ProxySeller 代理扣除手续费 $" + price);
  }
  private void afterSell() {
    System.out.println("ProxySeller 代理把票给客户，售票完成.");
  }
}
{% endhighlight %}

因为代理自己没有出票的能力，所以购票还是调用的原售票方法，但是调用之前进行了
扣除手续费的操作（beforeSell()方法），购票之后如果需要也可以进行别的操作（
afterSell()方法）。

此时就如果票价是 $30，那么就要加上 $5 的手续费，共 $35，运行如下：

{% highlight java %}
public class Test {
  public static void main(String[] args) {
    // 需要传入被代理对象的实例
    ProxySeller proxySeller = new ProxySeller(new TicketSeller());
    proxySeller.sell(35);
  }
}
{% endhighlight %}

运行结果：
{% highlight shell %}
ProxySeller 代理扣除手续费 $5
TicketSeller 以价格 $30 卖了一张票 ...
ProxySeller 代理把票给客户，售票完成.
{% endhighlight %}

这样就在不改变原代码的前提下增加（更改）了业务逻辑，那么如果这个代理门店不仅提供
代购车票服务，还提供代购盒饭服务呢，最原始的做法就是在实现一个外卖服务类和外卖服务代理类。
但是因为代购车票和代购别的东西都是一样的逻辑，那么久可以在现有代码的基础上通过改造，
实现代理类复用。如果不希望代理类中使用if……else……语句进行被代理类的类型判断，那么需要
改造当前TicketSeller类，提取出Seller类，sell()接口作为公共接口，表示被代理类的售卖能力：

{% highlight java %}
public interface Seller {
  void sell(int price);
}
{% endhighlight %}

接着改造原有TicketSeller类：

{% highlight java %}
public class TicketSeller implements Seller {
  public void sell(int price) {
    System.out.println("TicketSeller 以价格 $" + price + " 卖了一张票 ...");
  }
}
{% endhighlight %}

参考TicketSeller类，实现外卖服务类：
{% highlight java %}
public class FoodSeller implements Seller {
  public void sell(int price) {
    System.out.println("FoodSeller 以价格 $" + price + " 卖了一盒饭 ...");
  }
}
{% endhighlight %}

再改造代理类，使代理类具有代购外卖的服务：

{% highlight java %}
public class ProxySeller implements Seller {
  private Seller seller;
  public ProxySeller(Seller seller) {
    this.seller = seller;
  }
  public void sell(int price) {
    before();
    // 代理以扣除手续费之后的价格从原渠道购物
    seller.sell(price - 5);
    after();
  }
  private void before() {
    System.out.println("ProxySeller 代理收取手续费 $5");
  }
  private void after() {
    System.out.println("ProxySeller 代理完成");
  }
}
{% endhighlight %}

测试代理的买票和外卖服务：

{% highlight java %}
public class Test {
  public static void main(String[] args) {
    // 买票服务
    ProxySeller tickeProxy = new ProxySeller(new TicketSeller());
    tickeProxy.sell(35);
    // 外卖服务
    ProxySeller foodProxy = new ProxySeller(new FoodSeller());
    foodProxy.sell(20);
  }
}
{% endhighlight %}

运行结果：
{% highlight shell %}
ProxySeller 代理扣除手续费 $5
TicketSeller 以价格 $30 卖了一张票 ...
ProxySeller 代理把物品给客户，代购完成.

ProxySeller 代理扣除手续费 $5
FoodSeller 以价格 $15 卖了一盒饭 ...
ProxySeller 代理把物品给客户，代购完成.
{% endhighlight %}

### 动态代理
上述静态代理类中，如果增加了一个新的方法，比如退货接口，那么接口类、所有实现类以及代理类都要根据新接口进行修改：

{% highlight java %}
// Seller接口增加方法：
void back(int price);
// TicketSeller类增加实现：
public void back(int price) {
  System.out.println("TicketSeller 退回了价值 $" + price + " 的车票 ...");
}
// FoodSeller类增加实现：
public void back(int price) {
  System.out.println("FoodSeller 退回了价值 $" + price + " 的盒饭 ...");
}
// ProxySeller代理类增加实现：
public void back(int price) {
  before();
  seller.back(price - 5);
  after();
}
{% endhighlight %}

这样的话就可以从代理类调用退货方法：
{% highlight java %}
public class Test {
  public static void main(String[] args) {
    // 车票服务
    ProxySeller tickeProxy = new ProxySeller(new TicketSeller());
    tickeProxy.sell(35); // 买票
    tickeProxy.back(35); // 退票
    System.out.println();
    // 外卖服务
    ProxySeller foodProxy = new ProxySeller(new FoodSeller());
    foodProxy.sell(20); // 买外卖
    foodProxy.back(20); // 退外卖
  }
}
{% endhighlight %}

运行结果：
{% highlight shell %}
ProxySeller 代理收取手续费 $5
TicketSeller 以价格 $30 卖了一张票 ...
ProxySeller 代理完成
ProxySeller 代理收取手续费 $5
TicketSeller 退回了价值 $30 的车票 ...
ProxySeller 代理完成

ProxySeller 代理收取手续费 $5
FoodSeller 以价格 $15 卖了一盒饭 ...
ProxySeller 代理完成
ProxySeller 代理收取手续费 $5
FoodSeller 退回了价值 $15 的盒饭 ...
ProxySeller 代理完成
{% endhighlight %}

使用动态方法的话就可以在不修改代理类的前提下进行方法的扩充，而动态代理有JDK动态代理，
CGLIB字节码增强、JAVAASISST等方式。还有另外一个好处，就是各个实现类的可以继承自不同的接口，
所以方法命名也可以不同了。

#### JDK动态代理方式
JDK动态代理的前提条件是实现类必须实现了某个接口(FoodSeller和TicketSeller必须继承某个接口)，代码如下：

{% highlight java %}
public class ProxySeller implements InvocationHandler {
  private Object target;
  public Object getInstance(Object target) {
    this.target = target;
    //Get the proxy object
    return Proxy.newProxyInstance(
        target.getClass().getClassLoader(),
        target.getClass().getInterfaces(),
        this
    );//To bind interface (this is a defect, cglib made up for this shortcoming)
  }
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    before();
    int price = (int) args[0];
    Object result = method.invoke(target, price - 5);
    after();
    return result;
  }
  private void before() {
    System.out.println("ProxySeller 代理收取手续费 $5");
  }
  private void after() {
    System.out.println("ProxySeller 代理完成");
  }
}
{% endhighlight %}


#### CGLIB字节码增强方式

CGLIB没有必须实现接口的限制(FoodSeller和TicketSeller可以不继承接口)，代码如下：

{% highlight java %}
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
public class ProxySeller implements MethodInterceptor {
  private Object target;
  public Object getInstance(Object target) {
    this.target = target;
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(this.target.getClass());
    // The callback method
    enhancer.setCallback(this);
    // Create a proxy object
    Object o = enhancer.create();
    return o;
  }
  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    before();
    int price = (int) args[0];
    Object result = proxy.invokeSuper(obj, new Object[]{price - 5});
    after();
    return result;
  }
  private void before() {
    System.out.println("ProxySeller 代理收取手续费 $5");
  }
  private void after() {
    System.out.println("ProxySeller 代理完成");
  }
}
{% endhighlight %}

## 代理效率
关于各个代理模式的效率如何，可以参考文章《[Java代理性能比较]({% 2016-12-14-java-proxy-performance %})》。

## 参考资料

* 【注-1】《Java与模式——阎宏》
