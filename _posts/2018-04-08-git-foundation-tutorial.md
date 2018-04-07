---
layout:     post
title:      Git 相关基本操作
date:       2018-04-08 00:23:01 +0800
postId:     2018-04-08-00-23-01
categories: [blog]
tags:       [Git]
geneMenu:   true
excerpt:    Git 相关基本操作
---

## 分支

```Shell
# 创建并切换
git branch -b <branch-name>
# 切换分支
git checkout <branch-name>
# 删除分支
git branch -d <branch-name>
# 合并分支(分支所有提交记录)
git merge <branch-name>
# 合并分支(需要重新commit提交备注信息)
git merge --squash <branch-name>
```

## 修改历史

## 过滤文件

## 参考资料

* [聊下git merge --squash](https://www.cnblogs.com/wangiqngpei557/p/6026007.html)

