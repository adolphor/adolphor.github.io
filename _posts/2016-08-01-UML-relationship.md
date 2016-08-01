---
layout: post
title:  "UML中的类图关系"
date:   2016-08-01 16:08:00 +0800
postId: 2016-08-01-16-08-00
categories: [UML]
tags: [UML]
---
## 概述

UML图中类之间的关系有如下几种：依赖，关联，泛化，实现，聚合，组合。

各种关系的强弱顺序：泛化 = 实现 > 组合 > 聚合 > 关联 > 依赖 

> 但是，有几点需要注意：

* 将A类作为B类的属性，可能是组合，也可能是聚合，或者是关联关系。根据业务场景来定义。
* 依赖关系一般体现在某个类的方法使用另一个类的对象作为参数
* 泛化关系就是继承关系，关键字：extends
* 实现关系就是实现接口，关键字：implements

![image]({{ '/image/post/2016/08/20160801-UML00.png' }})


## 泛化（Generalization `/ˌdʒɛnərəlɪˈzeʃən/`）
泛化关系（Generalization）：A是B和C的父类，B,C具有公共类（父类）A，说明A是B,C的一般化（概括，也称泛化）

* 泛化关系(Generalization)也就是继承关系，也称为“is-a-kind-of”关系，泛化关系用于描述父类与子类之间的关系，父类又称作基类或超类，
子类又称作派生类。
* 在代码实现时，使用面向对象的继承机制来实现泛化关系，如在Java语言中使用extends关键字、在C++/C#中使用冒号“：”来实现。 
* 在UML中，泛化关系用带空心三角形的直线来表示。

{% highlight java %}
public class Person {
  private String name;
  private String age;
  public void eat(){
  }
}
public class Student extends Person {
  private Integer score;
  public void study() {
  }
}
public class Teacher extends Person{
  private Integer salary;
  public void teach() {
  }
}
{% endhighlight %}

![image]({{ '/image/post/2016/08/20160801-UML02.jpg' }})


## 实现（Realization `/ˌriələ'zeʃən/`）
实现关系（Implementation）：是用来规定接口和实线接口的类或者构建结构的关系，接口是操作的集合，而这些操作就用于规定类或者构建的一种服务。

* 接口之间也可以有与类之间关系类似的继承关系和依赖关系，但是接口和类之间还存在一种实现关系(Realization)，在这种关系中，类实现了接口，
类中的操作实现了接口中所声明的操作。
* 在UML中，类与接口之间的实现关系用带空心三角形的虚线来表示。

{% highlight java %}
public interface Animal {
  void eat();
  void sleep();
}
public class Person implements Animal {
  public void eat() {
    System.out.println("吃饭");
  }
  public void sleep() {
    System.out.println("睡床上");
  }
}
public class Panda implements Animal {
  public void eat() {
    System.out.println("吃竹子");
  }
  public void sleep() {
    System.out.println("睡树上");
  }
}
{% endhighlight %}

![image]({{ '/image/post/2016/08/20160801-UML03.jpg' }})

## 组合关系(Composition `/ˌkɑmpə'zɪʃən/`)

组合关系（Composition）:也是整体与部分的关系，但是整体与部分不可以分开.

* 组合关系(Composition)也表示类之间整体和部分的关系，但是组合关系中部分和整体具有统一的生存期。一旦整体对象不存在，
部分对象也将不存在，部分对象与整体对象之间具有同生共死的关系。
* 在组合关系中，成员类是整体类的一部分，而且整体类可以控制成员类的生命周期，即成员类的存在依赖于整体类。
* 在UML中，组合关系用带实心菱形的直线表示。

{% highlight java %}
public class Person {
  private Head head;
  private Arm leftArm;
  private Arm rightArm;
  private Leg leftLeg;
  private Leg rightLeg;
}
public class Head {
}
public class Arm {
}
public class Leg {
}
{% endhighlight %}

> 备注：当然，对于胳膊和腿，因为左右是不一样的，可以再抽象出来一层父类。但本例，简单起见，不再进行抽象。

