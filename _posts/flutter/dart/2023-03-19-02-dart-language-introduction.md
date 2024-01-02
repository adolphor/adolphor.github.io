---
layout:     post
title:      Dart 语言入门
date:       2023-03-19 21:20:56 +0800
postId:     2023-03-19-21-20-56
categories: [Flutter]
keywords:   [Flutter,Dart]
---

为了学习Dart语言，先学习下Dart语言基础范例，熟悉下相关语法。
具体环境配置参考：[Dart和Flutter环境配置]({% post_url flutter/dart/2023-03-19-01-dart-develop-enviroment-configuration %})

## Hello World

每个应用都有一个 `main()` 方法，要打印文本到控制台，可以使用顶级（top-level）方法 `print()`。

```dart
void main() {
  print('Hello, World!');
}
```

> 问题

* main方法没有使用public等修饰符，是没有相关定义吗？
* print 是顶级方法，直接调用就行，还有哪些顶级方法？顶级方法能被覆写吗？

## 变量 Variables

即使在类型安全的 Dart 代码中，由于类型推断，大多数变量也不需要显式定义变量类型。

```dart
var name = 'Voyager I';
var year = 1977;
var antennaDiameter = 3.7;
var flybyObjects = ['Jupiter', 'Saturn', 'Uranus', 'Neptune'];
var image = {
  'tags': ['saturn'],
  'url': '//path/to/saturn.jpg'
};
```

> 问题

* 如果想要定义变量类型，怎么定义？
* print方法会打印出对象等具体信息，而不是像Java那样打印出来当前对象的ID，如下所示：

```dart
print(name);
print(year);
print(antennaDiameter);
print(flybyObjects);
print(image);
```
```
Voyager I
1977
3.7
[Jupiter, Saturn, Uranus, Neptune]
{tags: [saturn], url: //path/to/saturn.jpg}
```

## 控制流程语句 Control flow statements

没什么好解释的

```dart
if (year >= 2001) {
  print('21st century');
} else if (year >= 1901) {
  print('20th century');
}
for (final object in flybyObjects) {
  print(object);
}
for (int month = 1; month <= 12; month++) {
  print(month);
}
while (year < 2016) {
  year += 1;
}
```

> 问题

* 特殊注意下，foreach一个数组对象的时候，可以使用 `in` 关键字，类似于kotlin和swift语法，这是跟Java不同的地方。

## 函数 Functions

建议明确指明每个函数的参数值和返回值类型：

```dart
int fibonacci(int n) {
  if (n == 0 || n == 1) return n;
  return fibonacci(n - 1) + fibonacci(n - 2);
}
var result = fibonacci(20);
```

对于只包含一个语句的函数，可以使用更方便的 `=>` 箭头语法来表示。当将匿名函数作为参数传递时，此语法特别有用：
```dart
flybyObjects.where((name) => name.contains('turn')).forEach(print);
```

上面的代码中，`where()` 函数的参数是一个匿名函数：`(name) => name.contains('turn')`。
另外，代码中还使用了一个非匿名函数作为参数：顶级函数 `print()` 就是 `forEach()` 的参数。

待学习：可选参数、默认值、词法范围。

> 问题

无

## 注释 Comments

就直接使用 `//` 进行注释即可，也可使用 `/**/` 进行多行注释。
```dart
// This is a normal, one-line comment.

/// This is a documentation comment, used to document libraries,
/// classes, and their members. Tools like IDEs and dartdoc treat
/// doc comments specially.

/* Comments like these are also supported. */
```
> 问题

* 无

## 导入 Imports

使用 `import` 关键字，导入在其他类库中定义的APIs：
```dart
// Importing core libraries
import 'dart:math';

// Importing libraries from external packages
import 'package:test/test.dart';

// Importing files
import 'path/to/my_other_file.dart';
```

待学习：库前缀显示和隐藏、`deferred` 关键字实现懒加载。

> 问题

* 可见性的范围？

## 类 Classes
下面这个类的范例：包含三个属性、两个构造方法和一个函数。其中一个属性不能被直接设置，需要通过使用getter函数（而不是变量）来定义。
```dart
class Spacecraft {
  String name;
  DateTime? launchDate;

  // Read-only non-final property
  int? get launchYear => launchDate?.year;

  // Constructor, with syntactic sugar for assignment to members.
  Spacecraft(this.name, this.launchDate) {
    // Initialization code goes here.
  }

  // Named constructor that forwards to the default one.
  Spacecraft.unlaunched(String name) : this(name, null);

  // Method.
  void describe() {
    print('Spacecraft: $name');
    // Type promotion doesn't work on getters.
    var launchDate = this.launchDate;
    if (launchDate != null) {
      int years = DateTime.now().difference(launchDate).inDays ~/ 365;
      print('Launched: $launchYear ($years years ago)');
    } else {
      print('Unlaunched');
    }
  }
}
```
使用方式：
```dart
var voyager = Spacecraft('Voyager I', DateTime(1977, 9, 5));
voyager.describe();

var voyager3 = Spacecraft.unlaunched('Voyager III');
voyager3.describe();
```

