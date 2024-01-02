---
layout:     post
title:      Dart - 语法基础
date:       2023-03-22 21:42:50 +0800
postId:     2023-03-22-21-42-50
categories: [Dart]
keywords:   [Flutter,Dart]
---

Dart语言基础语法学习笔记。

## 变量 Variables

Here’s an example of creating a variable and initializing it:
创建一个变量并初始化：

```dart
var name = 'Bob';
Object name = 'Bob';
String name = 'Bob';
```

* 上面的第一行代码会自动进行类型推断，推断出来的变量类型是 String。
* 如果不指定类型，像第二行那样，可以设置为 Object
* 也可以明确指出变量类型是 String，像第三行那样

### 默认值 Default value

具有可空类型的未初始化变量的初始值为null。即使是数字类型的变量最初也是空的，因为数字——就像 Dart 中的其他一切一样——都是对象。
```dart
int? lineCount;
assert(lineCount == null);
```

如果启用`空安全`，则必须在使用`不可空变量`之前初始化它们的值：
```dart
int lineCount = 0;
```

您不必在声明局部变量的地方对其进行初始化，但需要在使用前为其赋值。例如，以下代码是有效的，因为 Dart 
可以lineCount在它传递给时检测到它是非空的print()：
```dart
int lineCount;
if (weLikeToCount) {
  lineCount = countLines();
} else {
  lineCount = 0;
}
print(lineCount);
```

顶级和类变量被延迟初始化；初始化代码在第一次使用变量时运行。

### 惰性初始化变量 Late variables
修饰符late有两个用例：
* 声明一个不可为 null 的变量，该变量在声明后初始化。
* 延迟初始化变量。

```dart
late String description;
void main() {
  description = 'Feijoada!';
  print(description);
}
```

这种惰性初始化在以下几种情况下很方便：
* 该变量可能不需要，并且初始化它的成本很高。
* 您正在初始化一个实例变量，它的初始化程序需要访问this.

在下面的示例中，如果temperature从未使用过变量，则readThermometer()永远不会调用昂贵的函数：
```dart
// This is the program's only call to readThermometer().
late String temperature = readThermometer(); // Lazily initialized.
```

### final 和 const
如果一个变量初始化之后不打算再更改这个值，则可以声明为 final 或者 const：
* final 变量只能设置一次
* const 变量是编译时常量

> 注意： 实例变量可以final但不可以const。因为编译都时候，实例变量还没初始化，也就是不存在呢。

下面是创建和设置变量的示例final：
```dart
final name = 'Bob'; // Without a type annotation
final String nickname = 'Bobby';
```

> 问题：
> * 为什么可以缺省类型声明？

## 操作符 Operators

![全部运算符]({{ site.baseurl }}/image/post/2023/03/22/02/全部运算符.png)

### 算术操作符 Arithmetic operators

| 操作符 | 含义     | 备注  |
|-----|--------|-----|
| +   | 加      |     |
| _   | 减      |     |
| *   | 乘      |     |
| /   | 除      |     |
| ~/  | 除，返回整数 |     |
| %   | 模      |     |

范例：
```dart
assert(2 + 3 == 5);
assert(2 - 3 == -1);
assert(2 * 3 == 6);
assert(5 / 2 == 2.5); // Result is a double
assert(5 ~/ 2 == 2); // Result is an int
assert(5 % 2 == 1); // Remainder

assert('5/2 = ${5 ~/ 2} r ${5 % 2}' == '5/2 = 2 r 1');
```

自增和自减操作符，和Java并无区别：

| 操作符    | 含义            | 备注          |
|--------|---------------|-------------|
| ++var  | var = var + 1 | 返回值：var + 1 |
| var++  | var = var + 1 | 返回值：var     |
| --var  | var = var - 1 | 返回值：var - 1 |
| var--- | var = var - 1 | 返回值：var     |

### 相等和关系运算符 Equality and relational operators

相等和关系运算符，一般使用和Java区别不大：

| 操作符 | 含义   | 备注  |
|-----|------|-----|
| ==  | 相等   |     |
| !=  | 不等   |     |
| \>  | 大于   |     |
| <   | 小于   |     |
| \>= | 大于等于 |     |
| \<= | 小于等于 |     |

但是需要注意，`==` 运算符和Java有区别（待验证）：
* Dart的 == 比对的是两个实例的内容是否相同，而不是两个实例的内存地址是否相同
* Dart的 identical() 方法，比对的才是两个实例是否是同一个对象

