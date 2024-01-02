---
layout:     post
title:      【转】lambda code review 实战
date:       2023-12-12 14:56:17 +0800
postId:     2023-12-12-14-56-17
categories: [Java]
keywords:   [Java]
---

* 一般来说`Optional`的通常玩法是非必要不拆包，一个`ofNullable`后立马跟个`ifPresentOrElse`的做法严重不推荐。跟普通`if != null` 没分别，`else`部分还难看。
* 在我看来，这段代码最大的问题是使用stream来产生副作用，也就是设置那个循环item的值。 函数式编程和命令式编程最大的不同就在于程序的副作用上。

## 源代码

> [LambdaCvDemo.java]({{ site.sourceUrl }}/src/main/java/y2023/m12/d12/LambdaCvDemo.java)

![image-alter]({{ site.baseurl }}/image/post/2023/12/12/01/lambda1.png)

## 优化代码
如果是我的话应该会倾向于把搜索和赋值（副作用）分开，例如：

> [LambdaCvRefactor.java]({{ site.sourceUrl }}/src/main/java/y2023/m12/d12/LambdaCvRefactor.java)

![image-alter]({{ site.baseurl }}/image/post/2023/12/12/01/lambda2.png)

也就是“apply”之前纯粹就是找出当前状态，以及当状态为RUNNING时的sessionDTO，但是不涉及任何具体业务逻辑。 
这部分如果需要可以直接抽取成一个独立函数复用。事实上实际开发中我大概率是会直接把这部分抽取成一个独立函数，
因为这部分是纯函数，单独做单元测试非常方便，只需要提供输入参数然后检查返回值。

apply部分才是跟具体业务相关的副作用。

其中的"pair" 和 “apply” 来自我自己定义的一个双元组数据结构。

> [LambdaPair.java]({{ site.sourceUrl }}/src/main/java/y2023/m12/d12/LambdaPair.java)

![image-alter]({{ site.baseurl }}/image/post/2023/12/12/01/struct.png)

一般来说`Optional`的通常玩法是非必要不拆包，一个`ofNullable`后立马跟个`ifPresentOrElse`的做法严重不推荐。
跟普通`if != null` 没分别，`else`部分还难看。 另外，可能你们公司有什么隐含规则，但就代码来看，
既然sessionDTO和paperDTO共用一个ExamStatusEnum 来表示状态，
这里当所有sessionDTO的状态不为RUNNING时就认定paperDTO状态为END就有点奇怪，
为啥不是当sessionDTO全是END的时候认定为paperDTO为END呢？
现在的逻辑在以后如果加入一个非RUNNING非END的状态就可能会给出错误结果，而且还不好排查。
如果是我的话应该会加入在找不到RUNNING时分开全END和不是全END两种情况处理，
如果不是全END的话，至少打个警告日志。如果这是关键路径，就干脆直接抛异常，
这样如果加入新状态又没修改这里的话，直接在开发期就爆了。
当然这只是一般情况的设想，你们公司可能对此有具体的设定。

## 参考资料
* [【转】lambda code review 实战]({% post_url java/lambda/2023-12-12-01-lambda-code-review-in-action %})
* [【转】如图，公司来了一个五年的java开发，这段代码是什么水平？ - kidneyball的回答 - 知乎](https://www.zhihu.com/question/594231110/answer/2984332733)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/12/12/01/xxx.png)
```
