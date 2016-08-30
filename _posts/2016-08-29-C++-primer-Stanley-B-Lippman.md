---
layout:     post
title:      《C++ primer —— Stanley B. Lippman》读书笔记
date:       2016-08-29 14:02:57 +0800
postId:     2016-08-29-14-02-57
categories: [C/C++/C#]
tags:       [C/C++/C#, C++, 读书笔记]
geneMenu:   true
excerpt:    《C++ primer —— Stanley B. Lippman》读书笔记
---

## Part0. Introduction

### Chapter 1: Getting started
This chapter introduces most of the basic elements of C++: types, variables,
expressions, statements, and functions. Along the way, we’ll briefly explain how to
compile and execute a program.

* Writing a Simple C++ Program
* A First Look at Input/Output
* A Word about Comments
* Flow of Control
* Introducing Classes
* The Bookstore Program
* Chapter Summary
* Defined Terms

#### 1.1. Writing a Simple C++ Program
和Java一样，C++必须有一个main函数。main函数和一般的函数除了方法名，其余部分基本相同。
Different compilers use different suffix conventions; the most common 
include .cc, .cxx, .cpp, .cp, and .C。本例将文件名后缀为 `.cc`，将上面那个方法保存至 `prog1.cc`文件，

    prog1.cc
{% highlight C++ %}
int main() {
  return 0;
}
{% endhighlight %}

##### 1.1.1. Compiling and Executing Our Program
使用命令行编译：

    # 默认参数
    $ g++ prog1.cc
        => a.out
    # 指定文件名
    $ g++ -o prog1 prog1.cc
        => prog1

运行：

    $ ./a.out
    # 或者
    $ ./prog1

#### 1.2. A First Look at Input/Output
I/O 操作相关的库叫做iosteam，分别是：istream 和 ostream，并有一些内置对象，
所有标准卡的命名空间都是 `std`，使用 `::` (operator) 进行调用：

* istream
    * cin (读作 see-in)
* ostream
    * cout (读作 see-out)
    * cerr (读作 see-err)
    * clog (读作 see-log)
    * endl

范例：

    prog2.cc
    
{% highlight C++ %}
#include <iostream>     // 文件头，引用iostream库
int main() {
    std::cout << "Enter two numbers:" << std::endl; // 链式操作，等价于
    // std::cout << "Enter two numbers:";
    // std::cout << std::endl; // endl 叫做 manipulator，进行flush操作
    int v1 = 0, v2 = 0;
    std::cin >> v1 >> v2;   // 接收两个参数，分别赋值到 v1 和 v2，等价于
    // std::cin >> v1;
    // std::cin >> v2;
    std::cout << "The sum of " << v1 << " and " << v2
    << " is " << v1 + v2 << std::endl;
    return 0;
}
{% endhighlight %}

#### 1.3. A Word about Comments

* 单行注释：//
* 多行注释：/* */

#### 1.4. Flow of Control

流程控制

##### 1.4.1. The while Statement
while 语法结构，和Java一样：

    while (condition)
        statement

范例：

    prog3.cc
{% highlight C++ %}
#include <iostream>
int main() {
    int sum = 0, val = 1;
    // keep executing the while as long as val is less than or equal to 10
    while (val <= 10) {
        sum += val; // assigns sum + val to sum
        ++val; // add 1 to val
    }
    std::cout << "Sum of 1 to 10 inclusive is "
        << sum << std::endl;
    return 0;
}
{% endhighlight %}
    
##### 1.4.2. The for Statement
while 语法结构，和Java一样：

    for (init; condition; operate)
        statement

范例：

    prog4.cc
{% highlight C++ %}
#include <iostream>
int main() {
    int sum = 0;
    // sum values from 1 through 10 inclusive
    for (int val = 1; val <= 10; ++val)
        sum += val; // equivalent to sum = sum + val
    std::cout << "Sum of 1 to 10 inclusive by For loop is "
        << sum << std::endl;
    return 0;
}
{% endhighlight %}
    
##### 1.4.3. Reading an Unknown Number of Inputs
先看范例：

    prog5.cc
{% highlight C++ %}
#include <iostream>
int main() {
    int sum = 0, value = 0;
    // read until end-of-file, calculating a running total of all values read
    while (std::cin >> value)
        sum += value; // equivalent to sum = sum + value
    std::cout << "Sum is: " << sum << std::endl;
    return 0;
}
{% endhighlight %}

上述代码中，std::cin >> value 的返回值是一个iostream，那么对I/O对象进行test的时候，规则如何呢？
在本范例中，因为使用value进行输入参数的接收，那么当输入的参数是int类型的时候，test为true；当输入的
参数不是int类型的时候，test为false。或者使用 *end of file* 来表示输入的结束，此时test也为false。
while检测到false之后，就会跳出循环，继续下面代码的运行。

Q：并不会抛异常……

## Part1: The Basics

### Chapter2. Variables and Basic Types

### Chapter3. Strings, Vectors, and Arrays

### Chapter4. Expressions

### Chapter5. Statements

### Chapter6. Functions

### Chapter7. Classes

### Chapter8. The IO Library

## Part2: The C++ Library

### Chapter9. Sequential Containers

### Chapter10. Generic Algorithms

### Chapter11. Associative Containers

### Chapter12. Dynamic Memory

## Part3: Tools for Class Authors

### Chapter13. Copy Control

### Chapter14. Overloaded Operations andConversions

### Chapter15. Object-Oriented Programming

### Chapter16. Templates and GenericProgramming

## Part4: Advanced Topics

### Chapter17. Specialized Library Facilities

### Chapter18. Tools for Large Programs

### Chapter19. Specialized Tools andTechniques

## 参考资料

## TODO
* 编译环境和配置：win，linux，mac
* g++ 和 gcc 区别，是指不同的编译器？

* [C++ Primer (5th Edition)](https://book.douban.com/subject/24089577/)

{% highlight C++ %}
{% endhighlight %}
