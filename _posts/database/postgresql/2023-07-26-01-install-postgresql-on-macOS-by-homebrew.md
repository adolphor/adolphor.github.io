---
layout:     post
title:      在macOS上使用Homebrew安装PostgreSQL
date:       2023-07-26 08:44:23 +0800
postId:     2023-07-26-08-44-23
categories: [PostgreSQL]
keywords:   [database, PostgreSQL, macOS]
---
## 安装步骤

```shell
# 搜索应用和版本
brew search postgresql
# 安装当前最新版15
brew install postgresql@15
# 配置环境变量，以便使用命令行工具
echo 'export PATH="/opt/homebrew/opt/postgresql@15/bin:$PATH"' >> ~/.bash_profile
# 刷新使配置生效
source ~/.bash_profile
# 启动运行
brew services start postgresql@15
```

## 初始化
### 默认账号
安装完成之后，会自动创建一个当前用户名的超级管理员用户，并且自动创建一个名为 `postgres` 的数据库，以及一个名为 `public` 的 schema。
可以使用数据库GUI工具如Datagrip直接使用用户名登陆，不需要输入密码。

### 创建用户和数据库
```shell
# 命令行登陆 postgres 数据库
$ psql postgres
# 创建新用户
CREATE USER sonar;
# 创建新数据库
CREATE DATABASE sonar_db WITH OWNER sonar;
# 修改密码：sonar用户的密码为sonar1234
\password sonar
```

## 参考资料
* [在macOS上使用Homebrew安装PostgreSQL]({% post_url database/postgresql/2023-07-26-01-install-postgresql-on-macOS-by-homebrew %})

```
![image-alter]({{ site.baseurl }}/image/post/2023/07/26/01/xxx.png)
```
