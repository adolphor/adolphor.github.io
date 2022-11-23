---
layout:     post
title:      Linux系统中自动检测并重启Java项目
date:       2022-11-23 09:58:05 +0800
postId:     2022-11-23-09-58-05
categories: [linux]
keywords:   [Java, Linux]
---

后台运行的Java项目，会遇到莫名的原因导致后台运行的Java项目会停止运行，为了避免长时间宕机的问题，
可以使用Linux的定时任务自动检测进程信息，如果未运行自动重启。

## 基础脚本
```shell
#! /bin/sh
# 检测Java项目是否运行
ps -ef | grep java
# 剔除检测本身的进程
ps -ef | grep java | grep -v grep
# 统计进程数量
ps -ef | grep java | grep -v grep | wc -l

# 变量赋值
procnum=`ps -ef | grep java | grep -v grep | wc -l`

# 条件判断
if [ $procnum -eq 0 ]
then
    echo `date +%Y-%m-%d` `date +%H:%M:%S` "restart service" >> /root/logs/restart.log
    ./start.sh
fi
```

## 完整脚本

> monitor.sh
```shell
#! /bin/sh
procnum=`ps -ef | grep "play.server.Server" | grep -v grep | wc -l`
#echo `date +%Y-%m-%d` `date +%H:%M:%S` "procnum=$procnum" >> /mnt/runSys/monitor.log
if [ $procnum -eq 0 ]
then
    # 如果进程异常退出，可能需要删除pid文件才能重启成功
    rm -rf /mnt/runSys/jy_cms/server.pid
    echo `date +%Y-%m-%d` `date +%H:%M:%S` "restart service" >> /mnt/runSys/monitor.log
    ./start.sh
fi
```

## 将monitor.sh脚本加入crontab
```shell
touch /mnt/runSys/monitor.log
crontab -e
*/1 * * * * sh /mnt/runSys/monitor.sh >> /mnt/runSys/monitor.log
```

## 参考资料
* [Linux系统中自动检测并重启Java项目]({% post_url system/linux/2022-11-23-01-linux-restart-java-project %})
