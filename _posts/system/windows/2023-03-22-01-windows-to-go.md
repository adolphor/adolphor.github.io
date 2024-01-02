---
layout:     post
title:      Win To Go - 移动的Windows系统
date:       2023-03-22 20:59:50 +0800
postId:     2023-03-22-20-59-50
categories: [Windows]
keywords:   [Windows]
---

macOS想要使用Windows系统，可以使用 启动转换助手 安装双系统，为了不实用内置硬盘空间，考虑使用外接移动硬盘的方式安装Windows系统，
也就是windows to go。

在Windows10系统中，自带的Windows To Go 尝试安装了一次，安装成功之后无法启动运行，蓝屏之后自动关机，下面使用第三方安装工具的方式进行安装。

## 准备系统
* windows系统：可以是物理机也可以是虚拟机
* macOS系统：下载支持软件以及提取ISO镜像内容

## 软件下载
* windows景象：解压iso提取出来 `sources/install.wim`
* Windows To Go 辅助工具 / WTG辅助工具：v5.6.1
* macOS下载 `WindowsSupport`：启动转换助理 - 操作 - 下载Windows支持软件

## 安装
* 先要将 Windows支持软件 `$WinPEDriver$` 目录中的驱动，拷贝到 WTG辅助工具 的驱动文件夹下
* 打开WTG辅助工具，选择目标磁盘，选择 `install.wim` 等待安装完成
* 安装完成后，还需要运行Windows支持软件 BootCamp 中的 Setup.exe，相关驱动安装完成，就可以使用了



## 参考资料
* [Win To Go - 移动的Windows系统]({% post_url system/windows/2023-03-22-01-windows-to-go %})
