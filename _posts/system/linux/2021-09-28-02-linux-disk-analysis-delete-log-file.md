---
layout:     post
title:      Linux 磁盘空间分析和删除日志文件
date:       2021-09-28 11:07:36 +0800
postId:     2021-09-28-11-07-36
categories: [Linux]
keywords:   [Linux]
---

编写本文原由的是测试服务器中部署的Tomcat应用请求异常，既没有500错误，也没有404错误，单纯的没有返回结果，
登录测试服务器之后，提示磁盘空间已满，**`cannot create temp file for here-document: No space left on device`**
下面记录排查和解决的主要过程。

## df 命令
df命令用于显示磁盘分区上的可使用的磁盘空间。默认显示单位为KB。可以利用该命令来获取硬盘被占用了多少空间，
目前还剩下多少空间等信息。

* **-a** 或 **-\-all**：包含全部的文件系统；
* **-\-block-size=**<区块大小>：以指定的区块大小来显示区块数目；
* **-h** 或 **-\-human-readable**：以可读性较高的方式来显示信息；
* **-H** 或 **-\-si**：与-h参数相同，但在计算时是以1000 Bytes为换算单位而非1024 Bytes；
* **-i** 或 **-\-inodes**：显示inode的信息；
* **-k** 或 **-\-kilobytes**：指定区块大小为1024字节；
* **-l** 或 **-\-local**：仅显示本地端的文件系统；
* **-m** 或 **-\-megabytes**：指定区块大小为1048576字节；
* **-\-no-sync**：在取得磁盘使用信息前，不要执行sync指令，此为预设值；
* **-P** 或 **-\-portability**：使用POSIX的输出格式；
* **-\-sync**：在取得磁盘使用信息前，先执行sync指令；
* **-t**\<文件系统类型> 或 **-\-type=**\<文件系统类型>：仅显示指定文件系统类型的磁盘信息；
* **-T** 或 **-\-print-type**：显示文件系统的类型；
* **-x**\<文件系统类型> 或 **-\-exclude-type=**\<文件系统类型>：不要显示指定文件系统类型的磁盘信息；
* **-\-help**：显示帮助；
* **-\-version**：显示版本信息。

## du 命令
du命令也是查看使用空间的，但是与df命令不同的是du命令是对文件和目录磁盘使用的空间的查看，还是和df命令有一些区别的。

* **-a** 或 **-all** 显示目录中个别文件的大小。
* **-b** 或 **-bytes** 显示目录或文件大小时，以byte为单位。
* **-c** 或 **-\-total** 除了显示个别目录或文件的大小外，同时也显示所有目录或文件的总和。
* **-k** 或 **-\-kilobytes** 以KB(1024bytes)为单位输出。
* **-m** 或 **-\-megabytes** 以MB为单位输出。
* **-s** 或 **-\-summarize** 仅显示总计，只列出最后加总的值。
* **-h** 或 **-\-human-readable** 以K，M，G为单位，提高信息的可读性。
* **-x** 或 **-\-one-file-xystem** 以一开始处理时的文件系统为准，若遇上其它不同的文件系统目录则略过。
* **-L**\<符号链接> 或 **-\-dereference**\<符号链接> 显示选项中所指定符号链接的源文件大小。
* **-S** 或 **-\-separate-dirs** 显示个别目录的大小时，并不含其子目录的大小。
* **-X**\<文件> 或 **-\-exclude-from\=**\<文件> 在<文件>指定目录或文件。
* **-\-exclude=**<目录或文件> 略过指定的目录或文件。
* **-D** 或 **-\-dereference-args** 显示指定符号链接的源文件大小。
* **-H** 或 **-\-si** 与`-h`参数相同，但是K，M，G是以1000为换算单位。
* **-l** 或 **-\-count-links** 重复计算硬件链接的文件。

## 查看磁盘占用情况
直接使用 **`df -h`** 命令查看整个磁盘的占用情况，会显示类似如下信息：
```
$ df -h
Filesystem      Size  Used Avail Use% Mounted on
/dev/vda1        40G   27G   11G  72% /
devtmpfs        7.8G     0  7.8G   0% /dev
tmpfs           7.8G     0  7.8G   0% /dev/shm
tmpfs           7.8G  769M  7.1G  10% /run
tmpfs           7.8G     0  7.8G   0% /sys/fs/cgroup
tmpfs           1.6G     0  1.6G   0% /run/user/1000
tmpfs           1.6G     0  1.6G   0% /run/user/0
```

## 特定文件夹下占用情况分析
从上面的输出信息可以看出整个磁盘的空间占用情况，如果已经达到100%，就需要删除无用文件来腾出磁盘空间。
可以使用 **`du -sh /*`** 命令来查看具体是哪个目录占用的空间比较多：
* -s：
* -h

```
$ sudo du -sh /*
108M    /boot
23G     /home
16K     /lost+found
183M    /root
0       /sys
……
```

可以看出 **`/home`** 占用空间比较多，可以再次执行此命令，一直递归查询出来最终占用的文件：
```
$ sudo du -sh /home/adolphor/logs/*
795M    /home/adolphor/logs/adminsso
61M     /home/adolphor/logs/iaweb
300K    /home/adolphor/logs/mosquitto1.log
9.8G    /home/adolphor/logs/mosquitto2.log
92K     /home/adolphor/logs/mosquitto3.log
343M    /home/adolphor/logs/mqttserver
83M     /home/adolphor/logs/mqttserver-1890
137M    /home/adolphor/logs/ons.log
```

## 删除后空间未释放
通过上面的分析查找，定位到最大的日志文件是 `/home/adolphor/logs/mosquitto1.log`，
有 9.8G 大小，通过 `rm -rf mosquitto1.log` 删除以及 `touch mosquitto1.log`
重建 两个指令之后，发现磁盘空间并未释放，原因在于如果一个文件当前正在被使用，那么删除的时候
并不会立即释放磁盘空间，那么可以使用 **`lsof | grep deleted`** 指令进行验证：
```
$ lsof | grep deleted
java 24843 24867 adolphor 82w REG 253,1 11920164 1052113 /home/adolphor/logs/ons.log.10 (deleted)
java 24843 24867 adolphor 84w REG 253,1 59732775 1052162 /home/adolphor/logs/ons.log.10 (deleted)
java 24843 24868 adolphor 82w REG 253,1 11920164 1052113 /home/adolphor/logs/ons.log.10 (deleted)
……
```

解决办法就是，重新启动应用，让应用的日志信息，写入到新创建的那个日志文件中。

## 参考资料
* [Linux 磁盘空间分析和删除日志文件]({% post_url system/linux/2021-09-28-02-linux-disk-analysis-delete-log-file %})
* [Linux命令大全 - du命令](https://man.linuxde.net/du)