### 类型检查操作符

`as` 类似于Java中的强制类型转换操作：

| 操作符 | 含义   | 备注  |
|-----|------|-----|
| as  | 类型转换（也用于指定库前缀）   |     |
| is  | 如果对象具有指定类型则为真   |     |
| is! | 如果对象没有指定类型则为真   |     |

```dart
(employee as Person).firstName = 'Bob';

// Type check
if (employee is Person) {
  employee.firstName = 'Bob';
}
```

### 赋值运算符 Assignment operators

基本上都和Java一样，不详细介绍了：

![赋值运算符]({{ site.baseurl }}/image/post/2023/03/22/02/赋值运算符.png)

可以使用=运算符赋值，但要仅在分配给变量为 null 时分配，请使用 `??=` 运算符：
```dart
// Assign value to a
a = value;
// Assign value to b if b is null; otherwise, b stays the same
b ??= value;
```

### 逻辑运算符 Logical operators

基本上都和Java一样，不详细介绍了：

| 操作符 | 含义  | 备注  |
|-----|-----|-----|
| !   | 非   |     |
| \|\|     | 或   |     |
| &&  | 与   |     |

### 位和移位运算符 Bitwise and shift operators

![位运算符]({{ site.baseurl }}/image/post/2023/03/22/02/位运算符.png)

```dart
final value = 0x22;
final bitmask = 0x0f;

assert((value & bitmask) == 0x02); // AND
assert((value & ~bitmask) == 0x20); // AND NOT
assert((value | bitmask) == 0x2f); // OR
assert((value ^ bitmask) == 0x2d); // XOR
assert((value << 4) == 0x220); // Shift left
assert((value >> 4) == 0x02); // Shift right
assert((value >>> 4) == 0x02); // Unsigned shift right
assert((-value >> 4) == -0x03); // Shift right
assert((-value >>> 4) > 0); // Unsigned shift right
```

### 三元运算符（条件表达式） Conditional expressions

使用形式：`condition ? expr1 : expr2`，范例如下：
```
var visibility = isPublic ? 'public' : 'private';
```

如果布尔表达式测试为 null，请考虑使用`??`，是一种针对空值判断的特殊优化：
```dart
String playerName(String? name) => name ?? 'Guest';

// 前面的示例至少可以用其他两种方式编写
// Slightly longer version uses ?: operator.
String playerName(String? name) => name != null ? name : 'Guest';

// Very long version uses if-else statement.
String playerName(String? name) {
  if (name != null) {
    return name;
  } else {
    return 'Guest';
  }
}
```

### 级联符号 Cascade notation
这是Java所没有的全新操作符：
级联 ( `..`, `?..`) 允许您对同一对象进行一系列操作。除了访问实例成员之外，您还可以在同一个对象上调用实例方法。
这通常可以节省您创建临时变量的步骤，并允许您编写更流畅的代码。

考虑以下代码，构造函数Paint()返回一个Paint对象。级联表示法后面的代码对此对象进行操作，忽略可能返回的任何值：
```dart
var paint = Paint()
  ..color = Colors.black
  ..strokeCap = StrokeCap.round
  ..strokeWidth = 5.0;
// 等效于如下代码：
var paint = Paint();
paint.color = Colors.black;
paint.strokeCap = StrokeCap.round;
paint.strokeWidth = 5.0;
```

如果级联操作的对象可以为空，则使用空短级联 ( ?..) 进行第一个操作。从?..保证不会对该空对象尝试任何级联操作：
```dart
querySelector('#confirm') // Get an object.
  ?..text = 'Confirm' // Use its members.
  ..classes.add('important')
  ..onClick.listen((e) => window.alert('Confirmed!'))
  ..scrollIntoView();
// 等效于如下代码：
var button = querySelector('#confirm');
button?.text = 'Confirm';
button?.classes.add('important');
button?.onClick.listen((e) => window.alert('Confirmed!'));
button?.scrollIntoView();
```

还可以嵌套级联，例如：
```dart
final addressBook = (AddressBookBuilder()
      ..name = 'jenny'
      ..email = 'jenny@example.com'
      ..phone = (PhoneNumberBuilder()
            ..number = '415-555-0100'
            ..label = 'home')
          .build())
    .build();
```

### 其他操作符

