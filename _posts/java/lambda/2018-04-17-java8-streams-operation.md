---
layout:     post
title:      Java 数据流操作
date:       2018-04-17 22:14:29 +0800
postId:     2018-04-17-22-14-29
categories: []
tags:       [Java,Java8]
geneMenu:   true
excerpt:    Java 数据流操作
---

<style>
table:first-of-type th:first-child {
    width: 20%;
    text-align: center;
}
table:first-of-type td:first-child {
    width: 20%;
    text-align: center;
}
</style>



流操作提供了一种新的数据处理方式，

## 流相关概念

主要有以下几个数据流操作相关的概念：

概念                   | 详解
--------------------- |  ---------------------------------
Sequence of elements  |  流是一组按照一定顺序排列的特定类型的元素
Source                |  源可以将集合、数组和 I/O 资源作为输入源
Aggregate operations  |  流提供了一系列的聚合操作，像 filter, map, limit, reduce, find, match 等
Pipelining            |  大多数流操作都是在管道（Pipeline）中进行的，这些操作叫做中间操作（intermediate operations），他们的功能是加载并处理数据流，之后输出到目标地点。一般将 `collect()` 方法用于数据流操作的末尾，标志着数据流处理的结束。
Automatic iterations  |  流操作提供了数据遍历的功能


流操作带来最大的遍历就是可以链式操作，更加语义化，使得代码可读性更高。

```java
public class Student {
    int no;
    String name;
    String sex;
    float height;
}

List<Student> list = new ArrayList<>();
list.add(new Student(1, "A", "M", 184));
list.add(new Student(2, "B", "G", 163));
list.add(new Student(3, "C", "M", 175));
list.add(new Student(4, "D", "G", 158));
list.add(new Student(5, "E", "M", 170));

// 流操作
list.stream()
    .filter(student -> student.getSex().equals("G"))
    .forEach(student -> System.out.println(student.toString()));
```

例子中，对聚合操作的使用可以归结为3个部分：

1. 创建Stream:通过stream()方法，取得集合对象的数据集。
2. Intermediate:通过一系列中间（Intermediate）方法，对数据集进行过滤、检索等数据集的再次处理。如上例中，使用filter()方法来对数据集进行过滤。
3. Terminal通过最终（terminal）方法完成对数据集中元素的处理。如上例中，使用forEach()完成对过滤后元素的打印。


## 数据流操作分类

像filter这样只描述Stream，最终不产生新集合的方法叫作惰性求值方法；而像count这样最终会从Stream产生值的方法叫作及早求值方法。
如果返回值是Stream，那么就是惰性求值；如果返回值不是Stream或者是void，那么就是及早求值。
在一个Stream操作中，可以有多次惰性求值，但有且仅有一次及早求值。

Stream类中定义的函数式接口汇总如下：

* Intermediate
  - `filter` 过滤：对原Stream按照指定条件过滤
  - map相关：将对于Stream中包含的元素使用给定的转换函数进行转换操作，新生成的Stream只包含转换生成的元素，元素类型是lambda表达式中返回的数据类型。
    - `map` 
    - `mapToInt`
    - `mapToLang`
    - `mapToDouble`
  - flatMap相关，与map方法类似，不同的是，该换转函数的对象是一个Stream，也不会再创建一个新的Stream，而是将原Stream的元素取代为转换的Stream。
    - `flatMap`
    - `flatMapToInt`
    - `flatMapToLang`
    - `flatMapToDouble`
  - `distinct` 去重
  - `sorted` (重载) 排序：对原Stream进行排序
  - `peek`：peek方法生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例），新Stream每个元素被消费的时候都会执行给定的消费函数，并且消费函数优先执行
  - `skip` 跳过：过滤掉原Stream中的前N个元素，返回剩下的元素所组成的新Stream
  - `takeWhile` ?
  - `dropWhile` ?
  - `empty`
  - `of` (重载)
  - `ofNullable`
  - `generate`
  - `iterate`
  - `concat` 
* Short-circuiting
  - `limit`：截取原Stream，截取后Stream的最大长度不能超过指定值N。
  - `anyMatch`：判断Stream中的是否有满足指定条件的元素，至少有一个满足就返回ture。
  - `allMatch`：判断Stream中的元素是否全部满足指定条件。
  - `noneMatch`：判断Stream中的所有元素是否满足指定的条件，如果所有元素都不满足条件，返回true；否则，返回false.
  - `findFirst`：于获取含有Stream中的第一个元素的Optional，如果Stream为空，则返回一个空的Optional。
  - `findAny`：获取含有Stream中的某个元素的Optional，如果Stream为空，则返回一个空的Optional。
