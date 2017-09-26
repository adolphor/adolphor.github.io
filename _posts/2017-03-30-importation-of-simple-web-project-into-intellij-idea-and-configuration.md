---
layout:     post
title:      Intellij Idea 导入一般的web项目以及相关配置
date:       2017-03-30 10:14:17 +0800
postId:     2017-03-30-10-14-18
categories: [blog]
tags:       [IDE]
geneMenu:   true
excerpt:    Intellij Idea 导入一般的web项目以及相关配置
---

* 导入的时候选择new而不是open
* 选择从源码导入
* 选择新建项目，而不是eclipse项目
* 导入过程中，idea会自动识别相关模块，选择确定即可
* 导入成功之后，project structure中选择facets，会提示创建artifacts
* 创建artifacts之后，会提示解决lib依赖相关的问题
* 如果引用了httpServlet相关的jar包，需要依赖tomcat的servlet-api
    - 首先配置application servers，也就是tomcat运行环境
    - 在project structure中选择modules，右边加号选择liberary，之后application servers
    中选择上面配置的server即可，相当于eclipse中的target runtime
* 之后在run/debug configuration 中配置运行环境即可

## 参考资料

* 无
