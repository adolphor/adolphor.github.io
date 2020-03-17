---
layout:     post
title:      Linux 用户和组管理
date:       2018-09-06 09:31:07 +0800
postId:     2018-09-06-09-31-08
categories: [article]
tags:       [Linux]
geneMenu:   true
excerpt:    Linux 用户管理
---

## 用户管理

### 用户的新增

使用范例：
```bash
# 创建新用户：-m 创建home目录；-U 创建同名用户组；-s 指定shell类型不然登录之后无法使用命令行
useradd -m -U -s /bin/bash my_java
# 修改密码 haizhu@1314.aly.java
passwd my_java
# 增加用户组，不然无法使用sudo权限
usermod -aG sudo,adm my_java
# 汇总 (只有确认需要使用sudo切换的时候才使用-aG，)
useradd -m -U -aG sudo,adm -s /bin/bash -p password my_java
```

```bash
useradd --help
```
```
Usage: useradd [options] LOGIN
       useradd -D
       useradd -D [options]

Options:
  -b, --base-dir BASE_DIR       base directory for the home directory of the
                                new account
  -c, --comment COMMENT         GECOS field of the new account
  -d, --home-dir HOME_DIR       home directory of the new account
  -D, --defaults                print or change default useradd configuration
  -e, --expiredate EXPIRE_DATE  expiration date of the new account
  -f, --inactive INACTIVE       password inactivity period of the new account
  -g, --gid GROUP               name or ID of the primary group of the new
                                account
  -G, --groups GROUPS           list of supplementary groups of the new
                                account
  -h, --help                    display this help message and exit
  -k, --skel SKEL_DIR           use this alternative skeleton directory
  -K, --key KEY=VALUE           override /etc/login.defs defaults
  -l, --no-log-init             do not add the user to the lastlog and
                                faillog databases
  -m, --create-home             create the user's home directory
  -M, --no-create-home          do not create the user's home directory
  -N, --no-user-group           do not create a group with the same name as
                                the user
  -o, --non-unique              allow to create users with duplicate
                                (non-unique) UID
  -p, --password PASSWORD       encrypted password of the new account
  -r, --system                  create a system account
  -R, --root CHROOT_DIR         directory to chroot into
  -s, --shell SHELL             login shell of the new account
  -u, --uid UID                 user ID of the new account
  -U, --user-group              create a group with the same name as the user
  -Z, --selinux-user SEUSER     use a specific SEUSER for the SELinux user mapping
      --extrausers              Use the extra users database
```

```bash
sudo useradd [options] <username>
# 创建一个用户名为olivia的用户，但此用户没有自己的home目录，且不能登录：
sudo useradd olivia
# 创建一个用户名为olivia的用户，此用户有自己的home目录，并设置登录密码：
sudo useradd -m olivia
# 设置密码
sudo passwd olivia
```

### 用户的修改

```bash
usermod --help
```
```
Usage: usermod [options] LOGIN

Options:
  -c, --comment COMMENT         new value of the GECOS field
  -d, --home HOME_DIR           new home directory for the user account
  -e, --expiredate EXPIRE_DATE  set account expiration date to EXPIRE_DATE
  -f, --inactive INACTIVE       set password inactive after expiration
                                to INACTIVE
  -g, --gid GROUP               force use GROUP as new primary group
  -G, --groups GROUPS           new list of supplementary GROUPS
  -a, --append                  append the user to the supplemental GROUPS
                                mentioned by the -G option without removing
                                him/her from other groups
  -h, --help                    display this help message and exit
  -l, --login NEW_LOGIN         new value of the login name
  -L, --lock                    lock the user account
  -m, --move-home               move contents of the home directory to the
                                new location (use only with -d)
  -o, --non-unique              allow using duplicate (non-unique) UID
  -p, --password PASSWORD       use encrypted password for the new password
  -R, --root CHROOT_DIR         directory to chroot into
  -s, --shell SHELL             new login shell for the user account
  -u, --uid UID                 new UID for the user account
  -U, --unlock                  unlock the user account
  -v, --add-subuids FIRST-LAST  add range of subordinate uids
  -V, --del-subuids FIRST-LAST  remove range of subordinate uids
  -w, --add-subgids FIRST-LAST  add range of subordinate gids
  -W, --del-subgids FIRST-LAST  remove range of subordinate gids
  -Z, --selinux-user SEUSER     new SELinux user mapping for the user account
```

### 用户的删除

使用范例：
```bash
userdel -r username
```

```bash
userdel --help
```
```
Usage: userdel [options] LOGIN

Options:
  -f, --force                   force removal of files,
                                even if not owned by user
  -h, --help                    display this help message and exit
  -r, --remove                  remove home directory and mail spool
  -R, --root CHROOT_DIR         directory to chroot into
      --extrausers              Use the extra users database
  -Z, --selinux-user            remove any SELinux user mapping for the user
```

### 用户切换

```bash
# 普通用户切到root用户
su -
# root用户切到普通用户
su - username
```


### 查看所有用户

```bash
# 可以查看所有用户的列表，但不够直观
cat /etc/passwd 
# 改进之后的查看命令
cat /etc/passwd|grep -v nologin|grep -v halt|grep -v shutdown|awk -F":" '{ print $1"|"$3"|"$4 }'|more
```

## 用户组

### 用户组的新增修改和删除

### 用户组的权限管理

### 用户组下的用户管理

```bash
# 查看当前登录用户的组内成员
groups
# 查看root用户所在的组，以及组内成员
groups root
# 查看当前登录用户名
whoami
# 查看用户组下所有用户
grep <groupname> /etc/group
```


## 参考资料

* [How to create users and groups in Linux from the command line](https://www.techrepublic.com/article/how-to-create-users-and-groups-in-linux-from-the-command-line/)
* [Linux: Add User to Group](https://www.hostingadvice.com/how-to/linux-add-user-to-group/)
* [Linux 用户和用户组管理](https://www.runoob.com/linux/linux-user-manage.html)
* [Ubuntu新建用户并赋予权限清单](https://islishude.github.io/blog/2018/05/20/linux/Ubuntu新建用户并赋予权限清单/)