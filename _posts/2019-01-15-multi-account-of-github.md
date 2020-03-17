---
layout:     post
title:      在同一个电脑上配置多个GitHub账号
date:       2019-01-15 21:02:05 +0800
postId:     2019-01-15-21-02-05
categories: []
tags:       [其他]
geneMenu:   true
excerpt:    在同一个电脑上配置多个GitHub账号
---

最近在使用github的时候,有这样的一个需求,就是一台电脑上登录两个github账号,并上传项目和更新自己的代码,大家都知道需要给该账号添加一个SSH key才能访问，参考： 具体设置 。当然如果你在多台机器使用一个账户，你可以为该账户添加多个SSH key。由于github是使用SSH key的fingerprint来判定你是哪个账户，而不是通过用户名，这样你就可以在设置完之后，在本地直接执行下面的语句，它就会自动使用你的.ssh/id_rsa.pub所对应的账户进行登陆，然后执行相关命令。

```bash
# 本地建库
git init
git commit -am "first commit" 
# push到github上去
git remote add origin git@github.com:xxxx/test.git
git push origin master
```
但是如果你想在一台机器使用两个github账号（比如私人账号和工作用账号）。这个时候怎么指定push到哪个账号的test仓库上去呢？假设你已经拥有私有账号且已经OK，现在想使用另一个工作用账号：

## 为工作账号生成SSH Key

存储key的时候，不要覆盖现有的id_rsa，使用一个新的名字，比如id_rsa_work：

```bash
$ ssh-keygen -t rsa -C "your-email-address"  -f /c/Users/Administrator/.ssh/id_rsa_work
```

把该key加到ssh agent上。由于不是使用默认的.ssh/id_rsa，所以你需要显示告诉ssh agent你的新key的位置

```bash
ssh-add ~/.ssh/id_rsa_work
```
可以通过ssh-add -l来确认结果
 
## 把id_rsa_work.pub加到你的work账号上

`id_rsa_work.pub` 的内容添加到GitHub配置中

## 配置.ssh/config

使用命令行编辑文件：

```bash
sudo vim .ssh/config
```

添加如下内容：

```yaml 
Host github.com
  HostName github.com
  IdentityFile ~/.ssh/id_rsa
 
Host github_work
  HostName github.com
  IdentityFile ~/.ssh/id_rsa_work
```

## 配置项目路径

按照上面的文件中的Host名字，更改需要使用 `id_rsa_work` 秘钥的项目路径：

```bash
git remote rm origin
git remote add origin git@github_work:adolphor/test.git
git remote -v
```

## 配置完成

这样的话，你就可以通过使用github.com别名github_work来明确说你要是使用id_rsa_work的SSH key来连接github，即使用工作账号进行操作。

```bash
# 本地建库
git init
git commit -am "first commit"
# push到github上去
git remote add origin git@github_work:xxxx/test.git
git push origin master
```

## 参考资料

* [如何在同一台电脑上使用两个github账户](https://blog.csdn.net/wolfking0608/article/details/78512171)