| 操作符 | 含义       | 备注                                                                                                 |
|-----|----------|----------------------------------------------------------------------------------------------------|
| ()  | 函数应用     | 表示函数调用                                                                                             |
| []  | 下标访问	    | 表示对可重写运算符的调用[]；示例：`fooList[1]`将 int 传递1给以fooList访问索引处的元素1                                            |
| ?[] | 条件下标访问   | 类似[]，但最左边的操作数可以为空；示例：`fooList?[1]`将 int 传递1给以fooList访问索引处的元素1，除非fooList为 null（在这种情况下，表达式的计算结果为 null） |
| .   | 会员权限	    | 引用表达式的属性；示例：从表达式中`foo.bar`选择属性barfoo                                                                 |
| ?.  | 有条件的成员访问 | 类似`.`，但最左边的操作数可以为空；示例：从表达式中`foo?.bar`选择属性，除非为空（在这种情况下，值为空）`barfoofoofoo?.bar`                            |
| !   | 空断言运算符   | 将表达式转换为其基础的不可空类型，如果转换失败则抛出运行时异常；示例：`foo!.bar`断言foo为非空并选择属性bar，除非foo为空，在这种情况下会抛出运行时异常                 |

## 注释 Comments
* 单行注释：//
* 多行注释：/**  */
* 文档注释：略，以后再看吧

## 注解 Metadata

注解可以出现在库、类、typedef、类型参数、构造函数、工厂、函数、字段、参数或变量声明之前以及导入或导出指令之前。
您可以使用反射在运行时检索注解。

### 内置注解
注解以`@`开头，后跟对编译时常量（例如deprecated）的引用或对常量构造函数的调用，内置的三个可用注解：
* @Deprecated
* @deprecated
* @override

### 自定义注解

```dart
class Todo {
  final String who;
  final String what;

  const Todo(this.who, this.what);
}
```

使用范例：
```dart
@Todo('Dash', 'Implement this function')
void doSomething() {
  print('Do something');
}
```

## 类库和导入 Libraries & imports

创建模块化和可共享的代码库。库不仅提供 API，而且还是一个隐私单元：以下划线 `_` 开头的标识符仅在库内部可见。
每个 Dart 应用程序都是一个库，即使它不使用library指令。

参考：[为什么 Dart 使用下划线而不是像publicor 这样的访问修饰符关键字private](https://github.com/dart-lang/sdk/issues/33383)

### 使用方式 Using libraries
```dart
// dart内置库
import 'dart:html';
// The package: scheme specifies libraries provided by a package manager such as the pub tool
import 'package:test/test.dart';
```

### 指定库前缀 Specifying a library prefix
如果 library1 和 library2 都有一个 Element 类，那么您可能有这样的代码：
```dart
import 'package:lib1/lib1.dart';
import 'package:lib2/lib2.dart' as lib2;

// Uses Element from lib1.
Element element1 = Element();

// Uses Element from lib2.
lib2.Element element2 = lib2.Element();
```

### 仅导入库的一部分
```dart
// Import only foo.
import 'package:lib1/lib1.dart' show foo;

// Import all names EXCEPT foo.
import 'package:lib2/lib2.dart' hide foo;
```

### 延迟加载
使用延迟加载的一些情况：
* 减少网络应用程序的初始启动时间。
* 执行 A/B 测试——例如，尝试算法的替代实现。
* 加载很少使用的功能，例如可选屏幕和对话框。

要延迟加载库，您必须首先使用deferred as：
```dart
import 'package:greetings/hello.dart' deferred as hello;
```

当您需要库时， loadLibrary()使用库的标识符调用：
```dart
Future<void> greet() async {
  // await关键字暂停执行，直到加载库。
  await hello.loadLibrary();
  hello.printGreeting();
}
```

## Keywords

| column1   | column2    | column3   | column4 |
|-----------|------------|-----------|---------|
| abstract  | else       | import    | show    |
| as        | enum       | in        | static  |
| assert    | export     | interface | super   |
| async     | extends    | is        | switch  |
| await     | extension  | late      | sync    |
| break     | external   | library   | this    |
| case      | factory    | mixin     | throw   |
| catch     | false      | new       | true    |
| class     | final      | null      | try     |
| const     | finally    | on        | typedef |
| continue  | for        | operator  | var     |
| covariant | Function   | part      | void    |
| default   | get        | required  | while   |
| deferred  | hide       | rethrow   | with    |
| do        | if         | return    | yield   |
| dynamic   | implements | set       |         |

## 参考资料

* [Dart - 语法基础]({% post_url flutter/dart/2023-03-22-02-dart-syntax-basics %})
* [官网 Dart Variables](https://dart.dev/language/variables)
