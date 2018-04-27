---
layout:     post
title:      使用 WireShark 查看 HTTP 通信
date:       2018-04-26 10:51:35 +0800
postId:     2018-04-26-10-51-35
categories: [blog]
tags:       [Java,Java8]
geneMenu:   true
excerpt:    使用 WireShark 查看 HTTP 通信
---

## TCP 报文格式

TCP和IP 协议分别是`传输层`和`网络层`协议，主要解决数据如何在网络中传输；
HTTP是`应用层`协议，主要解决如何包装数据，用以封装 HTTP 文本信息。

因为HTTP是在TCP/IP之上的应用层协议，所以关于HTTP的三次握手，其实就是使用三次TCP握手确认建立一个HTTP连接。

先看TCP报文格式：
![TCP协议首部](/image/post/2018/04/26/20180426-TCP-protocol.jpg)

上图中有几个字段需要重点介绍下：
* 序号：Seq序号，占32位，用来标识从TCP源端向目的端发送的字节流，发起方发送数据时对此进行标记。
* 确认序号：Ack序号，占32位，只有ACK标志位为1时，确认序号字段才有效，Ack=Seq+1。
* 标志位：共6个，即URG、ACK、PSH、RST、SYN、FIN等，具体含义如下：
  - URG：紧急指针（urgent pointer）有效。
  - ACK：确认序号有效。
  - PSH：接收方应该尽快将这个报文交给应用层。
  - RST：重置连接。
  - SYN：发起一个新连接。
  - FIN：释放一个连接。

需要注意的是：
* 不要将确认序号Ack与标志位中的ACK搞混了。
* 确认方Ack=发起方Req+1，两端配对。 

## 三次握手建立连接

所谓三次握手（Three-Way Handshake）即建立TCP连接，就是指建立一个TCP连接时，需要客户端和服务端总共发送3个包以确认连接的建立。
在socket编程中，这一过程由客户端执行connect来触发，整个流程如下所示：
 
1) 第一次握手：Client将标志位SYN置为1，随机产生一个值seq=J，并将该数据包发送给Server，Client进入SYN_SENT状态，等待Server确认。

2) 第二次握手：Server收到数据包后由标志位SYN=1知道Client请求建立连接，Server将标志位SYN和ACK都置为1，ack=J+1，随机产生一个
  值seq=K，并将该数据包发送给Client以确认连接请求，Server进入SYN_RCVD状态。

3) 第三次握手：Client收到确认后，检查ack是否为J+1，ACK是否为1，如果正确则将标志位ACK置为1，ack=K+1，并将该数据包发送给Server，
  Server检查ack是否为K+1，ACK是否为1，如果正确则连接建立成功，Client和Server进入ESTABLISHED状态，完成三次握手，随后Client与
  Server之间可以开始传输数据了。

![TCP 3次握手](/image/post/2018/04/26/20180426-TCP-handshake.jpg)

## 四次挥手断开连接
所谓四次挥手（Four-Way Wavehand）即终止TCP连接，就是指断开一个TCP连接时，需要客户端和服务端总共发送4个包以确认连接的断开。
由于TCP连接时全双工的，因此，每个方向都必须要单独进行关闭，这一原则是当一方完成数据发送任务后，发送一个FIN来终止这一方向的连接，
收到一个FIN只是意味着这一方向上没有数据流动了，即不会再收到数据了，但是在这个TCP连接上仍然能够发送数据，直到这一方向也发送了FIN。
首先进行关闭的一方将执行主动关闭，而另一方则执行被动关闭。

在socket编程中，这一过程由客户端或服务端任一方执行close来触发，整个流程如下所示：

1) 第一次挥手：Client发送一个FIN，用来关闭Client到Server的数据传送，Client进入FIN_WAIT_1状态。

2) 第二次挥手：Server收到FIN后，发送一个ACK给Client，确认序号为收到序号+1（与SYN相同，一个FIN占用一个序号），Server进入CLOSE_WAIT状态。

3) 第三次挥手：Server发送一个FIN，用来关闭Server到Client的数据传送，Server进入LAST_ACK状态。

4) 第四次挥手：Client收到FIN后，Client进入TIME_WAIT状态，接着发送一个ACK给Server，确认序号为收到序号+1，Server进入CLOSED状态，完成四次挥手。

![TCP 3次握手](/image/post/2018/04/26/20180426-TCP-close-handshake.jpg)

## WireShark 的使用和验证

WireShark过滤器有两种，捕捉过滤器和显示过滤器。

### 捕捉过滤器

