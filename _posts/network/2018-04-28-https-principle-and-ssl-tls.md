---
layout:     post
title:      https 原理及相关
date:       2018-04-28 15:51:29 +0800
postId:     2018-04-28-15-51-30
categories: []
tags:       [网络]
geneMenu:   true
excerpt:    https 原理及相关
---

<style>
p img[alt="TLS/SSL 加密算法"] {
  text-align: center;
  margin: 0 auto;
}
</style>

## 密码学基础

* __明文__

  明文指的是未被加密过的原始数据。
  
* __密文__

  明文被某种加密算法加密之后，会变成密文，从而确保原始数据的安全。密文也可以被解密，得到原始的明文。

* __密钥__
  
  密钥是一种参数，它是在明文转换为密文或将密文转换为明文的算法中输入的参数。密钥分为对称密钥与非对称密钥，分别应用在对称加密和非对称加密上。

  - 对称加密：只有一个密钥，加解密都用同一个密钥
  - 非对称加密
    - 公钥：分发给所有公用的用户
    - 私钥：服务器自己保存

### 对称加密  

对称加密又叫做私钥加密，即信息的发送方和接收方使用同一个密钥去加密和解密数据。对称加密的特点是算法公开、加密和解密速度快，
适合于对大数据量进行加密，常见的对称加密算法有DES、3DES、TDEA、Blowfish、RC5和IDEA。 

其加密过程如下：
  
```
明文 + 加密算法 + 私钥 => 密文 
```

解密过程如下：

```
密文 + 解密算法 + 私钥 => 明文 
```

对称加密中用到的密钥叫做私钥，私钥表示个人私有的密钥，即该密钥不能被泄露。 
其加密过程中的私钥与解密过程中用到的私钥是同一个密钥，这也是称加密之所以称之为“对称”的原因。由于对称加密的算法是公开的，
所以一旦私钥被泄露，那么密文就很容易被破解，所以对称加密的缺点是密钥安全管理困难。
  
### 非对称加密 

非对称加密也叫做公钥加密。非对称加密与对称加密相比，其安全性更好。对称加密的通信双方使用相同的密钥，如果一方的密钥遭泄露，
那么整个通信就会被破解。而非对称加密使用一对密钥，即公钥和私钥，且二者成对出现。私钥被自己保存，不能对外泄露。公钥指的是公共的密钥，
任何人都可以获得该密钥。用公钥或私钥中的任何一个进行加密，用另一个进行解密。 

被公钥加密过的密文只能被私钥解密，过程如下： 

```
明文 + 加密算法 + 公钥 => 密文
密文 + 解密算法 + 私钥 => 明文 
```
  
被私钥加密过的密文只能被公钥解密，过程如下： 

```
明文 + 加密算法 + 私钥 => 密文
密文 + 解密算法 + 公钥 => 明文 
```
  
由于加密和解密使用了两个不同的密钥，这就是非对称加密“非对称”的原因。 
非对称加密的缺点是加密和解密花费时间长、速度慢，只适合对少量数据进行加密。 
在非对称加密中使用的主要算法有：RSA、Elgamal、Rabin、D-H、ECC（椭圆曲线加密算法）等。

## HTTPS通信协议和流程

### HTTPS 和 SSL/TLS 名词介绍

* __SSL__

  SSL的全称是Secure Sockets Layer，即安全套接层协议，是为网络通信提供安全及数据完整性的一种安全协议。SSL协议在1994年被Netscape发明，
  后来各个浏览器均支持SSL，其最新的版本是3.0

* __TLS__

  TLS的全称是Transport Layer Security，即安全传输层协议，最新版本的TLS（Transport Layer Security，传输层安全协议）是IETF
  （Internet Engineering Task Force，Internet工程任务组）制定的一种新的协议，它建立在SSL 3.0协议规范之上，是SSL 3.0的后续版本。
  在TLS与SSL3.0之间存在着显著的差别，主要是它们所支持的加密算法不同，所以TLS与SSL3.0不能互操作。虽然TLS与SSL3.0在加密算法上不同，
  但是在我们理解HTTPS的过程中，我们可以把SSL和TLS看做是同一个协议。

