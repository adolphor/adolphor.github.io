---
layout:     post
title:      Linux系统shell脚本基础
date:       2022-12-01 11:11:58 +0800
postId:     2022-12-01-11-11-58
categories: [Linux]
keywords:   [Linux]
---

## 脚本基础
最简单的脚本范例：

> first.sh

```shell
#!/bin/bash
#---------------
#@file:first.sh
#---------------
echo "I'm in first file"
```
* `#!/bin/bash`：指定shell客户端
* `echo`：打印输出
* `#`：注解

## 脚本权限
推荐配置：所有用户都有读和执行权限，如果权限不对，需要使用如下指令修正：
```shell
chmod a+rw first.sh
# 或者
chmod 755 first.sh
```
参考：[Linux系统权限控制基础]({% post_url system/linux/2022-12-01-02-linux-system-permission %})

## 脚本调用
脚本调用有三种方式：
* source
* 点号 `.`
* `sh` 命令

### source


```shell
#!/bin/bash
#---------------
#@file:second.sh
#---------------
echo "I'm in second file"
source first.sh
```

### 点号 `.`
```shell
#!/bin/bash
echo "I'm in second file"
. first.sh
```

### `sh` 命令
```shell
#!/bin/bash
echo "I'm in second file"
sh  first.sh
```

### 三者区别
以上三种方式的输出结果都是：
```
I'm in second file
I'm in first file
```

使用 source 命令和点号是等价的，类似于 C/C++ 中的 #include 预处理指令，都是将指定的脚本内容拷贝至当前的脚本中，
由一个 Shell 进程来执行。使用sh命令来调用另外的脚本和前面两种方法有着本质的区别。使用sh命令则会开启新的Shell进程
来执行指定的脚本，这样的话，父进程中的变量在子进程中就无法访问。参考如下代码：

first.sh 内容如下，访问了 second.sh 中的变量 second。

```shell
#!/bin/bash
echo "I'm in first file"
echo "second:" $second
```
second.sh内容，通过上面介绍的三种方法来调用first.sh，看看对second.sh的变量second访问有什么区别：
```shell
#!/bin/bash
second=lvlv
echo "I'm in second file"
source first
. first
sh first
```
程序的输出结果是：
```
I'm in second file
I'm in first file
second: lvlv
I'm in first file
second: lvlv
I'm in first file
second:
```
可见，使用sh命令开启一个子进程来调用指定的shell脚本无法访问父进程的变量。我们如何让子进程访问父进程中变量呢？可以使用 export 命令。

## cron定时任务调用脚本
使用cron调用定时任务，范例如下：
```shell
*/1 * * * * sh /mnt/runSys/monitor.sh >> /mnt/runSys/monitor.log
```
如果 `monitor.sh` 应用了其他脚本，比如 `start.sh`，那么需要使用绝对路径：

> parent.sh

```shell
#!/bin/bash
echo 'running parent.sh'
source /home/temp/content.sh
```

> parent.sh

```shell
#!/bin/bash
echo 'running content.sh'
```

## 参考资料
* [Linux系统shell脚本基础]({% post_url system/linux/2022-12-01-01-linux-system-shell %})
* [Shell 脚本调用另一个脚本的三种方法](https://blog.csdn.net/k346k346/article/details/86751705)

```
![image-alter]({{ site.baseurl }}/image/post/2022/12/01/01/xxx.jpg)
```
