# 草稿箱

* 一般草稿文件名不包含日期
* 预览草稿的方法：  

```
jekyll server --drafts --incremental --port 1888
```

# 语法备忘

## 文件引用

    [Hadoop，Spark，HBase 开发环境配置]({% post_url 2016-10-28-hadoop-spark-hbase-develop-environment %})

## 目录ID，目录跳转锚点

    ## lambda表达式（Lambda Expressions） {#LambdaExpressions}

## 代码
* 使用此代码格式的时候，要注意前面要有一空行。

```java
System.out.print("我前面有一空行！");
```
* 在 * 列表中也可以使用
    - 比如在此处使用，需要注意的是下面的代码段要比子列表再缩进一个等级样式才正确

    ```java
    System.out.print("我一可以缩进！");
    ```

## 图片
![iterm2login]({{ site.baseurl }}/image/post/2017/06/26/20170626-0301-iterm2-config.jpg)

## 样式

第一个table的样式：
```
<style>
table:nth-of-type(1) tr th:first-child {
    width: 10%;
    text-align: center;
}
table:nth-of-type(1) tr td:first-child {
    width: 10%;
    text-align: center;
}
</style>
```