捕捉过滤器的语法与其它使用Lipcap（Linux）或者Winpcap（Windows）库开发的软件一样，比如著名的TCPdump。
捕捉过滤器必须在开始捕捉前设置完毕，这一点跟显示过滤器是不同的。 

  - 语法格式

    Protocol | Direction | Host(s) | Value | Logical Operations | Other expression
    ---------|-----------|---------|-------|--------------------|-----------------------
    tcp      |  dst      | 10.1.1.1|  80   |       and          |  tcp dst 10.2.2.2 3128
  
  - Protocol (协议)
    - 可能的值: ether, fddi, ip, arp, rarp, decnet, lat, sca, moprc, mopdl, tcp and udp.
    - 如果没有特别指明是什么协议，则默认使用所有支持的协议。 
  
  - Direction (方向)
    - 可能的值：src, dst, src and dst, src or dst
    - 如果没有特别指明来源或目的地，则默认使用 "src or dst" 作为关键字。

  - Host (主机)
    - 可能的值： net, port, host, portrange.
    - 如果没有指定此值，则默认使用"host"关键字。

  - Logical Operations（逻辑运算）
    - 可能的值：not, and, or.
    - 否("not")具有最高的优先级。或("or")和与("and")具有相同的优先级，运算时从左至右进行。

  - 使用范例
    - `tcp dst port 3128` 显示目的TCP端口为3128的封包。
    - `ip src host 10.1.1.1` 显示来源IP地址为10.1.1.1的封包。
    - `host 10.1.2.3` 显示目的或来源IP地址为10.1.2.3的封包。
    - `src portrange 2000-2500` 显示来源为UDP或TCP，并且端口号在2000至2500范围内的封包。
    - `not imcp` 显示除了icmp以外的所有封包。（icmp通常被ping工具使用）
    - `src host 10.7.2.12 and not dst net 10.200.0.0/16` 显示来源IP地址为10.7.2.12，但目的地不是10.200.0.0/16的封包。
    - `(src host 10.4.1.12 or src net 10.6.0.0/16) and tcp dst portrange 200-10000 and dst net 10.0.0.0/8` 
    显示来源IP为10.4.1.12或者来源网络为10.6.0.0/16，目的地TCP端口号在200至10000之间，并且目的位于网络10.0.0.0/8内的所有封包。 

  - 注意事项
    - 当使用关键字作为值时，需使用反斜杠“\”。
    - "ether proto \ip" (与关键字"ip"相同)，这样写将会以IP协议作为目标。
    - "ip proto \icmp" (与关键字"icmp"相同)，这样写将会以ping工具常用的icmp作为目标。 
    - 可以在"ip"或"ether"后面使用"multicast"及"broadcast"关键字。
    - 当您想排除广播请求时，"no broadcast"就会非常有用。 
  
### 显示过滤器

通常经过捕捉过滤器过滤后的数据还是很复杂。此时您可以使用显示过滤器进行更加细致的查找。
它的功能比捕捉过滤器更为强大，而且在您想修改过滤器条件时，并不需要重新捕捉一次。

  - 语法格式

    Protocol | String 1 | String 2 | Comparison operator |  Value   | Logical Operations | Other expression
    ---------|----------|----------|---------------------|----------|--------------------|-----------------
    ftp      | passive  |    ip    |         ==          | 10.2.3.4 |         xor        |     icmp.type

  - Protocol (协议)
    您可以使用大量位于OSI模型第2至7层的协议。点击"Expression..."按钮后，您可以看到它们。
    比如：IP，TCP，DNS，SSH
  
  - String1, String2 (可选项):
    协议的子类。
    点击相关父类旁的"+"号，然后选择其子类。    
  
  - Comparison operators （比较运算符）: 
    可以使用6种比较运算符：

    英文写法 |C语言写法 | 含义
    --------|--------|--------
      eq    |   ==   |  等于
      ne    |   !=   |  不等于
      gt    |   >    |  大于
      lt    |   <    |  小于
      ge    |   >=   | 大于等于
      le    |   <=   | 小于等于

  - Logical expressions (逻辑运算符)

    英文写法 |C语言写法 | 含义
    --------|---------|--------
      and   |   &&    |  逻辑与
      or    |  \|\|   |  逻辑或
      xor   |   ^^    |  逻辑异或
      not   |   !     |  逻辑非
  
  - 使用范例
    - `snmp || dns || icmp`	显示SNMP或DNS或ICMP封包。
    - `ip.addr == 10.1.1.1` 显示来源或目的IP地址为10.1.1.1的封包。
    - `ip.src != 10.1.2.3 or ip.dst != 10.4.5.6` 显示来源不为10.1.2.3或者目的不为10.4.5.6的封包。
    - `ip.src != 10.1.2.3 and ip.dst != 10.4.5.6` 显示来源不为10.1.2.3并且目的IP不为10.4.5.6的封包。
    - `tcp.port == 25` 显示来源或目的TCP端口号为25的封包。
    - `tcp.dstport == 25` 显示目的TCP端口号为25的封包。
    - `tcp.flags` 显示包含TCP标志的封包。
    - `tcp.flags.syn == 0x02` 显示包含TCP SYN标志的封包。


## 参考资料

* TCP/IP详解
* HTTP权威指南
* 计算机网络-自顶向下方法与Internet特色
* [HTTP与HTTPS握手的那些事](https://www.cnblogs.com/lovesong/p/5186200.html)
* [简述TCP的三次握手过程](https://blog.csdn.net/sssnmnmjmf/article/details/68486261)
* [访问Web，tcp传输全过程（三次握手、请求、数据传输、四次挥手）](https://blog.csdn.net/sinat_21455985/article/details/53508115)
* [Wireshark-TCP协议分析（包结构以及连接的建立和释放）](https://blog.csdn.net/ahafg/article/details/51039584)
* [Wireshark 过滤器](http://openmaniak.com/cn/wireshark_filters.php)