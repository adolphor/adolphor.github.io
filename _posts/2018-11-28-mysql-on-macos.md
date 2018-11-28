---
layout:     post
title:      macOS上使用MySQL的那些事
date:       2018-11-28 19:45:19 +0800
postId:     2018-11-28-19-45-19
categories: [blog]
tags:       [database,MySQL]
geneMenu:   true
excerpt:    MySQL那些事
---


## macOS上安装 MySQL 5.7

```bash
brew search mysql@5.7
brew install mysql@5.7

brew info mysql@5.7
```
```log
mysql@5.7: stable 5.7.24 (bottled) [keg-only]

We've installed your MySQL database without a root password. To secure it run:
    mysql_secure_installation

MySQL is configured to only allow connections from localhost by default

To connect run:
    mysql -uroot

mysql@5.7 is keg-only, which means it was not symlinked into /usr/local,
because this is an alternate version of another formula.

If you need to have mysql@5.7 first in your PATH run:
  echo 'export PATH="/usr/local/opt/mysql@5.7/bin:$PATH"' >> ~/.zshrc

For compilers to find mysql@5.7 you may need to set:
  export LDFLAGS="-L/usr/local/opt/mysql@5.7/lib"
  export CPPFLAGS="-I/usr/local/opt/mysql@5.7/include"

For pkg-config to find mysql@5.7 you may need to set:
  export PKG_CONFIG_PATH="/usr/local/opt/mysql@5.7/lib/pkgconfig"


To have launchd start mysql@5.7 now and restart at login:
  brew services start mysql@5.7
Or, if you don't want/need a background service you can just run:
  /usr/local/opt/mysql@5.7/bin/mysql.server start
==> Analytics
install: 21,477 (30 days), 62,061 (90 days), 114,872 (365 days)
install_on_request: 21,422 (30 days), 61,918 (90 days), 114,651 (365 days)
build_error: 0 (30 days)
```

### 配置文件
可以看到mysql的配置文件为：
```
Default options are read from the following files in the given order:
/etc/my.cnf /etc/mysql/my.cnf /usr/local/etc/my.cnf ~/.my.cnf
```
只有`/usr/local/etc/my.cnf`存在，修改绑定地址开放其他电脑访问权限：
```
vim /usr/local/etc/my.cnf
    bind-address = 0.0.0.0
```

### 密码安全规则

如果出现`ERROR 1819 (HY000): Your password does not satisfy the current policy requirements`的提示，说明密码安全规则较高，
不能设置太简单的密码，如果想要修改规则，进行如下操作：
```
# 查看所有规则
SHOW VARIABLES LIKE 'validate_password%';
# 查看密码安全等级
select @@validate_password_policy;
# 修改安全等级
set global validate_password_policy=0;
# 查看密码长度限制
select @@validate_password_length;
# 修改密码长度限制
set global validate_password_length=1;
```

### 重启

```
brew services restart mysql@5.7

brew services start mysql@5.7
brew services stop mysql@5.7
```

## 命令行基本操作

### 登录

* -u 跟上用户名
* -p 跟上密码
* 最后可以加上数据库名称也可以不加

```bash
mysql -uroot -phaizhu
mysql -uroot -phaizhu testdb
```

## 其他操作

### 修改 ROOT 密码

如果出现以下错误，可以通过修改密码来解决：
```log
ERROR 1045 (28000): Access denied for user 'root'@'localhost'
```

具体步骤：

```bash
# 停止mysql服务
brew services stop mysql@5.7
# 检查并kill所有相关进程
ps -ef | grep mysql
kill -9 xxx
# 安全模式启动
mysqld_safe --skip-grant-tables &
# 进入mysql交互模式
mysql

use mysql
flush privileges;
ALTER USER 'root'@'localhost' IDENTIFIED BY 'adolphor';
quit
```





