---
layout:     post
title:      JVM - 类加载器
date:       2021-10-01 23:06:06 +0800
postId:     2021-10-01-23-06-06
categories: [JVM]
keywords:   [Java, JVM]
---

## 类的生命周期
![类的生命周期]({{ site.baseurl }}/image/post/2021/10/01/01/类的生命周期.png)

## 类加载过程
Class 文件需要加载到虚拟机中之后才能运行和使用，那么虚拟机是如何加载这些 Class 文件呢？
系统加载 Class 类型的文件主要三步：加载 -> 连接 -> 初始化。
连接过程又可分为三步：验证 -> 准备 -> 解析。

### 加载
类加载过程的第一步，主要完成下面 3 件事情：
1. 通过全类名获取定义此类的二进制字节流 
2. 将字节流所代表的静态存储结构转换为方法区的运行时数据结构 
3. 在内存中生成一个代表该类的 Class 对象，作为方法区这些数据的访问入口

虚拟机规范上面这 3 点并不具体，因此是非常灵活的。比如："通过全类名获取定义此类的二进制字节流" 
并没有指明具体从哪里获取、怎样获取。比如：比较常见的就是从 ZIP 包中读取（日后出现的 JAR、EAR、
WAR 格式的基础）、其他文件生成（典型应用就是 JSP）等等。

加载阶段和连接阶段的部分内容是交叉进行的，加载阶段尚未结束，连接阶段可能就已经开始了。

### 验证
![类加载过程-验证]({{ site.baseurl }}/image/post/2021/10/01/01/类加载过程-验证.png)

### 准备
准备阶段是正式为类变量分配内存并设置类变量初始值的阶段，这些内存都将在方法区中分配。

### 解析
解析阶段是虚拟机将常量池内的符号引用替换为直接引用的过程。解析动作主要针对类或接口、
字段、类方法、接口方法、方法类型、方法句柄和调用限定符 7 类符号引用进行。

### 初始化
初始化阶段是执行初始化方法 <clinit> ()方法的过程，是类加载的最后一步，这一步 JVM 
才开始真正执行类中定义的 Java 程序代码(字节码)。

### 卸载
卸载类即该类的 Class 对象被 GC。

卸载类需要满足 3 个要求:
* 该类的所有的实例对象都已被 GC，也就是说堆不存在该类的实例对象。
* 该类没有在其他任何地方被引用
* 该类的类加载器的实例已被 GC

* 所以，在 JVM 生命周期内，由 jvm 自带的类加载器加载的类是不会被卸载的。
* 但是由我们自定义的类加载器加载的类是可能被卸载的。

## 类加载器

类加载阶段分为加载、连接、初始化三个阶段，而加载阶段需要通过类的全限定名来获取定义了此类的二进制字节流。
Java特意把这一步抽出来用类加载器来实现。把这一步骤抽离出来使得应用程序可以按需自定义类加载器。
并且得益于类加载器，OSGI、热部署等领域才得以在JAVA中得到应用。

在Java中任意一个类都是由这个类本身和加载这个类的类加载器来确定这个类在JVM中的唯一性。也就是你用
A类加载器加载的com.ClassDemo 和你B类加载器加载的com.ClassDemo 它们是不同的，也就是用
instanceof 这种对比都是不同的。所以即使都来自于同一个class文件但是由不同类加载器加载的那就是两个独立的类。

JVM 中内置了三个重要的 ClassLoader，除了 BootstrapClassLoader 其他类加载器均由 Java 
实现且全部继承自 **`java.lang.ClassLoader`**：

### BootstrapClassLoader(启动类加载器)
最顶层的加载类，它是属于虚拟机自身的一部分，由 C++实现，负责加载 `%JAVA_HOME%/lib` 目录下的
jar 包和类或者被 `-Xbootclasspath` 参数指定的路径中的所有类。

### ExtensionClassLoader(扩展类加载器)
扩展类加载器是Java实现的，独立于虚拟机，主要负责加载 `%JRE_HOME%/lib/ext` 目录下的 jar 
包和类，或被 `java.ext.dirs` 系统变量所指定的路径下的 jar 包。

### AppClassLoader(应用程序类加载器)
应用程序类加载器是Java实现的，独立于虚拟机。面向我们用户的加载器，负责加载当前应用 classpath 
下的所有 jar 包和类。


## 双亲委派模型

![类加载过程-双亲委派模型]({{ site.baseurl }}/image/post/2021/10/01/01/类加载过程-双亲委派模型.awebp)

在类加载的时候，系统会首先判断当前类是否被加载过。已经被加载的类会直接返回，否则才会尝试加载。
双亲委派的意思是如果一个类加载器需要加载类，那么首先它会把这个类请求委派给父类加载器去完成，每一层都是如此。
一直递归到顶层，当父加载器无法完成这个请求时，子类才会尝试去加载。这里的双亲其实就指的是父类，没有mother。
父类也不是我们平日所说的那种继承关系，只是调用逻辑是这样。