待学习：初始化列表、可选new和const、重定向构造函数、 工厂构造函数、getter、setter 等等。

## 枚举 Enums
枚举，就不解释了吧，定义方式如下：
```dart
enum PlanetType { terrestrial, gas, ice }
```

下面是一个描述行星的类的增强型枚举声明的示例，具有一组定义的常量实例，即我们自己太阳系的行星：
```dart
/// Enum that enumerates the different planets in our solar system
/// and some of their properties.
enum Planet {
  mercury(planetType: PlanetType.terrestrial, moons: 0, hasRings: false),
  venus(planetType: PlanetType.terrestrial, moons: 0, hasRings: false),
  // ···
  uranus(planetType: PlanetType.ice, moons: 27, hasRings: true),
  neptune(planetType: PlanetType.ice, moons: 14, hasRings: true);

  /// A constant generating constructor
  const Planet({required this.planetType, required this.moons, required this.hasRings});

  /// All instance variables are final
  final PlanetType planetType;
  final int moons;
  final bool hasRings;

  /// Enhanced enums support getters and other methods
  bool get isGiant => planetType == PlanetType.gas || planetType == PlanetType.ice;
}
```
使用方法：
```dart
final yourPlanet = Planet.earth;

if (!yourPlanet.isGiant) {
  print('Your planet is not a "giant planet".');
}
```
待学习：强的枚举要求、自动引入的属性、访问枚举值名称、switch 语句支持等等。

## 继承 Inheritance
Dart 具有单一继承：
```dart
class Orbiter extends Spacecraft {
  double altitude;
  Orbiter(super.name, DateTime super.launchDate, this.altitude);
}
```

> 问题
> * 为什么有个 DateTime super.launchDate ？

待学习：关扩展类、可选@override注释

## Mixins
Mixin 是一种在多个类层次结构中重用代码的方法，可以解决不多重继承的情况下进行代码复用，以下是 mixin 声明：
```dart
mixin Piloted {
  int astronauts = 1;
  void describeCrew() {
    print('Number of astronauts: $astronauts');
  }
}
```
要将 mixin 的功能添加到类中，只需使用 mixin 扩展类即可：
```dart
class PilotedCraft extends Spacecraft with Piloted {
  // ···
}
```

## 接口和抽象类 Interfaces and abstract classes
Dart 没有interface关键字。相反，所有类都隐式定义了一个接口。因此，您可以实现任何类。
```dart
// 没看懂
class MockSpaceship implements Spacecraft {
  // ···
}
```
您可以创建一个抽象类以由具体类扩展（或实现）。抽象类可以包含抽象方法（具有空主体）。
```dart
abstract class Describable {
  void describe();
  void describeWithEmphasis() {
    print('=========');
    describe();
    print('=========');
  }
}
```

> 问题：没看懂上面接口的释义是什么意思

## 异步 Async
使用 `async` 和 `await` 关键字，可以避免`回掉地狱`，让代码可读性更强。
```dart
const oneSecond = Duration(seconds: 1);
// ···
Future<void> printWithDelay(String message) async {
  await Future.delayed(oneSecond);
  print(message);
}
```
上面的代码等价于：
```dart
Future<void> printWithDelay(String message) {
  return Future.delayed(oneSecond).then((_) {
    print(message);
  });
}
```

再来一个范例：
```dart
Future<void> createDescriptions(Iterable<String> objects) async {
  for (final object in objects) {
    try {
      var file = File('$object.txt');
      if (await file.exists()) {
        var modified = await file.lastModified();
        print(
            'File for $object already exists. It was modified on $modified.');
        continue;
      }
      await file.create();
      await file.writeAsString('Start describing $object in this file.');
    } on IOException catch (e) {
      print('Cannot create description for $object: $e');
    }
  }
}
```

> 问题：
> * 为什么可读性更强？
> * 为什么两个代码范例等价？
> * then后面的代码什么意思？

## 异常 Exceptions
要引发异常，请使用throw：
```dart
if (astronauts == 0) {
  throw StateError('No astronauts.');
}
```
要捕获异常，请使用try带有on或catch（或两者）的语句：
```dart
Future<void> describeFlybyObjects(List<String> flybyObjects) async {
  try {
    for (final object in flybyObjects) {
      var description = await File('$object.txt').readAsString();
      print(description);
    }
  } on IOException catch (e) {
    print('Could not describe object: $e');
  } finally {
    flybyObjects.clear();
  }
}
```
请注意，上面的代码是异步的； try适用于同步代码和async函数中的代码。

待学习：stack traces, rethrow, and the difference between Error and Exception.

## 参考资料
* [Dart 语言入门]({% post_url flutter/dart/2023-03-19-02-dart-language-introduction %})
* [官网 Introduction to Dart](https://dart.dev/language)
* [Flutter基础：Dart 语法 mixin](https://juejin.cn/post/7062516429429407781)