* __HTTPS__

  HTTPS协议 = HTTP协议 + SSL/TLS协议，在HTTPS数据传输的过程中，需要用SSL/TLS对数据进行加密和解密，需要用HTTP对加密后的数据进行传输，
  由此可以看出HTTPS是由HTTP和SSL/TLS一起合作完成的。
  
  HTTPS为了兼顾安全与效率，同时使用了对称加密和非对称加密。数据是被对称加密传输的，对称加密过程需要客户端的一个密钥，为了确保能把该密钥
  安全传输到服务器端，采用非对称加密对该密钥进行加密传输，总的来说，对数据进行对称加密，对称加密所要使用的密钥通过非对称加密传输。

### 协议层次框架

HTTPS (Secure Hypertext Transfer Protocol)安全超文本传输协议，是一个安全通信通道，它基于HTTP开发用于在客户计算机和服务器之间交换信
息。它使用安全套接字层(SSL)进行信息交换，简单来说它是HTTP的安全版,是使用TLS/SSL加密的HTTP协议。

![HTTP vs HTTPS]({{ site.baseurl }}/image/post/2018/04/28/20180428-http-vs-https.jpg)

协议栈如下所示：

![HTTPS流程图]({{ site.baseurl }}/image/post/2018/04/28/20180428-https-ssl-protocol.jpg)

TLS/SSL的功能实现主要依赖于三类基本算法：散列函数 Hash、对称加密和非对称加密，其利用非对称加密实现身份认证和密钥协商，
对称加密算法采用协商的密钥对数据加密，基于散列函数验证信息的完整性。

![TLS/SSL 加密算法]({{ site.baseurl }}/image/post/2018/04/28/20190209-tls-ssl-encrypt.jpg)    

### 流程简图

HTTPS在传输的过程中会涉及到三个密钥：

* 服务器端的公钥和私钥，用来进行非对称加密
* 客户端生成的随机密钥，用来进行对称加密

总的流程如下所示：

![HTTPS流程图]({{ site.baseurl }}/image/post/2018/04/28/20180428-https-flow.jpg)

一个HTTPS请求实际上包含了两次HTTP传输，可以细分为8步：

* 客户端向服务器发起HTTPS请求，连接到服务器的443端口。
* 服务器端有一个密钥对，即公钥和私钥，是用来进行非对称加密使用的，服务器端保存着私钥，不能将其泄露，公钥可以发送给任何人。
* 服务器将自己的公钥发送给客户端。
* 客户端收到服务器端的公钥之后，会对公钥进行检查，验证其合法性，如果发现发现公钥有问题，那么HTTPS传输就无法继续。严格的说，
这里应该是验证服务器发送的数字证书的合法性，关于客户端如何验证数字证书的合法性，下文会进行说明。如果公钥合格，那么客户端会
生成一个随机值，这个随机值就是用于进行对称加密的密钥，我们将该密钥称之为client key，即客户端密钥，这样在概念上和服务器端的
密钥容易进行区分。然后用服务器的公钥对客户端密钥进行非对称加密，这样客户端密钥就变成密文了，至此，HTTPS中的第一次HTTP请求结束。
* 客户端会发起HTTPS中的第二个HTTP请求，将加密之后的客户端密钥发送给服务器。
* 服务器接收到客户端发来的密文之后，会用自己的私钥对其进行非对称解密，解密之后的明文就是客户端密钥，然后用客户端密钥对数据进行
对称加密，这样数据就变成了密文。
* 然后服务器将加密后的密文发送给客户端。
* 客户端收到服务器发送来的密文，用客户端密钥对其进行对称解密，得到服务器发送的数据。这样HTTPS中的第二个HTTP请求结束，整个HTTPS
传输完成。

## SSL握手协议详解

SSL协议栈位置介于TCP和应用层之间，分为SSL记录协议层和SSL握手协议层。其中SSL握手协议层又分为SSL握手协议、SSL密钥更改协议和SSL警告协议。
SSL握手协议作用是在通信双方之间协商出密钥，SSL记录层的作用是定义如何对上层的协议进行封装。SSL记录协议将数据块进行拆分压缩，计算消息验证码，
加密，封装记录头然后进行传输。

所以，SSL握手协议是在TCP协议完成之后进行，整个过程如下所示：

