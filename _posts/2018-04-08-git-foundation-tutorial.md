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

## Git 分区

* 工作区
  - 新增、修改、删除等文件操作都发生在工作区
* 暂存区
  - 新增的文件状态是 Untracked
  - add之后的文件被索引在暂存区
* 版本库
  - commit 仅对暂存区的文件有效，对工作区的文件没有影响
  - commit之后的代码就被在版本库中进行管理

## 

## 版本比对

* 先安装个新工具
```Sehll
# 安装
brew install icdiff
# 配置 ~/.gitconfig
[difftool "icdiff"]
  cmd = /usr/bin/icdiff --line-numbers $LOCAL $REMOTE
```

* git diff 比较的是工作区和暂存区的差别
* git diff --cached 比较的是暂存区和版本库的差别
* git diff HEAD 可以查看工作区和版本库的差别

## 分支操作

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
# 删除本地分支

# 删除远程分支
git push origin --delete <branchName>
git push origin :<branchName>
```

重命名分支操作麻烦一些：
```Shell
# 删除远程分位置
git push --delete origin devel
# 重命名本地分支
git branch -m devel develop
# 推送本地分支
git push origin develop
```

## 撤销工作区的修改

### 使用 reset 回退
```Shell
git reset HEAD filename          回退文件，将文件从暂存区回退到工作区
git reset HEAD^回退版本           一个^表示一个版本，可以多个，另外也可以使用 git reset HEAD～n这种形式
```

* soft
  - git reset --soft HEAD～1         
  - 意为将版本库软回退1个版本，所谓软回退表示将本地版本库的头指针全部重置到指定版本，且将这次提交之后的所有变更都移动到暂存区
* mixed
  - git reset HEAD～1 
  - 意为将版本库回退1个版本，将本地版本库的头指针全部重置到指定版本，且会重置暂存区，即这次提交之后的所有变更都移动到未暂存阶段  
* hard
  - git reset --hard HEAD～1 
  - 意为将版本库回退1个版本，但是不仅仅是将本地版本库的头指针全部重置到指定版本，也会重置暂存区，并且会将工作区代码也回退到这个版本

### 使用检出 checkout 进行本地回退
```Shell
# 使用暂存区覆盖工作区的修改
git checkout .
# 使用版本库覆盖工作区的修改，暂存区的修改也会被清除
git checkout HEAD .
git checkout HEAD
```

### 回退远程仓库

在使用reset回退本地记录之后，使用如下指令进行远程回退：

```Shell
git push origin HEAD --force #远程提交回退
```


## 过滤文件

```Shell
/abc/             过滤整个文件夹
!/abc/mk.d        特殊指定这个文件不被过滤
*.zip             过滤所有.zip文件
/bcd/do.c         过滤某个具体文件
```

对于已经tracted的文件，需要将其从暂存区的索引清除之后，再添加到.gitignore才能让ignore生效：
```Shell
git rm -r --cached .
# 添加需要过滤的文件名到.gitignore
git add .
git commit -m "msg"
git push
```

## 编码

* 中文文件名
  ```Shell
  git config --global core.quotepath false
  ```

## 参考资料

* [聊下git merge --squash](https://www.cnblogs.com/wangiqngpei557/p/6026007.html)
* [icdiff](https://github.com/jeffkaufman/icdiff)