* Terminal
  - `forEach`：遍历
  - `forEachOrdered` 与forEach类似，不同的是，如果该Stream预先设定了顺序，会按照预先设定的顺序执行（Stream是无序的），默认为元素插入的顺序。
  - `toArray` (重载)
  - `reduce` (重载)
  - `collect` (重载)
  - `min`：根据指定的Comparator，返回一个Optional，该Optional中的value值就是Stream中最小的元素。
  - `max`：根据指定的Comparator，返回一个Optional，该Optional中的value值就是Stream中最大的元素。
  - `count`：返回Stream中元素的个数。
  - 

## 创建Stream
我们有多种方式生成Stream：
1. Stream接口的静态工厂方法（注意：Java8里接口可以带静态方法）
    * `of` 其生成的Stream是有限长度的，Stream的长度为其内的元素个数
    
      ```java
      - of(T... values) // 返回含有多个T元素的Stream
      - of(T t)         // 返回含有一个T元素的Stream
      ```
    * `generator` 返回一个无限长度的Stream,其元素由Supplier接口的提供。一般无限长度的Stream会与filter、limit等配合使用，否则Stream会无限制的执行下去
    
      ```java
      - generate(Supplier<T> s)
      ```
    * `iterate` 其返回的也是一个无限长度的Stream，与generate方法不同的是，其是通过函数f迭代对给指定的元素种子而产生无限连续有序Stream，其中包含的元素可以认为是：seed，f(seed),f(f(seed))无限循环。
    
      ```java
      - iterate(T seed, UnaryOperator<T> f)
      ```
    * `empty` 返回一个空数据流
2. Collection接口和数组的默认方法（默认方法,也使Java的新特性之一，后续介绍），把一个Collection对象转换成Stream
    * Collection.`stream`
    * Arrays.`stream`

      ```java
      - Stream<T> stream(T[] array)
      ```
3. 其他
    * Random.`ints`()
    * BitSet.`stream`()
    * Pattern.`splitAsStream`(java.lang.CharSequence)
    * JarFile.`stream`()


创建stream的范例代码：

```java
// 1. 静态工厂
// -- 1.1 of
Stream<Integer> integerStream = Stream.of(1, 2, 3);
Stream<String> stringStream = Stream.of("A");
// -- 1.2 generator 下面的范例是返回一个随机值
Stream<Double> generateA = Stream.generate(new Supplier<Double>() {
    @Override
    public Double get() {
        return java.lang.Math.random();
    }
});
Stream<Double> generateB = Stream.generate(()-> java.lang.Math.random());
Stream<Double> generateC = Stream.generate(java.lang.Math::random);
// -- 1.3 iterate 返回有序无限集合，也需要使用filter，limit等函数截取
Stream.iterate(1, item -> item + 1)
        .limit(10)
        .forEach(System.out::println); 
        // 打印结果：1，2，3，4，5，6，7，8，9，10

// 2. Collection
int ids[] = new int[]{1, 2, 3, 4};
Arrays.stream(ids)
        .forEach(System.out::println);

```

## 部分操作详解

### map 
map 操作针对泛型，另外对于基本数据类似，官方提供了三个封装类：`IntStream`、`LongStream`、`DoubleStream`，这三个封装类都可以有map
对应的生成方法直接生成，封装类提供了基础stream类没有的一些方法，比如`sum`、`average`、`boxed`等，另外也是为了提高效率。

```java
List<Integer> numbers = Arrays.asList(3, 2, 8, 3, 7, 9, 5);
// 基本操作
numbers.stream().map(n -> n * n).filter(n -> n > 5).forEach(n -> System.out.println(n + "、"));

// 使用map 在 IntStream 的数据基础上生成新的其他类型的数据流，注意返回的数据类型
Stream<Person> pStream = numbers.stream().map(n -> new Person("Person", n)); // Person 类型
pStream.forEach(p -> System.out.println(p.name + "=>" + p.age + "\n"));

Stream<Integer> iStream = persons.stream().map(p -> ++p.age); // Person 生成 Integer 类型
pStream.forEach(a -> System.out.println(a + "\n"));

IntStream intStream = pStream.mapToInt(p -> ++p.age); // Person 生成 int 类型
intStream.forEach(a -> System.out.println(a + "、"));
intStream.sum();      // int 的额外操作
intStream.average();
intStream.boxed();
```

