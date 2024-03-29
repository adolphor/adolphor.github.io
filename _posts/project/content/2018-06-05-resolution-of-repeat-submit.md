---
layout:     post
title:      重复提交问题及解决方案
date:       2018-06-05 13:41:44 +0800
postId:     2018-06-05-13-41-44
categories: [Project]
keywords:   [架构设计]
---

设计表单请求的系统都会遇到表单重复提交的问题，因为重复提交的原因有很多，比如网络延迟、
用户提交操作速度过快、绕过前端重复提交验证的恶意重复提交等等。对于订单交易等比较敏感
的交互操作，防重复提交设计一定要做好，不然会出现订单重复、多次扣费等问题。

下面简单的介绍几种解决重复提交的处理机制：

## 前端验证

前面页面中增加token，使用js控制其生命周期。
* 页面加载的时候就生成，提交一次之后清空。
* 如果一个页面中有多个submit操作按钮，那么需要针对每个操作生成对应的token。
* 如果操作一次成功之后可以再次操作，那么需要在操作成功的回调函数中重新生成token。

## 后端验证

增加后端token验证机制，也是加载页面的时候生成token，提交表单的时候需要提交到后台服务器，
在后台比对此token是否有效：
* 无效token：拒绝提交
* 已使用：提示用户不能重复提交，那么也要求token失效之后也需要保留一段时间
* 未使用：正常提交用户请求

## 提交幂等性

其实经过上面两步的验证策略，按照道理来讲已经能够保证所有提交的有效性。那么，考虑最坏
情况下，如果有重复提交走到了业务处理过程中，那么就需要保证幂等性了。


## 参考资料

* [高并发下的幂等策略分析](https://blog.csdn.net/aly1989/article/details/52352726/)
* [互联网金融系统——交易防重设计实战](https://mp.weixin.qq.com/s/pIUPiOXfUX73RCuu7zoppg)