双亲委派模型不是一种强制性约束，也就是你不这么做也不会报错怎样的，它是一种JAVA设计者推荐使用类加载器的方式。
```java
{
    // First, check if the class has already been loaded 先判断class是否已经被加载过了
    Class<?> c = findLoadedClass(name);
    if (c == null) {
        long t0 = System.nanoTime();
        try {
            if (parent != null) {
                c = parent.loadClass(name, false);  //找他爸爸去加载
            } else {
                c = findBootstrapClassOrNull(name);  //没爸爸说明是顶层了就用Bootstrap ClassLoader去加载
            }
        } catch (ClassNotFoundException e) {
            // ClassNotFoundException thrown if class not found
            // from the non-null parent class loader
        }

        if (c == null) {
            // If still not found, then invoke findClass in order
            // to find the class.
            long t1 = System.nanoTime();
            c = findClass(name);    //最后如果没找到，那就自己找

            // this is the defining class loader; record the stats
            sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
            sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
            sun.misc.PerfCounter.getFindClasses().increment();
        }
    }
    if (resolve) {
        resolveClass(c);
    }
    return c;
}
```

### 好处

双亲委派有啥好处呢？它使得类有了层次的划分。就拿`java.lang.Object`来说，你加载它经过一层层委托
最终是由 `Bootstrap ClassLoader` 来加载的，也就是最终都是由 Bootstrap ClassLoader 去找
`<JAVA_HOME>\lib` 中 `rt.jar` 里面的 `java.lang.Object` 加载到JVM中。

这样如果有不法分子自己造了个 `java.lang.Object`，里面嵌了不好的代码，
如果我们是按照双亲委派模型来实现的话，最终加载到JVM中的只会是我们 `rt.jar` 里面的东西，
也就是这些核心的基础类代码得到了保护。因为这个机制使得系统中只会出现一个 `java.lang.Object`。
不会乱套了。你想想如果我们JVM里面有两个Object,那岂不是天下大乱了。

因此既然推荐使用这种模型当然是有道理了。

### 为什么需要自定义类加载器
使用类加载器的双亲委派模型机制，可以避免不法分子伪造核心类。那么为什么不直接使用一个类加载器来
实现所有类的加载呢？
自定义类加载器可以灵活解决很多问题：
* 实现热部署：OSGI
* 扩展加载源：比如可以从网络获取class二进制文件
* tomcat自定义加载器：同一个tomcat中部署多个应用的时候，实现相同版本的jar共享，不同版本的jar包隔离

## 自定义类加载器范例

```java
package y2021.m10.d01;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/10/1 23:49
 * @Email: 0haizhu0@gmail.com
 */
public class MyClassLoader extends ClassLoader {

  private String classpath;

  public MyClassLoader(String classpath) {
    // 让系统类加载器(AppClassLoader)成为该类加载器的父类加载器
    super();
    this.classpath = classpath;
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    byte[] classByte = getClassBytes(name);
    return super.defineClass(null, classByte, 0, classByte.length);
  }

  private byte[] getClassBytes(String name) {
    try {
      String fileName = getClassFile(name);
      FileInputStream fileInput = new FileInputStream(fileName);
      FileChannel channel = fileInput.getChannel();
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      WritableByteChannel byteChannel = Channels.newChannel(output);
      ByteBuffer buffer = ByteBuffer.allocate(1024);
      int flag;
      while ((flag = channel.read(buffer)) != -1) {
        if (flag == 0) break;
        buffer.flip();
        byteChannel.write(buffer);
        buffer.clear();
      }
      fileInput.close();
      channel.close();
      byteChannel.close();
      return output.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("MyClassLoader getClassBytes error: " + e.getMessage());
    }
  }

  private String getClassFile(String name) {
    //利用StringBuilder将包形式的类名转化为Unix形式的路径
    StringBuilder sb = new StringBuilder(classpath);
    sb.append("/")
      .append(name.replace('.', '/'))
      .append(".class");
    return sb.toString();
  }

}
```

```java
package y2021.m10.d01;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/10/2 00:15
 * @Email: 0haizhu0@gmail.com
 */
public class MyClassLoaderMainDemo {
  public static void main(String[] args) throws ClassNotFoundException {
    MyClassLoader myClassLoader = new MyClassLoader("src/main/java/y2021/m10/d01/");
    Class<?> aClass = myClassLoader.findClass("MyClassLoader");
    System.out.println(aClass.getName());
    System.out.println(myClassLoader.getParent());
  }
}
```

```
y2021.m10.d01.MyClassLoader
sun.misc.Launcher$AppClassLoader@18b4aac2
```

## 参考资料
* [JVM - 类加载器]({% post_url java/jvm/content/2021-10-01-01-jvm-load-class %})
* [说说双亲委派模型](https://juejin.cn/post/6844903838927814669)
* [Java内存管理-掌握自定义类加载器的实现（七）](https://aijishu.com/a/1060000000004451)

