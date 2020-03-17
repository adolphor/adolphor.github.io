---
layout:     post
title:      WireShark 配置和使用
date:       2018-04-27 13:13:51 +0800
postId:     2018-04-27-13-13-51
categories: [article]
tags:       [Tools,网络]
geneMenu:   true
excerpt:    WireShark 配置和使用
---

WireShark过滤器有两种，捕捉过滤器和显示过滤器。

## 捕捉过滤器

捕捉过滤器的语法与其它使用Lipcap（Linux）或者Winpcap（Windows）库开发的软件一样，比如著名的TCPdump。
捕捉过滤器必须在开始捕捉前设置完毕，这一点跟显示过滤器是不同的。 

  - 语法格式

    Protocol | Direction | Host(s) | Value | Logical Operations | Other expression
    ---------|-----------|---------|-------|--------------------|-----------------------
    tcp      |  dst      | 10.1.1.1|  80   |       and          |  tcp dst 10.2.2.2 3128
  
  - __Protocol（协议）__
    - 可能的值: ether, fddi, ip, arp, rarp, decnet, lat, sca, moprc, mopdl, tcp and udp.
    - 如果没有特别指明是什么协议，则默认使用所有支持的协议。 
  
  - __Direction（方向）__
    - 可能的值：src, dst, src and dst, src or dst
    - 如果没有特别指明来源或目的地，则默认使用 "src or dst" 作为关键字。

  - __Host（主机）__
    - 可能的值： net, port, host, portrange.
    - 如果没有指定此值，则默认使用"host"关键字。

  - __Logical Operations（逻辑运算）__
    - 可能的值：not, and, or.
    - 否("not")具有最高的优先级。或("or")和与("and")具有相同的优先级，运算时从左至右进行。

  - __使用范例__
    - `tcp dst port 3128` 显示目的TCP端口为3128的封包。
    - `ip src host 10.1.1.1` 显示来源IP地址为10.1.1.1的封包。
    - `host 10.1.2.3` 显示目的或来源IP地址为10.1.2.3的封包。
    - `src portrange 2000-2500` 显示来源为UDP或TCP，并且端口号在2000至2500范围内的封包。
    - `not imcp` 显示除了icmp以外的所有封包。（icmp通常被ping工具使用）
    - `src host 10.7.2.12 and not dst net 10.200.0.0/16` 显示来源IP地址为10.7.2.12，但目的地不是10.200.0.0/16的封包。
    - `(src host 10.4.1.12 or src net 10.6.0.0/16) and tcp dst portrange 200-10000 and dst net 10.0.0.0/8` 
    显示来源IP为10.4.1.12或者来源网络为10.6.0.0/16，目的地TCP端口号在200至10000之间，并且目的位于网络10.0.0.0/8内的所有封包。 

  - __注意事项__
    - 当使用关键字作为值时，需使用反斜杠“\”。
    - "ether proto \ip" (与关键字"ip"相同)，这样写将会以IP协议作为目标。
    - "ip proto \icmp" (与关键字"icmp"相同)，这样写将会以ping工具常用的icmp作为目标。 
    - 可以在"ip"或"ether"后面使用"multicast"及"broadcast"关键字。
    - 当您想排除广播请求时，"no broadcast"就会非常有用。 
  
## 显示过滤器

通常经过捕捉过滤器过滤后的数据还是很复杂。此时您可以使用显示过滤器进行更加细致的查找。
它的功能比捕捉过滤器更为强大，而且在您想修改过滤器条件时，并不需要重新捕捉一次。

  - __语法格式__

    Protocol | String 1 | String 2 | Comparison operator |  Value   | Logical Operations | Other expression
    ---------|----------|----------|---------------------|----------|--------------------|-----------------
    ftp      | passive  |    ip    |         ==          | 10.2.3.4 |         xor        |     icmp.type

  - __Protocol（协议）__
  
    您可以使用大量位于OSI模型第2至7层的协议。点击"Expression..."按钮后，您可以看到它们。
    比如：IP，TCP，DNS，SSH
  
  - __String1, String2（可选项）__
  
    协议的子类。
    点击相关父类旁的"+"号，然后选择其子类。    
  
  - __Comparison operators （比较运算符）__ 
    可以使用6种比较运算符：

    英文写法 |C语言写法 | 含义
    --------|--------|--------
      eq    |   ==   |  等于
      ne    |   !=   |  不等于
      gt    |   >    |  大于
      lt    |   <    |  小于
      ge    |   >=   | 大于等于
      le    |   <=   | 小于等于

  - __Logical expressions（逻辑运算符）__

    英文写法 |C语言写法 | 含义
    --------|---------|--------
      and   |   &&    |  逻辑与
      or    |  \|\|   |  逻辑或
      xor   |   ^^    |  逻辑异或
      not   |   !     |  逻辑非
  
  - __使用范例__
  
    - `snmp || dns || icmp`	显示SNMP或DNS或ICMP封包。
    - `ip.addr == 10.1.1.1` 显示来源或目的IP地址为10.1.1.1的封包。
    - `ip.src != 10.1.2.3 or ip.dst != 10.4.5.6` 显示来源不为10.1.2.3或者目的不为10.4.5.6的封包。
    - `ip.src != 10.1.2.3 and ip.dst != 10.4.5.6` 显示来源不为10.1.2.3并且目的IP不为10.4.5.6的封包。
    - `tcp.port == 25` 显示来源或目的TCP端口号为25的封包。
    - `tcp.dstport == 25` 显示目的TCP端口号为25的封包。
    - `tcp.flags` 显示包含TCP标志的封包。
    - `tcp.flags.syn == 0x02` 显示包含TCP SYN标志的封包。


## 参考资料

* [Wireshark 过滤器](http://openmaniak.com/cn/wireshark_filters.php)
