---
layout:     post
title:      HTTP 通信 —— 三次握手和四次挥手
date:       2018-04-26 10:51:35 +0800
postId:     2018-04-26-10-51-35
categories: []
tags:       [Tools,网络]
geneMenu:   true
excerpt:    HTTP 通信 —— 三次握手和四次挥手
---

## TCP 报文格式

TCP和IP 协议分别是`传输层`和`网络层`协议，主要解决数据如何在网络中传输；
HTTP是`应用层`协议，主要解决如何包装数据，用以封装 HTTP 文本信息。

因为HTTP是在TCP/IP之上的应用层协议，所以关于HTTP的三次握手，其实就是使用三次TCP握手确认建立一个HTTP连接。

先看TCP报文格式：
![TCP协议首部]({{ site.baseurl }}/image/post/2018/04/26/20180426-TCP-protocol.jpg)

上图中有几个字段需要重点介绍下：
* 序号：Seq序号，占32位，用来标识从TCP源端向目的端发送的字节流，发起方发送数据时对此进行标记。
* 确认序号：Ack序号，占32位，只有ACK标志位为1时，确认序号字段才有效，Ack=Seq+1。
* 标志位：共6个，即URG、ACK、PSH、RST、SYN、FIN等，具体含义如下：
  - URG => Urgent          ：紧急指针（urgent pointer）有效
  - ACK => Acknowledgement ：确认序号有效
  - PSH => Push            ：接收方应该尽快将这个报文交给应用层
  - RST => Reset           ：重置连接
  - SYN => Synchronize     ：发起一个新连接
  - FIN => Finished        ：释放一个连接

需要注意的是：
* 不要将确认序号Ack与标志位中的ACK搞混了。
* 确认方Ack=发起方Req+1，两端配对。 
* 还有两个未知作用的标志，[参考](http://www.vbforums.com/showthread.php?484268-Packet-flags-What-is-URG-ACK-PSH-RST-SYN-FIN)
    - CWR => Congestion Window Reduced
    - ECE => Explicit Congestion Notification Echo 


## 三次握手建立连接

所谓三次握手（Three-Way Handshake）即建立TCP连接，就是指建立一个TCP连接时，需要客户端和服务端总共发送3个包以确认连接的建立。
在socket编程中，这一过程由客户端执行connect来触发，整个流程如下所示：
 
* 第一次握手：Client将标志位SYN置为1，随机产生一个值seq=J，并将该数据包发送给Server，Client进入SYN_SENT状态，等待Server确认。

* 第二次握手：Server收到数据包后由标志位SYN=1知道Client请求建立连接，Server将标志位SYN和ACK都置为1，ack=J+1，随机产生一个
  值seq=K，并将该数据包发送给Client以确认连接请求，Server进入SYN_RCVD状态。

* 第三次握手：Client收到确认后，检查ack是否为J+1，ACK是否为1，如果正确则将标志位ACK置为1，ack=K+1，并将该数据包发送给Server，
  Server检查ack是否为K+1，ACK是否为1，如果正确则连接建立成功，Client和Server进入ESTABLISHED状态，完成三次握手，随后Client与
  Server之间可以开始传输数据了。

![TCP 3次握手]({{ site.baseurl }}/image/post/2018/04/26/20180426-TCP-handshake.jpg)

## 四次挥手断开连接
所谓四次挥手（Four-Way Wavehand）即终止TCP连接，就是指断开一个TCP连接时，需要客户端和服务端总共发送4个包以确认连接的断开。
由于TCP连接时全双工的，因此，每个方向都必须要单独进行关闭，这一原则是当一方完成数据发送任务后，发送一个FIN来终止这一方向的连接，
收到一个FIN只是意味着这一方向上没有数据流动了，即不会再收到数据了，但是在这个TCP连接上仍然能够发送数据，直到这一方向也发送了FIN。
首先进行关闭的一方将执行主动关闭，而另一方则执行被动关闭。

在socket编程中，这一过程由客户端或服务端任一方执行close来触发，整个流程如下所示：

* 第一次挥手：Client发送一个FIN，用来关闭Client到Server的数据传送，Client进入FIN_WAIT_1状态。

* 第二次挥手：Server收到FIN后，发送一个ACK给Client，确认序号为收到序号+1（与SYN相同，一个FIN占用一个序号），Server进入CLOSE_WAIT状态。

* 第三次挥手：Server发送一个FIN，用来关闭Server到Client的数据传送，Server进入LAST_ACK状态。

* 第四次挥手：Client收到FIN后，Client进入TIME_WAIT状态，接着发送一个ACK给Server，确认序号为收到序号+1，Server进入CLOSED状态，完成四次挥手。

![TCP 3次握手]({{ site.baseurl }}/image/post/2018/04/26/20180426-TCP-close-handshake.jpg)

## WireShark 监测 HTTP

配置好wireshark的过滤规则之后，使用curl命名发送HTTP的GET请求，就可以看到握手以及资源访问相关的请求和连接了。

配置规则，只监测TCP协议，而且是访问`adolphor.com`域名的链接：

```shell
tcp && host adolphor.com
```

请求指令：

```shell
curl http://news.baidu.com/passport
```

![WireShark监测HTTP请求]({{ site.baseurl }}/image/post/2018/04/26/20180426-http-wireshark-snapshot.jpg)

可以看到
* 第一次请求，TCP协议，客户端个向服务器发送请求，SYN，且 seq=0。
* 第二次请求，TCP协议，服务器向客户端发送请求并响应第一步中客户端的请求，SYN & ACK，且 seq=0 & ack=1。
* 第三次请求，TCP协议，客户端响应第二步中服务器的请求，ACK，且 seq=1 & ack=1。
* 第四次请求，HTTP协议，也就是建立连接之后的具体HTTP业务请求信息。

![WireShark监测HTTP请求]({{ site.baseurl }}/image/post/2018/04/26/20180426-http-wireshark-snapshot-get.jpg)

## 参考资料

* TCP/IP详解
* HTTP权威指南
* 计算机网络-自顶向下方法与Internet特色
* [HTTP与HTTPS握手的那些事](https://www.cnblogs.com/lovesong/p/5186200.html)
* [简述TCP的三次握手过程](https://blog.csdn.net/sssnmnmjmf/article/details/68486261)
* [访问Web，tcp传输全过程（三次握手、请求、数据传输、四次挥手）](https://blog.csdn.net/sinat_21455985/article/details/53508115)
* [Wireshark-TCP协议分析（包结构以及连接的建立和释放）](https://blog.csdn.net/ahafg/article/details/51039584)
* [WireShark 配置和使用]({% post_url network/2018-04-27-wireshark-configuration %})
