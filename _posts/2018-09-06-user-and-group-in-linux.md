---
layout:     post
title:      Linux 用户和组管理
date:       2018-09-06 09:31:07 +0800
postId:     2018-09-06-09-31-08
categories: [blog]
tags:       [Linux]
geneMenu:   true
excerpt:    Linux 用户管理
---

## 创建用户

基本指令：
```bash
sudo useradd [options] <username>
```

### 创建没有登录权限的用户

创建一个用户名为olivia的用户，但此用户没有自己的home目录，且不能登录：
```bash
sudo useradd olivia
```

```bash
sudo useradd -M olivia 
​sudo usermod -L olivia
```

### 创建有登录权限的用户

创建一个用户名为olivia的用户，此用户有自己的home目录，并设置登录密码：
```bash
# 创建用户
sudo useradd -m olivia
# 设置密码
sudo passwd olivia

# 将两条指令一起执行
sudo useradd -m olivia -p <password>
```


### 用户切换

```bash
# 普通用户切到root用户
su

# root用户切到普通用户
su - <username>
```

## 删除用户

```bash
userdel  [-r]  <username>
```

## 用户组

```bash
# 查看用户组下所有用户
grep <groupname> /etc/group
```


## 参考资料

* [How to create users and groups in Linux from the command line](https://www.techrepublic.com/article/how-to-create-users-and-groups-in-linux-from-the-command-line/)
* [Linux: Add User to Group](https://www.hostingadvice.com/how-to/linux-add-user-to-group/)