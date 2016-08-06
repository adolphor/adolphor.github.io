---
layout:     post
title:      Java class 编译版本相关设置和查看
date:       2016-08-06 14:28:52 +0800
postId:     2016-08-06-14-28-53
categories: [java, class]
tags:       [java, class]
geneMenu:   true
---

## Java版本介绍

### 当前所有版本
目前为止（2016年8月），Java最新版本是Java8（1.8.0_102）。
各版本的对应关系如下所示：

JDK version| major version | minor version
---|---|---
1.0 | 45 | 3
1.1 | 45 | 3
1.2 | 46 | 0
1.3 | 47 | 0
1.4 | 48 | 0
1.5 | 49 | 0
1.6 | 50 | 0
1.7 | 51 | 0
1.8 | 52 | 0



### 各版本兼容性

Default javac Source and Target Settings

JDK/J2SDK|Default Source|Source Range|Default Target|Target Range
---|---|---|---|---
1.0.x	| 1.0	    | —	        | 1.1	| —
1.1.x	| 1.1	    | —	        | 1.1	| —
1.2.x	| 1.2	    | —	        | 1.1	| 1.1 - 1.2
1.3.x	| 1.2/1.3	| —	        | 1.1	| 1.1 - 1.3
1.4.x	| 1.2/1.3	| 1.2† - 1.4| 1.2	| 1.1 - 1.4
5 (1.5)	| 1.5	    | 1.2 - 1.5	| 1.5	| 1.1 - 1.5
6 (1.6)	| 1.5	    | 1.2 - 1.6	| 1.6	| 1.1 - 1.6
7 (1.7)	| 1.7	    | 1.2 - 1.7	| 1.7	| 1.1 - 1.7
8 (1.8)	| 1.8	    | 1.2 - 1.8	| 1.8	| 1.1 - 1.8

> † JDK 1.4.0 and 1.4.1 only accepted source 1.3 and 1.4.


## 查看class文件target版本的方法

### 测试代码
{% highlight java %}
public class ClassVersionDemo {
  public static void main(String[] args) {
    System.out.println("This is a test of class version.");
  }
}
{% endhighlight %}

编译指令：
{% highlight shell %}
javac ClassVersionDemo.java
{% endhighlight %}

### 使用命令行查看

On Unix / Linux:
{% highlight shell %}
javap -verbose ClassVersionDemo | grep "major"
{% endhighlight %}

On Windows:
{% highlight shell %}
javap -verbose ClassVersionDemo | findstr "major"
{% endhighlight %}

major version: 52，表示class文件版本为1.8。

如果想要查看文件全部内容，使用如下指令：
{% highlight shell %}
javap -verbose ClassVersionDemo
{% endhighlight %}

结果如下：
{% highlight java %}
Classfile /M:/ClassVersionDemo.class
  Last modified 2016-8-6; size 458 bytes
  MD5 checksum 4362619f681e34fa4a732a4ea5c855f6
  Compiled from "ClassVersionDemo.java"
public class ClassVersionDemo
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #6.#15         // java/lang/Object."<init>":()V
   #2 = Fieldref           #16.#17        // java/lang/System.out:Ljava/io/PrintStream;
   #3 = String             #18            // This is a test of class version.
   #4 = Methodref          #19.#20        // java/io/PrintStream.println:(Ljava/lang/String;)V
   #5 = Class              #21            // ClassVersionDemo
   #6 = Class              #22            // java/lang/Object
   #7 = Utf8               <init>
   #8 = Utf8               ()V
   #9 = Utf8               Code
  #10 = Utf8               LineNumberTable
  #11 = Utf8               main
  #12 = Utf8               ([Ljava/lang/String;)V
  #13 = Utf8               SourceFile
  #14 = Utf8               ClassVersionDemo.java
  #15 = NameAndType        #7:#8          // "<init>":()V
  #16 = Class              #23            // java/lang/System
  #17 = NameAndType        #24:#25        // out:Ljava/io/PrintStream;
  #18 = Utf8               This is a test of class version.
  #19 = Class              #26            // java/io/PrintStream
  #20 = NameAndType        #27:#28        // println:(Ljava/lang/String;)V
  #21 = Utf8               ClassVersionDemo
  #22 = Utf8               java/lang/Object
  #23 = Utf8               java/lang/System
  #24 = Utf8               out
  #25 = Utf8               Ljava/io/PrintStream;
  #26 = Utf8               java/io/PrintStream
  #27 = Utf8               println
  #28 = Utf8               (Ljava/lang/String;)V
{
  public ClassVersionDemo();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 1: 0

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #3                  // String This is a test of class version.
         5: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: return
      LineNumberTable:
        line 3: 0
        line 4: 8
}
SourceFile: "ClassVersionDemo.java"
{% endhighlight %}