![HTTPS流程图]({{ site.baseurl }}/image/post/2018/04/28/20180428-ssl-flow.jpg)

* client_hello

  客户端发起请求，以明文传输请求信息，包含版本信息，加密套件候选列表，压缩算法候选列表，随机数，扩展字段等信息，相关信息如下：
  - 支持的最高TSL协议版本version，从低到高依次 SSLv2 SSLv3 TLSv1 TLSv1.1 TLSv1.2，当前基本不再使用低于 TLSv1 的版本;
  - 客户端支持的加密套件 cipher suites 列表， 每个加密套件对应前面 TLS 原理中的四个功能的组合：认证算法 Au (身份验证)、密钥交换算法 
  KeyExchange(密钥协商)、对称加密算法 Enc (信息加密)和信息摘要 Mac(完整性校验);
  - 支持的压缩算法 compression methods 列表，用于后续的信息压缩传输;
  - 随机数 random_C，用于后续的密钥的生成;
  - 扩展字段 extensions，支持协议与算法的相关参数以及其它辅助信息等，常见的 SNI 就属于扩展字段，后续单独讨论该字段作用。
* server_hello + server_certificate + sever_hello_done
  - server_hello, 服务端返回协商的信息结果，包括选择使用的协议版本 version，选择的加密套件 cipher suite，选择的压缩算法 compression
   method、随机数 random_S 等，其中随机数用于后续的密钥协商;
  - server_certificates, 服务器端配置对应的证书链，用于身份验证与密钥交换;
  - server_hello_done，通知客户端 server_hello 信息发送结束; 
* 证书校验

  客户端验证证书的合法性，如果验证通过才会进行后续通信，否则根据错误情况不同做出提示和操作，合法性验证包括如下：
  - [证书链]的可信性 trusted certificate path，方法如前文所述; 
  - 证书是否吊销 revocation，有两类方式离线 CRL 与在线 OCSP，不同的客户端行为会不同;
  - 有效期 expiry date，证书是否在有效时间范围;
  - 域名 domain，核查证书域名是否与当前的访问域名匹配，匹配规则后续分析; 
* client_key_exchange + change_cipher_spec + encrypted_handshake_message
  - client_key_exchange，合法性验证通过之后，客户端计算产生随机数字 Pre-master，并用证书公钥加密，发送给服务器;
  - 此时客户端已经获取全部的计算协商密钥需要的信息：两个明文随机数 random_C 和 random_S 与自己计算产生的 Pre-master，计算得到协商密钥：
  enc_key=Fuc(random_C, random_S, Pre-Master)
  - change_cipher_spec，客户端通知服务器后续的通信都采用协商的通信密钥和加密算法进行加密通信;
  - encrypted_handshake_message，结合之前所有通信参数的 hash 值与其它相关信息生成一段数据，采用协商密钥 session secret 与算法进行
  加密，然后发送给服务器用于数据与握手验证;
* change_cipher_spec + encrypted_handshake_message
  - 服务器用私钥解密加密的 Pre-master 数据，基于之前交换的两个明文随机数 random_C 和 random_S，计算得到协商密钥:
  enc_key=Fuc(random_C, random_S, Pre-Master);
  - 计算之前所有接收信息的 hash 值，然后解密客户端发送的 encrypted_handshake_message，验证数据和密钥正确性;
  - change_cipher_spec, 验证通过之后，服务器同样发送 change_cipher_spec 以告知客户端后续的通信都采用协商的密钥与算法进行加密通信;
  - encrypted_handshake_message, 服务器也结合所有当前的通信参数信息生成一段数据并采用协商密钥 session secret 与算法加密并发送到客户端;
* 握手结束
  - 客户端计算所有接收信息的 hash 值，并采用协商密钥解密 encrypted_handshake_message，验证服务器发送的数据和密钥，验证通过则握手完成;
