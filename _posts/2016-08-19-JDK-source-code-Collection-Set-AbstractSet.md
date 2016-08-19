---
layout:     post
title:      【Java8源码阅读笔记】Collection框架之AbstractSet
date:       2016-08-19 10:14:44 +0800
postId:     2016-08-19-10-14-44
categories: [Collection]
tags:       [Collection, Set]
geneMenu:   true
excerpt:    【Java8源码阅读笔记】Collection框架之AbstractSet
---

## AbstractSet
Since 1.2，本类没有覆写 `AbstractCollection` 中的任何方法，
只是单纯的增加了 `equals` 和 `hashCode` 方法的实现。

## 接口实现

### boolean equals(Object o)
equals 方法判断的时候，先从最简单直接的特性进行比较：如果是自身，肯定相等；
如果不是Set类型，肯定不等；如果长度不等，肯定不等；
至此集合和参数长度相等，值没有重复，那么如果集合包含参数中的每一个元素，
则说明两者相等。

{% highlight java %}
public boolean equals(Object o) {
    if (o == this)              // 是否是自身
        return true;
    if (!(o instanceof Set))    // 是否是Set类型
        return false;
    Collection<?> c = (Collection<?>) o;
    if (c.size() != size())     // 长度是否相等
        return false;
    try {
        return containsAll(c);  // 包含参数中的所有元素 => 相等
    } catch (ClassCastException unused)   {
        return false;           // 类型转换异常
    } catch (NullPointerException unused) {
        return false;
    }
}
{% endhighlight %}


### int hashCode()
额，最简单的每个元素的哈希码求和……

{% highlight java %}
public int hashCode() {
    int h = 0;
    Iterator<E> i = iterator();
    while (i.hasNext()) {   // 遍历
        E obj = i.next();
        if (obj != null)
            h += obj.hashCode(); // 求和
    }
    return h;
}
{% endhighlight %}


### boolean removeAll(Collection<?> c)
AbstractCollection中已经实现了此方法，默认使用下面else中的实现。
覆写之后的实现是：进行判断之后，遍历小的那个集合来提高效率：

{% highlight java %}
public boolean removeAll(Collection<?> c) {
    Objects.requireNonNull(c);
    boolean modified = false;
    if (size() > c.size()) {    // 遍历较小的那个
        for (Iterator<?> i = c.iterator(); i.hasNext(); )
            modified |= remove(i.next());   // 位或操作：只要有一个true就是true，两个都是false才是false
    } else {
        for (Iterator<?> i = iterator(); i.hasNext(); ) {
            if (c.contains(i.next())) {
                i.remove();
                modified = true;
            }
        }
    }
    return modified;
}
{% endhighlight %}


## 接口继承

### AbstractCollection
add, addAll, clear, contains, containsAll, isEmpty, iterator, remove, retainAll, size, toArray, toArray, toString

## 遗留问题

* removeAll覆写应该不是单单为了提高这么一点效率吧？
那AbstractCollection中为什么没有使用这种实现方式呢？

## 参考资料

* [JDK文档 之 AbstractSet](https://docs.oracle.com/javase/8/docs/api/java/util/AbstractSet.html)

{% highlight java %}
{% endhighlight %}
