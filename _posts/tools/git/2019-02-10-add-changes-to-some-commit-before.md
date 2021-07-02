---
layout:     post
title:      Git追加代码更改到之前某次commit
date:       2019-02-10 13:15:13 +0800
postId:     2019-02-10-13-15-14
categories: []
tags:       [Tools, Git]
geneMenu:   true
excerpt:    Git追加代码更改到之前某次commit
---

Git提交原则上保持每个功能点一个commit，每次commit之后保持工作空间干净防止遗漏某些修改。
但如果对于某次commit遗漏了某个文件的修改，应该怎么处理呢？

## 追加到最后一次commit

如果只是单纯的追加到最后一次commit，可以直接commit之后使用rebase进行commit的合并：

```shell
git add .
git commit -m "add to the last commit"
git rebase -i HEAD~3
# change last commit flag to fix, then "Ctrl+C", type ":wq!" and "return"
git push -f origin master
```

## 追加到之前的某次commit

如果需要追加的代码已经间隔了其他的commit，那么追加之后的代码会影响之后的所有commit。可以使用如下方式解决：
* 直接定位到那次commit，然后修改代码并提交
* 先在工作空间修改好并stash，然后定位到那次commit，pop之后进行提交

### 定位后修改

操作参考下面的方法，唯一的区别就是不需要stash和pop操作，只需要定位到那次commit之后，直接修改源码即可。

### stash & pop 追加

#### 操作流程

1. 保存工作空间中的改动 `git stash`
2. 查看commit id：`git log --oneline`
3. 将HEAD移到需要更改的commit上:
   `git rebase f744c32cf74454a74bb2f80e5e38b120cb475af1^ --interactive` 
   找到需要更改的commit, 将行首的pick改成edit, 按esc, 输入 :wq 退出
4. `git stash pop`
5. 使用 `git add` 改动的文件添加改动文件到暂存
6. 使用 `git commit --amend` 追加改动到第一步中指定的commit上
7. 使用 `git rebase --continue` 移动HEAD到最新的commit处，如果这里有冲突, 需要解决:
    * 编辑冲突文件, 解决冲突
    * `git add .`
    * `git commit --amend`
8. 解决冲突之后再执行 `git rebase --continue`
9. 如果之前的commit已经push到远程仓库，那么慎用次更改方法，确定追加的话，使用`git push -f origin master`提交更改

#### 操作实例

```shell
# 将需要追加的文件添加到暂存空间
git add .
# 可以看到修改的文件
git status
```
```log
On branch master
Your branch is up to date with 'origin/master'.

Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

        modified:   mynety-client/src/main/java/com/adolphor/mynety/client/http/HttpInBoundHandler.java
        modified:   mynety-common/src/main/java/com/adolphor/mynety/common/bean/lan/LanMessage.java
```
```shell
git stash
```
```log
Saved working directory and index state WIP on master: eec9853 perf: MITM相关优化
```
```
# 查看那次commit的id并记录：fd7e818
git log --oneline
```
```log
eec9853 (HEAD -> master, origin/master) perf: MITM相关优化
d7b072c perf: 哑代理相关测试和修改，以及版本号升级
fd7e818 refactor: 重构lan通信协议
58f4c24 fix: findbugs warnings
9086e2a test: 增加单元测试用例
```
```shell
# 将HEAD移到需要更改的commit上：找到需要更改的commit, 将行首的pick改成edit, 按esc, 输入:wq退出
git rebase fd7e818^ --interactive
```
```log
Stopped at fd7e818...  refactor: 重构lan通信协议
You can amend the commit now, with

  git commit --amend 

Once you are satisfied with your changes, run

  git rebase --continue
```
```shell
git stash pop
```
```log
interactive rebase in progress; onto 58f4c24
Last command done (1 command done):
   edit fd7e818 refactor: 重构lan通信协议
Next commands to do (2 remaining commands):
   pick d7b072c perf: 哑代理相关测试和修改，以及版本号升级
   pick eec9853 perf: MITM相关优化
  (use "git rebase --edit-todo" to view and edit)
You are currently splitting a commit while rebasing branch 'master' on '58f4c24'.
  (Once your working directory is clean, run "git rebase --continue")

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

        modified:   mynety-client/src/main/java/com/adolphor/mynety/client/http/HttpInBoundHandler.java
        modified:   mynety-common/src/main/java/com/adolphor/mynety/common/bean/lan/LanMessage.java

no changes added to commit (use "git add" and/or "git commit -a")
Dropped refs/stash@{0} (8d044ba7e4138599a365e0691efe5bb25b56bbc7)
```
```shell
# 使用 git add 改动的文件添加改动文件到暂存
git add .
# 使用 git commit --amend 追加改动到第一步中指定的commit上
git commit --amend
# 会弹出commit msg编辑，因为是追加代码不需要改动，所以直接:wq保存即可
```
```log
[detached HEAD c7df87f] refactor: 重构lan通信协议
 Date: Fri Feb 8 18:24:25 2019 +0800
 22 files changed, 587 insertions(+), 270 deletions(-)
 create mode 100644 mynety-common/src/main/java/com/adolphor/mynety/common/constants/LanMsgType.java
 create mode 100644 mynety-common/src/main/java/com/adolphor/mynety/common/utils/BaseUtils.java
 rewrite mynety-common/src/main/java/com/adolphor/mynety/common/utils/ByteStrUtils.java (84%)
 create mode 100644 mynety-common/src/main/java/com/adolphor/mynety/common/utils/ByteUtils.java
 create mode 100644 mynety-common/src/test/java/com/adolphor/mynety/common/utils/BaseUtilsTest.java
```
```shell
# 使用git rebase --continue移动HEAD到最新的commit处
git rebase --continue
# 如果幸运的话，没有冲突的情况下会直接提示成功
```
```log
Successfully rebased and updated refs/heads/master.
```
```shell
# 如果不幸，那么需要解决冲突
    1. 编辑冲突文件, 解决冲突
    2. git add .
    3. git commit --amend
# 解决冲突之后再执行
git rebase --continue
```
```shell
# 查看当前状态
git status
```
```log
On branch master
Your branch and 'origin/master' have diverged,
and have 3 and 3 different commits each, respectively.
  (use "git pull" to merge the remote branch into yours)
```
```shell
# 可以看到追加已经生效，但是因为之前的commit已经push到远程，所以需要force提交当前更改【慎用force，评估风险】
git push -f
```
```log
Enumerating objects: 211, done.
Counting objects: 100% (211/211), done.
Delta compression using up to 4 threads
Compressing objects: 100% (111/111), done.
Writing objects: 100% (152/152), 31.11 KiB | 3.46 MiB/s, done.
Total 152 (delta 59), reused 0 (delta 0)
remote: Resolving deltas: 100% (59/59), completed with 36 local objects.
To adolphor_github:adolphor/mynety.git
 + eec9853...4959937 master -> master (forced update)
```

## 参考资料

* [如何更改某个提交内容/如何把当前改动追加到某次commit上??](https://www.jianshu.com/p/8d666830e826)