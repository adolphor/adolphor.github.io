---
layout:     post
title:      Linux 排查进程和线程
date:       2021-09-28 09:45:02 +0800
postId:     2021-09-28-09-45-02
categories: [Linux]
keywords:   [Linux]
---
服务器运行过程中，可能有些程序运行异常，就需要通过排查手段找到运行异常的应用，分析异常原因，
来解决相关问题。

## top
直接使用 `top` 命令显示系统和内存信息，`Q` 或者 `ctrl + C` 退出。

```
$ top
top - 10:06:14 up 123 days, 20:13,  2 users,  load average: 0.04, 0.08, 0.13
Tasks: 112 total,   1 running, 109 sleeping,   0 stopped,   2 zombie
%Cpu(s):  0.7 us,  0.5 sy,  0.0 ni, 98.8 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
KiB Mem :  8009180 total,   555876 free,  1975280 used,  5478024 buff/cache
KiB Swap:        0 total,        0 free,        0 used.  5303448 avail Mem

  PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND
13099 root      10 -10  139532  24036  10076 S   1.3  0.3 877:07.80 AliYunDun
19343 jyapp     20   0 3515812   1.1g  16824 S   1.0 15.0   2480:11 java
32577 jyapp     20   0 3411248 184464  12488 S   0.3  2.3 141:00.46 java
    1 root      20   0   49732   3876   2492 S   0.0  0.0  68:37.47 systemd
    2 root      20   0       0      0      0 S   0.0  0.0   0:02.02 kthreadd
    3 root      20   0       0      0      0 S   0.0  0.0   1:35.58 ksoftirqd/0
    5 root       0 -20       0      0      0 S   0.0  0.0   0:00.00 kworker/0:0H
    7 root      rt   0       0      0      0 S   0.0  0.0   0:11.75 migration/0
```

## top -Hp pid
直接使用 `top` 查看的是进程信息，可以使用 `-H -p` 参数查看进程里的线程信息：
* **`-H`**：开启查看线程信息
* **`-p`**：指定线程的PID

```shell
top -Hp 19343
```
```
top - 10:08:12 up 123 days, 20:15,  2 users,  load average: 0.07, 0.08, 0.13
Threads: 124 total,   0 running, 124 sleeping,   0 stopped,   0 zombie
%Cpu(s):  2.2 us,  1.2 sy,  0.0 ni, 96.5 id,  0.2 wa,  0.0 hi,  0.0 si,  0.0 st
KiB Mem :  8009180 total,   555568 free,  1975112 used,  5478500 buff/cache
KiB Swap:        0 total,        0 free,        0 used.  5303632 avail Mem

  PID USER      PR  NI    VIRT    RES    SHR S %CPU %MEM     TIME+ COMMAND
19366 jyapp     20   0 3515812   1.1g  16824 S  0.3 15.0  53:38.62 java
19397 jyapp     20   0 3515812   1.1g  16824 S  0.3 15.0  52:18.84 java
19517 jyapp     20   0 3515812   1.1g  16824 S  0.3 15.0   4:16.15 java
16965 jyapp     20   0 3515812   1.1g  16824 S  0.3 15.0 112:02.65 java
19343 jyapp     20   0 3515812   1.1g  16824 S  0.0 15.0   0:00.00 java
19344 jyapp     20   0 3515812   1.1g  16824 S  0.0 15.0   0:03.59 java
19345 jyapp     20   0 3515812   1.1g  16824 S  0.0 15.0   2:03.50 java
```

## PID对应项目
通过ps及top命令查看进程信息时，只能查到相对路径，查不到的进程的详细信息，如绝对路径等。
有时候只根据进程信息和启动的应用名称无法区分到底是哪一个应用，比如启动了多个tomcat或者
spring boot 项目的时候，显示的名称都一样，那么需要通过以下的方法来查看进程的详细信息：

Linux在启动一个进程时，系统会在/proc下创建一个以PID命名的文件夹，在该文件夹下会有我们的进程的信息，
其中包括一个名为exe的文件即记录了绝对路径，通过ll或ls –l命令即可查看：
```shell
ll /proc/19343
```
```
lrwxrwxrwx   1 jyapp jyapp 0 Sep 24 00:32 cwd -> /home/jyapp/app/apache-tomcat-rms/bin
lrwxrwxrwx   1 jyapp jyapp 0 Sep 23 23:55 exe -> /home/jyapp/app/jdk1.7.0_76/bin/java
-r--r--r--   1 jyapp jyapp 0 Sep 23 23:55 cmdline
-r--------   1 jyapp jyapp 0 Sep 24 00:32 environ
dr-x------   2 jyapp jyapp 0 Sep  7 11:27 fd
……
```
* **`cwd`**：符号链接的是进程运行目录；
* **`exe`**：符号连接就是执行程序的绝对路径；
* **`cmdline`**：就是程序运行时输入的命令行命令；
* **`environ`**：记录了进程运行时的环境变量；
* **`fd`**：目录下是进程打开或使用的文件的符号连接。


## netstat命令
```shell
netstat -an | grep <port>
```

范例：

```shell
$ netstat -an | grep 1087
tcp4       0      0  *.1087                 *.*                    LISTEN    
```

## lsof命令

通过`list open file`命令可以查看到当前打开文件，在linux中所有事物都是以文件形式存在，包括网络连接及硬件设备。
-i参数表示网络链接，:80指明端口号，该命令会同时列出PID，方便kill。

```shell
lsof -i:<port>
```

查看所有进程监听的端口（耗时很长，慎用）：
```shell
sudo lsof -i -P | grep -i "listen"
```

范例：
```shell
$ lsof -i:1087
COMMAND   PID      USER   FD   TYPE              DEVICE  SIZE/OFF  NODE  NAME
privoxy  1052  adolphor    3u  IPv4  0xf5bc06c5ea3112fd       0t0   TCP  *:cplscrambler-in (LISTEN)
```

## java应用
如果是Java应用，那么可以使用JDK自带的命令行工具来查找相应的PID：
* jps
* jcmd：从1.7开始

两个命令显示的内容稍有不同：

| pid   | jps 指令内容 | jcmd 指令内容                                 |
| ----- | ------------ | ------------------------------------------- |
| 23106 | Jps          | sun.tools.jcmd.JCmd                         |
| 19343 | Bootstrap    | org.apache.catalina.startup.Bootstrap start |
| 32577 | Bootstrap    | org.apache.catalina.startup.Bootstrap start |
| 14029 | Bootstrap    | org.apache.catalina.startup.Bootstrap start |

在获取PID之后，同样，使用 `ll /proc/PID` 查看项目信息，以及后续使用 `jstack` 等指令
分析应用相关线程运行情况。

更新：
**jps** 增加 **-lm** 参数之后，等价于 **jcmd**：
```shell
jps -lm
```
```
29587 sun.tools.jps.Jps -lm
19343 org.apache.catalina.startup.Bootstrap start
32577 org.apache.catalina.startup.Bootstrap start
14029 org.apache.catalina.startup.Bootstrap start
```

## 参考资料
* [Linux 排查进程和线程]({% post_url system/linux/2021-09-28-01-linux-progress-thread %})
* [JVM - jstack工具]({% post_url java/jvm/tools/2021-07-29-01-jvm-jstack %})
* [JVM - jps工具]({% post_url java/jvm/tools/2021-09-29-01-jvm-jps %})
* [Linux通过PID查看进程完整信息](https://blog.csdn.net/Great_Smile/article/details/50114133)
* [使用jcmd进行JVM性能和内存跟踪微调](https://www.jdon.com/54887)
