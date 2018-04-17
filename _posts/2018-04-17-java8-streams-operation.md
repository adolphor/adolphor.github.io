---
layout:     post
title:      Java 数据流操作
date:       2018-04-17 22:14:29 +0800
postId:     2018-04-17-22-14-29
categories: [blog]
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
  - map相关：将对于Stream中包含的元素使用给定的转换函数进行转换操作，新生成的Stream只包含转换生成的元素。为了提高处理效率，官方已封装好了，三种变形：mapToDouble，mapToInt，mapToLong
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

## 创建Stream
我们有多种方式生成Stream：
1. Stream接口的静态工厂方法（注意：Java8里接口可以带静态方法）
    * of 其生成的Stream是有限长度的，Stream的长度为其内的元素个数
    
      ```java
      - of(T... values)：返回含有多个T元素的Stream
      - of(T t)：返回含有一个T元素的Stream
      ```
    * generator 返回一个无限长度的Stream,其元素由Supplier接口的提供。一般无限长度的Stream会与filter、limit等配合使用，否则Stream会无限制的执行下去
    
      ```java
      - generate(Supplier<T> s)
      ```
    * iterate 其返回的也是一个无限长度的Stream，与generate方法不同的是，其是通过函数f迭代对给指定的元素种子而产生无限连续有序Stream，其中包含的元素可以认为是：seed，f(seed),f(f(seed))无限循环。
    
      ```java
      - iterate(T seed, UnaryOperator<T> f)
      ```
    * empty 返回一个空数据流
2. Collection接口和数组的默认方法（默认方法,也使Java的新特性之一，后续介绍），把一个Collection对象转换成Stream
    * Collection.stream
    * Arrays.stream

      ```java
      - Stream<T> stream(T[] array)
      ```
3. 其他
    * Random.ints()
    * BitSet.stream()
    * Pattern.splitAsStream(java.lang.CharSequence)
    * JarFile.stream()

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



## 范例代码：

    StreamDemo.java

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Bob on 2016/8/25.
 */
public class StreamDemo {
  public static void main(String[] args) {

    List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
    List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");

    List<Person> persons = Arrays.asList(
            new Person("Max", 18),
            new Person("Peter", 23),
            new Person("Pamela", 23),
            new Person("David", 12)
    );

    System.out.print("原数据：");
    numbers.stream().forEach(n -> System.out.print(n + "、"));

    System.out.print("\n乘方：");
    numbers.stream().map(n -> n * n).forEach(n -> System.out.print(n + "、"));

    System.out.print("\n求和方法1：");
    int resultSum1 = numbers.stream().mapToInt(Integer::new).sum();
    System.out.print(resultSum1);

    System.out.print("\n排重：");
    numbers.stream().distinct().forEach(n -> System.out.print(n + "、"));

    System.out.print("\n过滤：");
    numbers.stream().filter(n -> n > 3).forEach(n -> System.out.print(n + "、"));

    System.out.print("\n截取：");
    numbers.stream().limit(4).forEach(n -> System.out.print(n + "、"));

    System.out.print("\n排序：");
    numbers.stream().sorted().forEach(n -> System.out.print(n + "、"));
    System.out.print("\n排序：");
    numbers.stream().sorted((a, b) -> a.compareTo(b)).forEach(n -> System.out.print(n + "、"));
    System.out.print("\n排序：");
    numbers.stream().sorted((a, b) -> b.compareTo(a)).forEach(n -> System.out.print(n + "、"));

    System.out.print("\n集合：");
    List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
    System.out.print(filtered);

    System.out.print("\n集合：");
    String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
    System.out.print(mergedString);


    // reduce相关操作：操作的是两个元素之间的关系
    System.out.print("\n求和方法2：");
    Optional<Integer> resultSum2 = numbers.stream().reduce((x, y) -> x + y);
    resultSum2.ifPresent(System.out::print);
    System.out.print("\n求和方法3：");
    Optional<Integer> resultSum3 = numbers.stream().reduce(Integer::sum);
    resultSum3.ifPresent(System.out::print);
    // —— 第一个参数0，相当于默认值
    System.out.print("\n求和方法4：");
    Integer resultSum4 = numbers.stream().reduce(0, (x, y) -> x + y);
    System.out.print(resultSum4);
    System.out.print("\n求和方法5：");
    Integer resultSum5 = numbers.stream().reduce(0, Integer::sum);
    System.out.print(resultSum5);

    System.out.print("\n求和方法6：");
    int resultSum6 = strings.stream().mapToInt(str -> str.length()).reduce(0, (x, y) -> x + y);
    System.out.print(resultSum6);

    /**
     * Collectors
     */
    System.out.print("\n分组：");
    Map<Integer, List<Person>> personsByAge = persons
            .stream()
            .collect(Collectors.groupingBy(p -> p.age));
    personsByAge
            .forEach((age, p) -> System.out.format("age %s: %s\n", age, p));

    System.out.print("\n平均值：");
    Double averageAge = persons
            .stream()
            .collect(Collectors.averagingInt(p -> p.age));
    System.out.println(averageAge);

    System.out.print("\n统计：");
    IntSummaryStatistics ageSummary = persons
            .stream()
            .collect(Collectors.summarizingInt(p -> p.age));
    System.out.println(ageSummary);

    System.out.print("\n统计：\n");
    IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x).summaryStatistics();
    System.out.println("\t最大值: " + stats.getMax());
    System.out.println("\t最小值: " + stats.getMin());
    System.out.println("\t求和: " + stats.getSum());
    System.out.println("\t平均值: " + stats.getAverage());

