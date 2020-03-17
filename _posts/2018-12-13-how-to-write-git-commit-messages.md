---
layout:     post
title:      如何编写 Git commit 信息
date:       2018-12-13 13:34:43 +0800
postId:     2018-12-13-13-34-43
categories: []
tags:       [Tools,Git]
geneMenu:   true
excerpt:    如何编写 Git commit 信息
---

## Git版本规范

### 分支

* master分支为主分支(保护分支)，不能直接在master上进行修改代码和提交；
* develop分支为测试分支，所以开发完成需要提交测试的功能合并到该分支；
* feature分支为开发分支，大家根据不同需求创建独立的功能分支，开发完成后合并到develop分支；
* fix分支为bug修复分支，需要根据实际情况对已发布的版本进行漏洞修复；

### Tag

采用三段式，v版本.里程碑.序号，如v1.2.1

* 架构升级或架构重大调整，修改第2位
* 新功能上线或者模块大的调整，修改第2位
* bug修复上线，修改第3位

### changelog

版本正式发布后，需要生产changelog文档，便于后续问题追溯。

## Git 信息结构规约

In order to create a useful revision history, teams should first agree on a commit message convention 
that defines at least the following three things:
为了创一个有意义的历史提交信息记录，协作开发人员首先应该同意，一次提交信息至少应该定义如下三个部分：

* 格式（Style）

Markup syntax, wrap margins, grammar, capitalization, punctuation. Spell these things out, remove
the guesswork, and make it all as simple as possible. The end result will be a remarkably consistent log
that’s not only a pleasure to read but that actually does get read on a regular basis.
标记语法，包装边距，语法，大写，标点符号。 拼出这些东西，删除猜测，并尽可能简单。 最终结果将是一个非常一致的日志，
这不仅是一种阅读的乐趣，而且实际上定期阅读。


* 内容（Content）
What kind of information should the body of the commit message (if any) contain? What should it not contain?

* 元数据（Metadata）
How should issue tracking IDs, pull request numbers, etc. be referenced?

## 7条规则

* Separate subject from body with a blank line
* Limit the subject line to 50 characters
* Capitalize the subject line
* Do not end the subject line with a period
* Use the imperative mood in the subject line
* Wrap the body at 72 characters
* Use the body to explain what and why vs. how

## 最佳实践格式说明

Commit message一般包括三部分：Header、Body和Footer。

### Header

```log
type(scope):subject
```

* type：用于说明commit的类别，规定为如下几种
  - feat：新增功能；
  - fix：修复bug；
  - docs：修改文档；
  - refactor：代码重构，未新增任何功能和修复任何bug；
  - build：改变构建流程，新增依赖库、工具等（例如webpack修改）；
  - style：仅仅修改了空格、缩进等，不改变代码逻辑；
  - perf：改善性能和体现的修改；
  - chore：非src和test的修改；
  - test：测试用例的修改；
  - ci：自动化流程配置修改；
  - revert：回滚到上一个版本；
* scope：【可选】用于说明commit的影响范围
* subject：commit的简要说明，尽量简短 

### Body

对本次commit的详细描述，可分多行

### Footer

* 不兼容变动：需要描述相关信息
* 关闭指定issue，可以使用如下关键字关闭issue
  - close
  - closes
  - closed
  - fix
  - fixes
  - fixed
  - resolve
  - resolves
  - resolved

比如，一个提交信息中包含 Fixes #45 ，那么一旦这次提交被合并到默认分支，仓库中的45号issue就会自动关闭。
如果你在commit的开头使用多个上述关键字加issue的引用的话，你就可以关闭多个issues。

## 参考资料

* [How to Write a Git Commit Message](https://chris.beams.io/posts/git-commit/)
* [Git提交信息规范化](https://blog.csdn.net/ligang2585116/article/details/80284819)