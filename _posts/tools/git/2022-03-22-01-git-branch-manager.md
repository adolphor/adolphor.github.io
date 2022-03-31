---
layout:     post
title:      Git 分支管理
date:       2022-03-22 11:17:17 +0800
postId:     2022-03-22-11-17-17
categories: [Git]
keywords:   [Tools, Git]
---

## 分支管理

### 分支命名
* master：禁止提交
* dev-feat：功能开发分支
* test-日期：测试分支

### 使用流程
* 基于 `master` 创建 `dev-feat` 分支
* 每日基于 `master` 分支创建 `test-日期` 分支
* 功能开发完毕之后，merge 到 `test-日期` 分支进行测试
* 功能测试完成后，merge 到 `master` 分支正式提交

## 参考资料
* [Git 分支管理]({% post_url tools/git/2022-03-22-01-git-branch-manager %})