    System.out.print("\njoining：");
    String phrase = persons
            .stream()
            .filter(p -> p.age >= 18)
            .map(p -> p.name)
            .collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));
    System.out.println(phrase);

    System.out.print("\nCollectors.toMap：");
    Map<Integer, String> map = persons
            .stream()
            .collect(
                    Collectors.toMap(
                            p -> p.age,
                            p -> p.name,
                            (name1, name2) -> {
                              System.out.println(name2);
                              return name1 + ";" + name2;
                            }));

    System.out.print("\nCollectors.of：");
    Collector<Person, StringJoiner, String> personNameCollector =
            Collector.of(
                    () -> new StringJoiner(" | "),          // supplier
                    (j, p) -> j.add(p.name.toUpperCase()),  // accumulator
                    (j1, j2) -> j1.merge(j2),               // combiner
                    StringJoiner::toString);                // finisher
    String names = persons
            .stream()
            .collect(personNameCollector);
    System.out.println(names);  // MAX | PETER | PAMELA | DAVID

    System.out.println(map);

    /**
     * FlatMap
     */
    List<Foo> foos = new ArrayList<>();

// create foos
    IntStream
            .range(1, 4)
            .forEach(i -> foos.add(new Foo("Foo" + i)));
// create bars
    foos.forEach(f ->
            IntStream
                    .range(1, 4)
                    .forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));

    System.out.print("\nflatMap：");
    foos.stream()
            .flatMap(f -> f.bars.stream())
            .forEach(b -> System.out.println(b.name));

    IntStream.range(1, 4)
            .mapToObj(i -> new Foo("Foo" + i))
            .peek(f -> IntStream.range(1, 4)
                    .mapToObj(i -> new Bar("Bar" + i + " <- " + f.name))
                    .forEach(f.bars::add))
            .flatMap(f -> f.bars.stream())
            .forEach(b -> System.out.println(b.name));

    Outer outer = new Outer();
    if (outer != null && outer.nested != null && outer.nested.inner != null) {
      System.out.println(outer.nested.inner.foo);
    }

    Optional.of(new Outer())
            .flatMap(o -> Optional.ofNullable(o.nested))
            .flatMap(n -> Optional.ofNullable(n.inner))
            .flatMap(i -> Optional.ofNullable(i.foo))
            .ifPresent(System.out::println);

    /**
     * Reduce
     */
    persons
            .stream()
            .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)
            .ifPresent(System.out::println);    // Pamela
    Person result =
            persons
                    .stream()
                    .reduce(new Person("", 0), (p1, p2) -> {
                      p1.age += p2.age;
                      p1.name += p2.name;
                      return p1;
                    });

    System.out.format("name=%s; age=%s", result.name, result.age);

    Integer ageSum = persons
            .stream()
            .reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);

    System.out.println(ageSum);  // 76

    Integer ageSum2 = persons
            .stream()
            .reduce(0,
                    (sum, p) -> {
                      System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                      return sum += p.age;
                    },
                    (sum1, sum2) -> {
                      System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                      return sum1 + sum2;
                    });

    Integer ageSum3 = persons
            .parallelStream()
            .reduce(0,
                    (sum, p) -> {
                      System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                      return sum += p.age;
                    },
                    (sum1, sum2) -> {
                      System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                      return sum1 + sum2;
                    });


    /**
     * Parallel Streams
     */
    System.out.print("\n并行：");
    numbers.parallelStream().filter(n -> n > 3).forEach(n -> System.out.print(n + "、"));
    System.out.print("\n并行排序：");
    numbers.parallelStream().sorted().forEachOrdered(n -> System.out.print(n + "、"));

    ForkJoinPool commonPool = ForkJoinPool.commonPool();
    System.out.println(commonPool.getParallelism());    // 根据机器配置情况不同而不同

    Arrays.asList("a1", "a2", "b1", "c2", "c1")
            .parallelStream()
            .filter(s -> {
              System.out.format("filter: %s [%s]\n",
                      s, Thread.currentThread().getName());
              return true;
            })
            .map(s -> {
              System.out.format("map: %s [%s]\n",
                      s, Thread.currentThread().getName());
              return s.toUpperCase();
            })
            .forEach(s -> System.out.format("forEach: %s [%s]\n",
                    s, Thread.currentThread().getName()));

    Arrays.asList("a1", "a2", "b1", "c2", "c1")
            .parallelStream()
            .filter(s -> {
              System.out.format("filter: %s [%s]\n",
                      s, Thread.currentThread().getName());
              return true;
            })
            .map(s -> {
              System.out.format("map: %s [%s]\n",
                      s, Thread.currentThread().getName());
              return s.toUpperCase();
            })
            .sorted((s1, s2) -> {
              System.out.format("sort: %s <> %s [%s]\n",
                      s1, s2, Thread.currentThread().getName());
              return s1.compareTo(s2);
            })
            .forEach(s -> System.out.format("forEach: %s [%s]\n",
                    s, Thread.currentThread().getName()));

    persons
            .parallelStream()
            .reduce(0,
                    (sum, p) -> {
                      System.out.format("accumulator: sum=%s; person=%s [%s]\n",
                              sum, p, Thread.currentThread().getName());
                      return sum += p.age;
                    },
                    (sum1, sum2) -> {
                      System.out.format("combiner: sum1=%s; sum2=%s [%s]\n",
                              sum1, sum2, Thread.currentThread().getName());
                      return sum1 + sum2;
                    });

    /**
     * Spliterator
     */
    Spliterator<Integer> mySpliterator = numbers.spliterator();
    System.out.println("mySpliterator.estimateSize() = " + mySpliterator.estimateSize());
    // watch my ArrayList forEach() tutorial for a detailed explanation on how a Consumer functional interface works.
    Consumer<Integer> c = x -> System.out.println("mySpliterator.forEachRemaining = " + x);
    mySpliterator.forEachRemaining(c);
    System.out.println("mySpliterator.estimateSize() = " + mySpliterator.estimateSize());

  }
}

class Person {
  String name;
  int age;

  Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  @Override
  public String toString() {
    return name;
  }
}

class Foo {
  String name;
  List<Bar> bars = new ArrayList<>();

  Foo(String name) {
    this.name = name;
  }
}

class Bar {
  String name;

  Bar(String name) {
    this.name = name;
  }
}

class Outer {
  Nested nested;
}

class Nested {
  Inner inner;
}

class Inner {
  String foo;
}
```

## 参考资料

* [Java 8 新特性](http://blog.techbeta.me/tags/java8/)
* [Java 8 官方教程翻译](http://blog.csdn.net/code_for_fun/article/details/42169993)
* [Java8 tutorials point](http://www.tutorialspoint.com/java8/index.htm)
* [Java 8 Stream Tutorial](http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/)
* [Java 8 Parallel Streams](http://www.byteslounge.com/tutorials/java-8-parallel-streams)
* [JDK8新特性介绍](https://my.oschina.net/spinachgit/blog/1606567)

* [Java 8系列之Stream的基本语法详解](https://my.oschina.net/spinachgit/blog/1604486)