* 加密通信

  开始使用协商密钥与算法进行加密通信。注意：
  - 服务器也可以要求验证客户端，即双向认证，可以在过程2要发送 client_certificate_request 信息，客户端在过程4中先发送 
  client_certificate与certificate_verify_message 信息，证书的验证方式基本相同，certificate_verify_message 是采用client的私钥
  加密的一段基于已经协商的通信信息得到数据，服务器可以采用对应的公钥解密并验证;
  - 根据使用的密钥交换算法的不同，如 ECC 等，协商细节略有不同，总体相似;
  - sever key exchange 的作用是 server certificate 没有携带足够的信息时，发送给客户端以计算 pre-master，如基于 DH 的证书，公钥不
  被证书中包含，需要单独发送;
  - change cipher spec 实际可用于通知对端改版当前使用的加密通信方式，当前没有深入解析;
  - alter message 用于指明在握手或通信过程中的状态改变或错误信息，一般告警信息触发条件是连接关闭，收到不合法的信息，信息解密失败，用户
  取消操作等，收到告警信息之后，通信会被断开或者由接收方决定是否断开连接。
 
## 数字证书

通过观察HTTPS的传输过程，我们知道，当服务器接收到客户端发来的请求时，会向客户端发送服务器自己的公钥，但是黑客有可能中途篡改公钥，
将其改成黑客自己的，所以有个问题，客户端怎么信赖这个公钥是自己想要访问的服务器的公钥而不是黑客的呢？ 这时候就需要用到数字证书。

在讲数字证书之前，先说一个小例子。假设一个镇里面有两个人A和B，A是个富豪，B想向A借钱，但是A和B不熟，怕B借了钱之后不还。这时候B找
到了镇长，镇长给B作担保，告诉A说：“B人品不错，不会欠钱不还的，你就放心借给他吧。” A听了这话后，心里想：“镇长是全镇最德高望重的了，
他说B没问题的话那就没事了，我就放心了”。 于是A相信B的为人，把钱借给了B。

与此相似的，要想让客户端信赖公钥，公钥也要找一个担保人，而且这个担保人的身份必须德高望重，否则没有说服力。这个担保人的就是证书认证
中心（Certificate Authority），简称CA。 也就是说CA是专门对公钥进行认证，进行担保的，也就是专门给公钥做担保的担保公司。 全球知名
的CA也就100多个，这些CA都是全球都认可的，比如VeriSign、GlobalSign等，国内知名的CA有WoSign。

那CA怎么对公钥做担保认证呢？CA本身也有一对公钥和私钥，CA会用CA自己的私钥对要进行认证的公钥进行非对称加密，此处待认证的公钥就相当
于是明文，加密完之后，得到的密文再加上证书的过期时间、颁发给、颁发者等信息，就组成了数字证书。

不论什么平台，设备的操作系统中都会内置100多个全球公认的CA，说具体点就是设备中存储了这些知名CA的公钥。当客户端接收到服务器的数字证
书的时候，会进行如下验证：

* 首先客户端会用设备中内置的CA的公钥尝试解密数字证书，如果所有内置的CA的公钥都无法解密该数字证书，说明该数字证书不是由一个全球知名
的CA签发的，这样客户端就无法信任该服务器的数字证书。
* 如果有一个CA的公钥能够成功解密该数字证书，说明该数字证书就是由该CA的私钥签发的，因为被私钥加密的密文只能被与其成对的公钥解密。
* 除此之外，还需要检查客户端当前访问的服务器的域名是与数字证书中提供的“颁发给”这一项吻合，还要检查数字证书是否过期等。
  
上面说到操作系统中有内置的100多个全球公认的CA，但是还有些CA可以信任而系统没有收录，这种情况下，就可以手动的将可以被信任的征收，自己
收到导入到系统中，比如12306网站的证书是由SRCA（Sinorail Certification Authority：铁道部旗下的中铁数字证书认证中心）颁发的，但
一般情况下操作系统并没有将其收录为可信证书签发机构，所以在电脑中可以将此站点证书导出之后导入到系统证书目录，就可以了。

## wireshark调试HTTPS

### 查看连接建立过程

配置wireshark过滤规则：

```shell
tcp && host adolphor.com
```

```shell
curl https://adolphor.com/js/main.js?version=20180129
```

### 浏览器下使用MITM

首先了解一下调试HTTPS的三种方法：

* RSA Private Key
  如果你拥有 HTTPS 网站的加密私钥，可以用来解密这个网站的加密流量。（使用私钥获取到加密方法和客户端key之后进行解密？）
