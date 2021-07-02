---
layout:     post 
title:      IDEA - code style 
date:       2021-07-01 09:05:40 +0800 
postId:     2021-07-01-09-05-41
categories: [IDE]
tags:       [idea]
geneMenu:   true 
excerpt:    IDEA - code style
---

## 代码格式规范的必要性

* 统一Java代码格式规范，是「代码规范」的一部分
* 保证Format代码时不会引入格式上的干扰，避免Git版本管理增加额外比对工作
* 提升团队协作效率、Code Review效率

## 规范详情

参考 Google Java Code Style，以IDEA默认格式为蓝本，制定如下规范

### 文件编码

所有源文件使用 UTF-8 编码

### 缩进

使用空格，不要使用TAB缩进，避免不同编辑器显示格式错乱。

IDEA默认4个空格，修改为2个，节省横向空间，显示更多有效内容。

### 导入数量

导入包时使用单个声明导入，不要使用 * 通配符：

* 编译速度：因为按需导入机制的特性，需要在 CLASSPATH 下找到所有符合包名的类，在编译时会消耗性能。在小项目中，这个速度可以忽略。如果在大项目中，就会有明细差异
* 可读性：在使用 IDE 开发过程中，我们很少会在import中查看类的路径。但是如果需要我们在其他环境编辑文件，比如 vim，从import查看类的路径就很便捷了

IDEA默认超过默认数量（类5个、静态变量3个）会改为按需导入，所以需要修改数量提高自动转换阈值。 可将类修改为50个、静态变量30个。

### 自动换行

一行一个语句，每个语句后要换行。

过长语句默认自动换行字符是80，为了平衡单行内容长度和大屏显示器，修改为120。

注意：package语句和import语句不需要换行。

### 大括号

* 大括号不可省略，即使只有一行Java语句也需带上大括号。
* 非空块：K & R 风格
    - 左大括号前不换行
    - 左大括号后换行
    - 右大括号前换行
    - 右大括号后看情况
        - 如果右大括号是一个语句、函数体或类的终止，则右大括号后换行; 否则不换行。
        - 例如，如果右大括号后面是else或逗号，则不换行

### 用小括号来限定组

除非作者和reviewer都认为去掉小括号也不会使代码被误解，或是去掉小括号能让代码更易于阅读， 否则我们不应该去掉小括号。我们没有理由假设读者能记住整个Java运算符优先级表。

### 每次只声明一个变量

不要使用组合声明，比如 `int a, b;`

### 非C风格的数组声明

中括号是类型的一部分： `String[] args` ， 而非 `String args[]`。

### 注解

一个注解独占一行，不管有几个注解，都不要和方法签名或者字段放在同一行， 推荐做法：

```java
@Override
@Nullable
public String getNameIfPresent(){
  ...
  }
```

错误做法：

```java
@Override public String getNameIfPresent(){
  ...
  }
```

### 空格

空格相关代码范例：

```java

@Annotation(param1 = "value1", param2 = "value2")
@SuppressWarnings({"ALL"})
// 大括号左边有空格，右边换行
public class Foo<T extends Bar & Abba, U> {
  // 数组声明中 [] 是类型的一部分
  int[] X = new int[]{1, 3, 5, 6, 7, 87, 1213, 2};
  // 大括号空代码块可以不换行
  int[] empty = new int[]{};

  //三元表达式格式，每个符号中间均有空格 
  int j = i > 2 ? 1 : -1;

  // 签名后面的小括号前面不需要空格；多参数的逗号后面需要空格
  public void foo(int x, int y) {
    Runnable r = () -> {
    };
    // 赋值等号左右均有空格（概括来说就是操作符左右都有数据的时候，
    // 操作符和数据之间左右都需要空格）
    Runnable r1 = this::bar;
    // for循环关键字后面需要空格
    for (int i = 0; i < x; i++) {
      y += (y ^ 0x123) << 2;
    }
    for (int a : X) {
      // 方法调用的小括号前面不需要空格
      System.out.print(a);
    }
    // do关键字后面需要空格
    do {
      // try关键字后面需要空格
      try (MyResource r1 = getResource(); MyResource r2 = null) {
        // if关键字后面需要空格
        if (0 < x && x < 10) {
          // while关键字后面需要空格
          while (x != y) {
            x = f(x * 3 + 5);
          }
          // else前后面都需要空格
        } else {
          // synchronized关键字后面需要空格
          synchronized (this) {
            // switch关键字后面需要空格
            switch (e.getCode()) {
              //...
            }
          }
        }
      } catch (MyException e) {
        // catch关键字前后需要空格
      } finally {
        // finally关键字前后需要空格
        int[] arr = (int[]) g(y);
        x = y >= 0 ? arr[y] : -1;
        Map<String, String> sMap = new HashMap<String, String>();
        Bar.<String, Integer>mess(null);
      }
    }
    while (true);
  }

  void bar() {
    {
      return;
    }
  }
}

class Bar {
  static <U, T> U mess(T t) {
    return null;
  }
}

interface Abba {
}
```

### 代码分组

分组使用空一行即可，多行也可以但不推荐

* 字段分组：具有逻辑意义上同一组的字段可以使用空一行和其他字段分隔
* 方法体分组：具有逻辑意义上的段落可以使用空一行和其他代码分隔，表示一段逻辑的结束

### 代码排序

* 相同类型代码放在一起：属性、构造函数、静态方法、public方法、私有方法
* 重载方法放在一起：相同签名不同参数的方法放在一起
* 基础模板生成的CRUD放在一起，其他自定义方法放在后面；按照新增、修改、查询、删除的顺序排列

### 自动Format

#### 方法1：Save Actions 代码保存时自动格式化

![save-actions-plugin]({{ site.baseurl }}/image/post/2021/07/01/code-style/save-actions-plugin.png)
![save-actions-config]({{ site.baseurl }}/image/post/2021/07/01/code-style/save-actions-config.png)

#### 方法2：提交前自动Format

![format-before-commit]({{ site.baseurl }}/image/post/2021/07/01/code-style/format-before-commit.png)

## 参考资料

* [java import 导入包时，我们需要注意什么呢](https://xie.infoq.cn/article/e432cd576a77f38f55e64dd69)
