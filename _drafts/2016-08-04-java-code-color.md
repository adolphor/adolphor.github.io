---
layout:     post
title:      JAVA代码颜色调试
date:       2016-08-04 23:27:21 +0800
postId:     2016-08-04-23-27-21
categories: [posts, java]
tags:       [tag1, tag2]
geneMenu:   false
---

颜色调试页面

{% highlight java %}
package demo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Doc comment here for <code>SomeClass</code>
 *
 * @param T type parameter
 * @see Math#sin(double)
 */
@Deprecated
public class SomeClass<T extends Runnable> { // some comment
  private T field = null;
  private double unusedField = 12345.67890;
  private String anotherString = "Another\nString";
  public static int staticField = 0;
  public final int instanceFinalField = 0;

  public SomeClass(AnInterface param, int[] reassignedParam) {
    int localVar = 1;
    System.out.println(anotherString + toString() + localVar);
    long time = Date.parse("1.2.3"); // Method is deprecated
    int reassignedValue = this.staticField;
    reassignedValue++;
    field.run();
    new SomeAbstractClass() {
      {
        int a = 2;
      }
    };
  }
}

enum AnEnum {CONST1, CONST2}

interface AnInterface {
  int CONSTANT = 2;
  void method();
}

abstract class SomeAbstractClass {
}
{% endhighlight %}


