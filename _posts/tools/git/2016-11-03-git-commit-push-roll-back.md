---
layout:     post
title:      GIT回滚commit/push到指定版本
date:       2016-11-03 14:51:15 +0800
postId:     2016-11-03-14-51-15
categories: []
tags:       [Git]
geneMenu:   true
excerpt:    GIT回滚commit/push到指定版本
---

## 回滚指令

```shell
# 查看帮助文档
$ git reset --help
# 常用指令格式
$ git reset [-q] [<tree-ish>] [--] <paths>…
$ git reset (--patch | -p) [<tree-ish>] [--] [<paths>…
$ git reset [--soft | --mixed [-N] | --hard | --merge | --keep] [-q] [<commit>]
```

想要回滚到指定版本，还需要知道具体的版本号，使用log命令可以进行查看：

```shell
# 查看帮助文档
$ git log --help
# 常用指令格式
$ git log [<options>] [<revision range>] [[\--] <path>…
```

## 本地代码库回滚

```shell
# 回滚到commit-id，讲commit-id之后提交的commit都去除
$ git reset --hard commit-id
# 或者将最近3次的提交回滚
$ git reset --hard HEAD~3
```


 

## 远程代码库回滚

这个是重点要说的内容，过程比本地回滚要复杂。

应用场景：自动部署系统发布后发现问题，需要回滚到某一个commit，再重新发布

原理：先将本地分支退回到某个commit，删除远程分支，再重新push本地分支

操作步骤：

```shell
$ git checkout the_branch
$ git pull
# 备份一下这个分支当前的情况
$ git branch the_branch_backup
# 把the_branch本地回滚到the_commit_id
$ git reset --hard the_commit_id
# 删除远程 the_branch
$ git push origin :the_branch
# 用回滚后的本地分支重新建立远程分支
$ git push origin the_branch
# 如果前面都成功了，删除这个备份分支
$ git push origin :the_branch_backup
```

如果使用了gerrit做远程代码中心库和code review平台，需要确保操作git的用户具备分支的push权限，
并且选择了 Force Push选项（在push权限设置里有这个选项）

另外，gerrit中心库是个bare库，将HEAD默认指向了master，因此master分支是不能进行删除操作的，
最好不要选择删除master分支的策略，换用其他分支。如果一定要这样做，可以考虑到gerrit服务器上修改HEAD指针。。。不建议这样搞

## 参考资料

* [git代码库回滚](http://www.cnblogs.com/qualitysong/archive/2012/11/27/2791486.html)
