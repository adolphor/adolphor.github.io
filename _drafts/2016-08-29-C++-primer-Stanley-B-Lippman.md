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

上述代码中，`std::cin >> value` 的返回值是一个iostream，那么对I/O对象进行test的时候，规则如何呢？
在本范例中，因为使用value进行输入参数的接收，那么当输入的参数是int类型的时候，test为true；当输入的
参数不是int类型的时候，test为false。或者使用 *end of file* 来表示输入的结束，此时test也为false。
while检测到false之后，就会跳出循环，继续下面代码的运行。

Q：并不会抛异常……

##### 1.4.4. The if Statement

和Java无差啦……

范例：

    prog6.cc
{% highlight C++ %}
#include <iostream>
int main() {
    // currVal is the number we're counting; we'll read new values into val
    int currVal = 0, val = 0;
    // read first number and ensure that we have data to process
    if (std::cin >> currVal) {
        int cnt = 1; // store the count for the current value we're processing
        while (std::cin >> val) { // read the remaining numbers
            if (val == currVal) // if the values are the same
            ++cnt; // add 1 to cnt
            else { // otherwise, print the count for the previous value
                std::cout << currVal << " occurs "
                    << cnt << " times" << std::endl;
                currVal = val; // remember the new value
                cnt = 1; // reset the counter
            }
        } // while loop ends here
        // remember to print the count for the last value in the file
        std::cout << currVal << " occurs "
            << cnt << " times" << std::endl;
    } // outermost if statement ends here
    return 0;
}
{% endhighlight %}

    # Input
    42 42 42 42 42 55 55 62 100 100 100 done

    # Output
    42 occurs 5 times
    55 occurs 2 times
    62 occurs 1 times
    100 occurs 3 times

#### 1.5. Introducing Classes

类作为头文件 (header file) 引入，一般以 `.h` 作为后缀，或者 `.H`, `.hpp`, `.hxx` 作为后缀。
在C++标准库中，甚至没有后缀，编译器并不在乎这些后缀，但有些IDE可能在乎。


##### 1.5.1. The Sales_item Class

定义一个 `Sales_item` 类，实现如下：

    Sales_item.h
{% highlight C++ %}
#ifndef SALESITEM_H
#define SALESITEM_H
#include <iostream>
#include <string>

class Sales_item
{
public:
    Sales_item(const std::string &book):isbn(book),units_sold(0),revenue(0.0){}
    Sales_item(std::istream &is){ is >> *this;}
    friend std::istream& operator>>(std::istream &,Sales_item &);
    friend std::ostream& operator<<(std::ostream &,const Sales_item &);
public:
    Sales_item & operator+=(const Sales_item&);
public:
    double avg_price() const;
bool same_isbn(const Sales_item &rhs)const
{
    return isbn == rhs.isbn;
}
Sales_item():units_sold(0),revenue(0.0){}
public:
    std::string isbn;
    unsigned units_sold;
    double revenue;
};
 
using std::istream;
using std::ostream;
Sales_item operator+(const Sales_item &,const Sales_item &);
inline bool operator==(const Sales_item &lhs,const Sales_item &rhs)
{
    return lhs.units_sold == rhs.units_sold && lhs.revenue == rhs.revenue && lhs.same_isbn(rhs);
}
inline bool operator!=(const Sales_item &lhs,const Sales_item &rhs)
{
    return !(lhs == rhs);
}
 
inline Sales_item & Sales_item::operator +=(const Sales_item &rhs)
{
    units_sold += rhs.units_sold;
    revenue += rhs.revenue;
    return *this;
}
inline Sales_item operator+(const Sales_item &lhs,const Sales_item &rhs)
{
    Sales_item ret(lhs);
    ret += rhs;
    return ret;
}
inline istream& operator>>(istream &in,Sales_item &s)
{
    double price;
    in >> s.isbn >> s.units_sold >> price;
    if(in)
         s.revenue = s.units_sold * price;
    else
    s = Sales_item();
    return in;
}
inline ostream& operator<<(ostream &out,const Sales_item &s)
{
    out << s.isbn << "t" <<s.units_sold << "t" << s.revenue << "t" << s.avg_price();
    return out;
}
inline double Sales_item::avg_price() const
{
    if(units_sold)
        return revenue/units_sold;
    else
    return 0;
}
#endif
{% endhighlight %}

下面看一些操作范例：

    prog7.cc
{% highlight C++ %}
#include <iostream>
#include "Sales_item.h"
int main() {
    Sales_item book;
    // read ISBN, number of copies sold, and sales price
    std::cin >> book;
    // write ISBN, number of copies sold, total revenue, and average price
    std::cout << book << std::endl;
    return 0;
}
{% endhighlight %}

对于标准库的引用，使用 `#include <iostream>` 尖括号包裹，
对于自定义的类的引用，使用的 `#include "Sales_item.h"` 双引号包裹。

    # Input
    0-201-70353-X 4 24.99

    # Output
    0-201-70353-X   4       99.96   24.99

再来一个范例：

    prog8.cc
{% highlight C++ %}
#include <iostream>
#include "Sales_item.h"
int main() {
    Sales_item book;
    // read ISBN, number of copies sold, and sales price
    std::cin >> book;
    // write ISBN, number of copies sold, total revenue, and average price
    std::cout << book << std::endl;
    return 0;
}
{% endhighlight %}

    # Input
    0-201-78345-X 3 20.00
    0-201-78345-X 2 25.00

    # Output
    0-201-78345-X   5       110     22
    
##### 1.5.2. A First Look at Member Functions
先上代码：

    prog9.cc
{% highlight C++ %}
#include <iostream>
#include "Sales_item.h"
int main() {
    Sales_item item1, item2;
    std::cin >> item1 >> item2;
    // first check that item1 and item2 represent the same book
    if (item1.isbn() == item2.isbn()) {
        std::cout << item1 + item2 << std::endl;
        return 0; // indicate success
    } else {
        std::cerr << "Data must refer to same ISBN"
            << std::endl;
        return -1; // indicate failure
    }
}
{% endhighlight %}

代码 `item1.isbn() == item2.isbn()` 中就引用了一个叫做 `isbn` 的 `member function`。 
A member function is a function that is defined as part of a class. 
Member functions are sometimes referred to as methods.
作为类的一部分，有时候作为method使用？ method 和 function有啥子区别？

#### 1.6. The Bookstore Program



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
