---
layout:     post
title:      树莓派系统安装和配置
date:       2019-01-07 22:04:49 +0800
postId:     2019-01-07-22-04-49
categories: [blog]
tags:       [其他]
geneMenu:   true
excerpt:    树莓派系统安装和配置
---

## 系统安装

* 使用`win32diskimager` 安装器，选择image镜像和对应TF或者U盘，然后等待完成即可
* 写入完成之后，需要在boot文件的根目录创建一个名为 `ssh` 且没有后缀的空文件

## 启动wifi
可以使用 `sudo raspi-config`可视化界面来配置。

也可以直接修改相关文件，修改`/etc/wpa_supplicant/wpa_supplicant.conf`文件，增加如下内容：
```
country=CN
network={
	ssid="jysmart"
	psk="jysmart123"
}
```

## ssh 连接

新系统默认没有开启SSH，所以需要在boot启动盘的根目录创建一个名称为`ssh`且没有后缀名的文件。之后用网线连接到局域网之后，使用其他的电脑进行SSH连接即可。如果有路由器，可以看到树莓派分配的动态IP，如果没有路由器查看权限，则可直接使用hostname进行局域网内的连接，用户名 `pi`，密码`raspberry`：
```
ssh -p 22 pi@192.168.1.38
ssh -p 22 pi@raspberrypi.local
```

## VPN 内网穿透

### 使用蒲公英（没成功，VPN和surge/shadowsocks不能共存）

https://pgy.oray.com/download/

```bash
chmod a+x PgyVPN-1.0.0-arm.deb
sudo dpkg -i PgyVPN-1.0.0-arm.deb
sudo pgyvpn
  adolphor
  haizhu@1314.hsk
```

```bash
1,getmbrs:	get group membership info
2,bypass:	check coustom routes
3,chgacnt:	change account
4,showsets:	show setting
5,prtinfo:	turn on real-time info(when group membership changes)
6,noinfo:	turn off real-time info
7,slang:	change language(切换语言)
8,qservice:	exit and close VPN service
9,quit:		exit PgyVPN interface
```

### 使用lanproxy

此方法需要一个有公网IP的服务器，创建后台启动sh文件 `/home/pi/lanproxy.sh`：

```
nohup /home/pi/client_linux_arm7 -s 107.175.23.142 -p 4900 -k ea247997c01c462eb169ccbcd3ad90c1 >/dev/null 2>&1 &
```

编辑`/etc/rc.local`设置lanproxy为开机运行：

```
su pi -c "exec /home/pi/lanproxy.sh"
```
其中 `su` 表示使用root用户执行的此命令，`pi`表示切换到pi用户，`-c` 表示运行，双引号之内的为运行的具体内容。

### shadowsocks

```bash
# 安装Pip
sudo apt-get install python-pip python-gevent python-m2crypto
# 安装Shadowsocks
sudo pip install shadowsocks
# 配置文件 shadowsocks.json 内容如下
{
    "server":"0.0.0.0",
    "server_port":8388,
    "local_address": "127.0.0.1",
    "local_port":1080,
    "password":"123456",
    "timeout":300,
    "method":"aes-256-cfb",
    "fast_open": false,
    "workers": 1
}
# 将json配置移动到 /etc/文件夹下：
sudo mv ~/shadowsocks.json /etc/shadowsocks.json
# 启动服务
ssserver -c /etc/shadowsocks.json

# 如果报错，`AttributeError: /usr/lib/arm-linux-gnueabihf/libcrypto.so.1.1: undefined symbol: EVP_CIPHER_CTX_cleanup`，需要执行如下命令：
# 请你将此文件中的52行和111行中的cleanup更新为reset后，再尝试启动。这是由于openssl库更新导致名称变更的问题
# /usr/local/lib/python2.7/dist-packages/shadowsocks/crypto/openssl.py 

# 设定为自动启动的方法：
#  到 /etc/rc.local 文件的 exit 之前 
sudo vim /etc/rc.local
  nohup ssserver -c /etc/shadowsocks.json >/dev/null 2>&1 &
```