### flatMap
对于map 和 flatMap 区别，从下面这个例子来看更明显：
```java
List<String> list = Arrays.asList("hello welcome", "world hello", "hello world", "hello world welcome");
list.stream()
  .map(item -> Arrays.stream(item.split(" ")))
  .distinct().collect(Collectors.toList()).forEach(System.out::println);
list.stream()
  .flatMap(item -> Arrays.stream(item.split(" ")))
  .distinct().collect(Collectors.toList()).forEach(System.out::println);

```
打印结果如下：
```
java.util.stream.ReferencePipeline$Head@2fc14f68
java.util.stream.ReferencePipeline$Head@591f989e
java.util.stream.ReferencePipeline$Head@66048bfd
java.util.stream.ReferencePipeline$Head@61443d8f
----------------------------
hello
welcome
world
```

使用map 生成的流类型是 `List<String>`，使用 flatMap 生成的流是 `List<Stream<String>>`。（这就是扁平化的意义？）
```java
List<Stream<String>> listResult = list.stream().map(item -> Arrays.stream(item.split(" ")))
  .distinct().collect(Collectors.toList());
List<String> listResult2 = list.stream().flatMap(item -> Arrays.stream(item.split(" ")))
  .distinct().collect(Collectors.toList());
// 使用如下方式可以将非扁平化的数据流扁平化处理：
list.stream().map(item -> item.split(" ")).flatMap(Arrays::stream)
  .distinct().collect(Collectors.toList()).forEach(System.out::println);
```
map 和 flatMap 结合使用个，再看一个例子：
```java
List<String> list2 = Arrays.asList("hello", "hi", "你好");
List<String> list3 = Arrays.asList("zhangsan", "lisi", "wangwu", "zhaoliu");
list2.stream().flatMap(item -> list3.stream().map(item2 -> item + " " + item2))
  .collect(Collectors.toList()).forEach(System.out::println);
```

### peek 

peek方法生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例），
新Stream每个元素被消费的时候都会执行给定的消费函数，并且消费函数优先执行。

```java
Stream.of(1, 2, 3, 4, 5)
  .peek(integer -> System.out.println("accept by peek: " + integer))
  .forEach(System.out::println);
```

上面的例子中，foreach的时候每个元素都会先执行一次peek中的操作之后才会进行foreach中定义的操作，所以结果如下：
```
accept by peek: 1
1
accept by peek: 2
2
accept by peek: 3
3
accept by peek: 4
4
accept by peek: 5
5
```

### takeWhile

对于有序数据流，它像 filter，又像一个 fast-fail，会在第一个失败的元素截止，然后将前面所有通过的元素返回。比如下面的例子，
Predicate 是 n < 8，那么会在 9 的地方失败，然后返回 9 之前的所有数据，返回的数据是总的数据流的prefix。


```java
Stream.of(4, 1, 5, 2, 6, 9, 5, 4, 6, 8)
  .takeWhile(n -> n < 8)
  .forEach(System.out::print);
// 41526
```
### dropWhile

dropWhile 可以看做 takeWhile 取反，返回的数据是：从失败的那个数据开始，一直到数据流结束。

```java
Stream.of(4, 1, 5, 2, 6, 9, 5, 4, 6, 8)
  .dropWhile(n -> n < 8)
  .forEach(System.out::print);
// 95468
```

### ofNullable

将一个可能为空的数据封装为数据流进行操作，而不会出现空指针异常，直接看范例：
没有此方法的时候只能用如下方式处理：

```java
// findCustomer can return null
Customer customer = findCustomer(customerId);
 
Stream<Order> orders = customer == null
	? Stream.empty()
	: customer.streamOrders();
// do something with stream of orders ...
 
// alternatively, for the Optional lovers
Optional.ofNullable(customer)
	.map(Customer::streamOrders)
	.orElse(Stream.empty()
	. // do something with stream of orders
```

此方法可以这么处理：

```java
// findCustomer can return null
Customer customer = findCustomer(customerId);

Stream.ofNullable(customer)
	.flatMap(Customer::streamOrders)
	. // do something with stream of orders
```

### concat
TODO

### reduce 
TODO

## 参考资料

* [Java8 tutorials point](http://www.tutorialspoint.com/java8/index.htm)
* [Java 8 Stream Tutorial](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/)
* [Java 8 Parallel Streams](http://www.byteslounge.com/tutorials/java-8-parallel-streams)
* [Java 8系列之Stream的基本语法详解](https://my.oschina.net/spinachgit/blog/1604486)
* [Java 9 Additions To Stream](https://blog.codefx.org/java/java-9-stream/)