* Man-in-the-middle（中间人）
  能够与网络通讯两端分别创建连接，交换其收到的数据，使得通讯两端都认为自己直接与对方对话，事实上整个会话都被中间人所控制。简而言之，
  在真正的服务端看来，中间人是客户端；而真正的客户端会认为中间人是服务端。Fiddler、Charles 和 whistle都是这种实现。
  首先 Fiddler 作为客户端跟服务端建立 TLS 连接，使用服务端的证书，处理请求和响应；然后 Fiddler 又作为服务端跟浏览器建立 TLS 连接，
  使用 Fiddler 的证书，处理请求和响应。所以 Fiddler 要解密 HTTPS 流量，需要先把它生成的根证书添加到系统受信任的根证书列表之中。
* SSLKEYLOGFILE
  Firefox 和 Chrome 都会在系统环境变量存在 SSLKEYLOGFILE 文件路径时，将每个 HTTPS 连接产生的 Premaster Secret 或 Master Secret 
  存下来。有了这个文件，Wireshark 就可以轻松解密 HTTPS 流量，即使是使用了 ECDHE 这种具有前向安全性的密钥交换。

Wireshark 的抓包原理是直接读取并分析网卡数据，要想让它解密 HTTPS 流量，有两个办法：1）如果你拥有 HTTPS 网站的加密私钥，可以用来解密这个
网站的加密流量；2）某些浏览器支持将 TLS 会话中使用的对称密钥保存在外部文件中，可供 Wireshark 加密使用。本文重点介绍第二种方法。
Firefox 和 Chrome 都支持生成上述第二种方式的文件，具体格式见这里：NSS Key Log Format。但 Firefox 和 Chrome 只会在系统环境变量中存在
SSLKEYLOGFILE 路径时才会生成它，先来加上这个环境变量（以 OSX 为例，如果是windows就新建一个名为SSLKEYLOGFILE的环境变量）：

```bash
mkdir ~/tls && touch ~/tls/sslkeylog.log

#zsh
echo "\nexport SSLKEYLOGFILE=~/tls/sslkeylog.log" >> ~/.zshrc && source ~/.zshrc

#bash
echo "\nexport SSLKEYLOGFILE=~/tls/sslkeylog.log" >> ~/.bash_profile && source ~/.bash_profile
```
接着，在 Wireshark 的 SSL 配置面板的 「(Pre)-Master-Secret log filename」选项中这个文件选上。如下图：

![wireShark配置SSLKEYLOGFILE]({{ site.baseurl }}/image/post/2018/04/28/20180428-ssl-wireshark-sslkeylogfile.jpg)

通过 `terminal` 终端启动 Firefox 或 Chrome（确保能读取到环境变量，本机使用iterm2启动的时候没有成功）：

```bash
open /Applications/Firefox.app
open /Applications/Google\ Chrome.app
```

这时再访问 HTTPS 网站，sslkeylog.log 文件中应该有浏览器写入的数据了。

![ssl log content]({{ site.baseurl }}/image/post/2018/04/28/20180428-ssl-wireshark-ssllog-content.jpg)
![ssl key log file content]({{ site.baseurl }}/image/post/2018/04/28/20180428-ssl-wireshark-sslkeylogfile-content.jpg)

检查无误后，就可以开启 Wireshark，选择合适的网卡开始抓包（本文目的是抓取 HTTP/2 数据包，可以将 TCP 端口限定在 443，让抓到的数据少一些）：

![wireShark监听443]({{ site.baseurl }}/image/post/2018/04/28/20180428-ssl-wireshark-443.jpg)

这里就可以看到解密之后的数据（就看到了一次！！！！雾~ 以后再说吧，等解密之后再上图~）。

## 参考资料

* [HTTPS理论基础及其在Android中的最佳实践](https://blog.csdn.net/iispring/article/details/51615631)
* [HTTPS加密协议详解](https://www.wosign.com/faq/faq2016-0309-04.htm)
* [三种解密 HTTPS 流量的方法介绍](https://imququ.com/post/how-to-decrypt-https.html)
* [Keyless SSL: The Nitty Gritty Technical Details](https://blog.cloudflare.com/keyless-ssl-the-nitty-gritty-technical-details/)