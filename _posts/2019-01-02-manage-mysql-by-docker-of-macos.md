---
layout:     post
title:      在macOS上使用docker运行MySQL
date:       2019-01-02 14:50:38 +0800
postId:     2019-01-02-14-50-38
categories: [article]
tags:       [Docker]
geneMenu:   true
excerpt:    在macOS上使用docker运行MySQL
---

搜索镜像
```bash
docker search msyql
```

使用之前配置的脚本文件，查看镜像版本
```bash
dockertags mysql
```

下载镜像
```bash
docker pull mysql:5.7
```

创建mysql容器
```bash
sudo docker run --name mysql -p 3301:3306 -e MYSQL_ROOT_PASSWORD=admin -d mysql:5.7
```
需要具体解释一下：
* -p 3301:3306：将容器的3306端口映射到主机的3306端口
* -e 指定环境变量，其中MYSQL_ROOT_PASSWORD=root即初始化root用户的密码为root 
* -name 即命名容器的名称为mysql 
* -d 后台运行容器，并返回容器ID 
* mysql:5.7 最后的mysql表明使用最新的mysql镜像，如果要指定版本，和pull命令一样，冒号:版本号。 
* 上面的命令如果本地不存在mysql镜像的话，也会先pull操作的。 

这样就启动了一个运行mysql的docker容器。
这里再重点解释一下端口映射：

-P 可以指定要映射的IP和端口，但是在一个指定端口上只可以绑定一个容器。支持的格式有 hostPort:containerPort、ip:hostPort:containerPort、 ip::containerPort

* `hostPort:containerPort`（映射所有接口地址）

将本地的 5000 端口映射到容器的 5000 端口，可以执行如下命令：
`$ sudo docker run -d -p 5000:5000 training/webapp python app.py` 此时默认会绑定本地所有接口上的所有地址。


* `ip:hostPort:containerPort` （映射指定地址的指定端口）
指定映射使用一个特定地址，比如 localhost 地址 127.0.0.1
`$ sudo docker run -d -p 127.0.0.1:5000:5000 training/webapp python app.py`
 
* `ip::containerPort` （映射指定地址的任意端口）
绑定 localhost 的任意端口到容器的 5000 端口，本地主机会自动分配一个端口。
`sudo docker run -d -p 127.0.0.1::5000 training/webapp python app.py`
还可以使用 udp 标记来指定 udp 端口
`$ sudo docker run -d -p 127.0.0.1:5000:5000/udp training/webapp python app.py`

查看映射端口配置
使用 docker port 来查看当前映射的端口配置，也可以查看到绑定的地址
`docker port nostalgic_morse 5000`
127.0.0.1:49155


使用inspect命令，加上容器名称或者id即可，可以看到IP等信息：
```bash
sudo docker inspect mysql
```

因为-P指定的时候没有指定监听host，所以直接使用localhost就可以进行连接：
```log
jdbc:mysql://localhost:3301
root/admin
```



## 参考资料

* [外部访问docker容器(docker run -p/-P 指令)](https://www.jianshu.com/p/2b424c3bf0f7)
* [Docker mysql 把数据存储在本地目录](https://blog.csdn.net/ataoajuan/article/details/78646581)