## update & proxy

系统的HTTP代理在update的时候不起作用：
```
export http_proxy=http://192.168.199.128:6162;export https_proxy=http://192.168.199.128:6162;export all_proxy=socks5://192.168.199.128:6163
```
需要使用如下形式来使用代理：
```
sudo apt-get -o Acquire::http::proxy="http://192.168.199.128:6162" update
sudo apt-get -o Acquire::http::proxy="http://192.168.199.128:6162" upgrade
```

## 安装常用软件

### vim
```
sudo apt-get -o Acquire::http::proxy="http://192.168.199.128:6162" install vim --fix-missing
```


### oracle-jdk8

```
sudo apt-get -o Acquire::http::proxy="http://192.168.199.128:6162" install oracle-java8-jdk --fix-missing
```

## 远程桌面

```
sudo apt-get -o Acquire::http::proxy="http://192.168.199.128:6162" install tightvncserver
```
然后使用 `vncpasswd` 设置密码：

* 读写权限：haizhu
* 只读权限：未启用

启动
```
tightvncserver :1
```

停止：
```
tightvncserver -kill :1
```

## 修改密码

```
# 修改root用户的密码 (haizhu123!@#)
sudo passwd
# 修改pi用户的密码（haizhu.rasp）
sudo passwd pi
```

## 开机后运行脚本

```bash
# 编辑 rc.local 文件
sudo vim /etc/rc.local
# 增加如下配置
su pi -c "exec /home/pi/mynety-lan/bin/startup.sh"
```

## 错误

### 一直自动重启

电源插头质量不好，或者输出功率不是 5V 1A，需要找个输出功率稳定、质量好的电源插头。

### ssh连接错误

```
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@    WARNING: REMOTE HOST IDENTIFICATION HAS CHANGED!     @
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
IT IS POSSIBLE THAT SOMEONE IS DOING SOMETHING NASTY!
Someone could be eavesdropping on you right now (man-in-the-middle attack)!
It is also possible that a host key has just been changed.
The fingerprint for the ECDSA key sent by the remote host is
SHA256:JAeh+bV8B7EAb70h4CtddugiALCDmhXSNapc3/6+/+w.
Please contact your system administrator.
Add correct host key in /Users/LiuMingchuan/.ssh/known_hosts to get rid of this message.
Offending ECDSA key in /Users/LiuMingchuan/.ssh/known_hosts:7
ECDSA host key for 121.43.169.99 has changed and you have requested strict checking.
Host key verification failed.
```

解决方法：
```
rm ~/.ssh/known_hosts
```
### 语言设置

终端下输入
```
sudo raspi-config
```
调出软件设计工具，选择5语言时区选择 ,

按空格键在前面打勾或去掉勾（星号=勾），PageUp PageDown快速翻页，Tab键跳到OK按钮上 
去掉 en_GB.UTF-8 UTF-8 
勾上“en_US.UTF-8 UTF-8”“zh_CN.UTF-8 UTF-8”“zh_CN.GBK GBK” 
下一屏幕默认语言选zh_CN.UTF-8 


## 其他

* [修改树莓派的风扇，使风扇能够随温度变化而启停](https://blog.csdn.net/qq_15947947/article/details/79637718)
* [树莓派：VNC远程登录Raspbian图形界面（tightvncserver）](https://blog.csdn.net/lu_embedded/article/details/50621203)
* [mac通过vnc远程桌面raspberry pi](https://blog.csdn.net/u010900754/article/details/53048998)
* [树莓派3B 搭建ss服务端笔记](https://lance.moe/post-300.html)
* [使用ss访问公司资源](http://ju.outofmemory.cn/entry/316721)
* [Surge Guide — 轻松访问家中的网络服务](https://medium.com/@Blankwonder/surge-guide-%E8%BD%BB%E6%9D%BE%E8%AE%BF%E9%97%AE%E5%AE%B6%E4%B8%AD%E7%9A%84%E7%BD%91%E7%BB%9C%E6%9C%8D%E5%8A%A1-6188ef189ca8)