可以看到，“ minor version: 0，major version: 52 ”，也就是1.8。


### 从class文件源码查看
使用文本编辑器（比如sublime Text）打开 ClassVersionDemo.class 文件，内容如下：
{% highlight byte %}
cafe babe 0000 0034 001d 0a00 0600 0f09
0010 0011 0800 120a 0013 0014 0700 1507
0016 0100 063c 696e 6974 3e01 0003 2829
5601 0004 436f 6465 0100 0f4c 696e 654e
756d 6265 7254 6162 6c65 0100 046d 6169
6e01 0016 285b 4c6a 6176 612f 6c61 6e67
2f53 7472 696e 673b 2956 0100 0a53 6f75
7263 6546 696c 6501 0015 436c 6173 7356
6572 7369 6f6e 4465 6d6f 2e6a 6176 610c
0007 0008 0700 170c 0018 0019 0100 2054
6869 7320 6973 2061 2074 6573 7420 6f66
2063 6c61 7373 2076 6572 7369 6f6e 2e07
001a 0c00 1b00 1c01 0010 436c 6173 7356
6572 7369 6f6e 4465 6d6f 0100 106a 6176
612f 6c61 6e67 2f4f 626a 6563 7401 0010
6a61 7661 2f6c 616e 672f 5379 7374 656d
0100 036f 7574 0100 154c 6a61 7661 2f69
6f2f 5072 696e 7453 7472 6561 6d3b 0100
136a 6176 612f 696f 2f50 7269 6e74 5374
7265 616d 0100 0770 7269 6e74 6c6e 0100
1528 4c6a 6176 612f 6c61 6e67 2f53 7472
696e 673b 2956 0021 0005 0006 0000 0000
0002 0001 0007 0008 0001 0009 0000 001d
0001 0001 0000 0005 2ab7 0001 b100 0000
0100 0a00 0000 0600 0100 0000 0100 0900
0b00 0c00 0100 0900 0000 2500 0200 0100
0000 09b2 0002 1203 b600 04b1 0000 0001
000a 0000 000a 0002 0000 0003 0008 0004
0001 000d 0000 0002 000e 
{% endhighlight %}

上面文件第一行，前四个字节——cafe babe，是magic字符，表示这个文件是一个class文件，  
再后面两个字节 —— 0000，表示 minor version，小版本号，  
再后面两个字节 —— 0034，表示的就是 major version，0x34(16进制) = 52(10进制)，也就是1.8。

## 使用 intellij 时编译版本的指定
当出现源文件版本和目标文件版本不一致的错误时，将如下配置更改为统一的版本即可解决问题。

### intellij设置  

#### intellij 的 Settings 配置  
Settings -> Build,Execution,Deployment -> Compiler -> Java Compliler  
project bytecode version：可以置空，使用默认配置  
per-module bytecode version：可以置空，使用project的默认配置  

#### intellij 的 Project Structure 配置  
Project Structure (Ctrl+Alt+Shif+S) ->   
Project -> Project SDK, Project language level  
Modules -> Language level  

### maven工具指定
    <configuration>
        <source>1.7</source>
        <target>1.7</target>
        <encoding>UTF-8</encoding>
    </configuration>



## 参考文献

[java compiler target version](http://javapapers.com/core-java/how-to-find-java-the-compiler-target-version-from-a-java-class-file)

[source_target_class_file_version](https://blogs.oracle.com/darcy/entry/source_target_class_file_version)