![image]({{ '/image/post/2016/08/20160801-UML04.jpg' }})


## 聚合关系(Aggregation `/ˌægrɪ'geʃən/`)

聚合关系（Aggregation）：表示的是整体和部分的关系，整体与部分可以分开.

* 聚合关系(Aggregation) 表示一个整体与部分的关系。通常在定义一个整体类后，再去分析这个整体类的组成结构，从而找出一些成员类，
该整体类和成员类之间就形成了聚合关系。
* 在聚合关系中，成员类是整体类的一部分，即成员对象是整体对象的一部分，但是成员对象可以脱离整体对象独立存在。
* 在UML中，聚合关系用带空心菱形的直线表示。 

{% highlight java %}
public class Team {
  private Student student;
  public Team(Student student){
    this.student = student;
  }
}
public class Student {
}

{% endhighlight %}
![image]({{ '/image/post/2016/08/20160801-UML05.jpg' }})

## 关联关系(Association `/ə,soʃɪ'eʃən/` )

关联关系（Association）:类之间的联系，如客户和订单，每个订单对应特定的客户，每个客户对应一些特定的订单，再如篮球队员与球队之间的关联。
其中，关联两边的"employee"和“employer”标示了两者之间的关系，而数字表示两者的关系的限制，是关联两者之间的多重性。
通常有“*”（表示所有，不限），“1”（表示有且仅有一个），“0...”（表示0个或者多个），“0，1”（表示0个或者一个），
“n...m”(表示n到m个都可以),“m...*”（表示至少m个）。

* 关联关系(Association) 是类与类之间最常用的一种关系，它是一种结构化关系，用于表示一类对象与另一类对象之间有联系。
* 在UML类图中，用实线连接有关联的对象所对应的类，在使用Java、C#和C++等编程语言实现关联关系时，通常将一个类的对象作为另一个类的属性。
* 在使用类图表示关联关系时可以在关联线上标注角色名。

> 关联关系可细分为：单向关联，双向关联，自关联，多重性关联关系

表示方式 | 多重性说明
---|---
1..1 | 表示另一个类的一个对象只与一个该类对象有关系
0..* | 表示另一个类的一个对象与零个或多个该类对象有关系
1..* | 表示另一个类的一个对象与一个或多个该类对象有关系
0..1 | 表示另一个类的一个对象没有或只与一个该类对象有关系
m..n | 表示另一个类的一个对象与最少m、最多n个该类对象有关系 (m<=n)


{% highlight java %}
public class Teacher {
  private Student[] students; // 一对多
}
public class Student {
  private StudentCard studentCard; // 一对一
  private Teacher[] teachers; // 一对多
}
public class StudentCard {
  private Student student;
}
{% endhighlight %}

![image]({{ '/image/post/2016/08/20160801-UML06.jpg' }})

## 依赖关系(Dependence `/dɪ'pɛndəns/`)

依赖关系（Dependence）：假设A类的变化引起了B类的变化，则说名B类依赖于A类。

* 依赖关系(Dependency) 是一种使用关系，特定事物的改变有可能会影响到使用该事物的其他事物，在需要表示一个事物使用另一个事物时使用依赖
关系。
* 局部变量、方法的参数或者对静态方法的调用。大多数情况下，依赖关系体现在某个类的方法使用另一个类的对象作为参数。
* 在UML中，依赖关系用带箭头的虚线表示，由依赖的一方指向被依赖的一方。


{% highlight java %}
class Driver {
  public void drive(Car car) {
    car.move();
  }
}
class Car {
  public void move() {
    System.out.println("move……");
  }
}
{% endhighlight %}

![依赖关系图示]({{ '/image/post/2016/08/20160801-UML01.png' }})


## 源码文件目录

src/main/java/Y2016/M08/D01_UML


## 参考文章

http://blog.csdn.net/fatherican/article/details/44966891

http://www.open-open.com/lib/view/open1328059700311.html
