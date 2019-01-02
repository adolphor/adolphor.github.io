package Y2018.M04.D17_Java8.streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
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
    int resultSum1 = numbers.stream().mapToInt(Integer::intValue).sum();
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
