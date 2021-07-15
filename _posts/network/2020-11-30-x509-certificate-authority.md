---
layout:     post
title:      X509证书体系
date:       2020-11-30 09:49:53 +0800
postId:     2020-11-30-09-49-53
categories: [database]
keywords:   [网络]
---


## 公钥体系与 Diffie-Hellman 协议

说到证书，不得不先引入下 公钥体系 的概念：

1. 公开密钥基础建设透过信任数字证书认证机构的根证书、及其使用公开密钥加密作数字签名核发的公开密钥认证，形成信任链架构，已在 TLS 实现并在万维网的 HTTP 以 HTTPS、在电子邮件的 SMTP 以 STARTTLS 引入
2. 公钥解密的特性可以形成数字签名，使数据和文件受到保护并可信赖；如果公钥透过数字证书认证机构签授成为电子证书，更可作为数字身份的认证，这都是对称密钥加密无法实现的
3. 不过，公钥加密在在计算上相当复杂，性能欠佳、远远不比对称加密；因此，在一般实际情况下，往往通过公钥加密来随机创建临时的对称秘钥，亦即对话键，然后才通过对称加密来传输大量、主体的数据

上面的第 3 点，就引出了，经典的秘钥交换（协商）协议 Diffie-Hellman，解决了不同实体间，由公钥对（Public Key Pair）协商出对称（共享）秘钥的过程。Openssh 的通信过程就是基于此来实现的、https 亦是。

## 一些基础名词

CA 实现了 PKI 体系 中一些非常重要的功能，核心功能就是 “发放” 和 “管理” 数字证书，证书抽象起来就是：CSR+ 签发者公钥 + 签名 + 有效期：
* 接收验证最终用户数字证书的申请
* 确定是否接受最终用户数字证书的申请，即证书的审批（签发）
* 向申请者颁发、拒绝颁发数字证书
* 接收、处理最终用户的数字证书更新请求
* 接收最终用户数字证书的查询、撤销
* 产生和发布证书废止列表
* 数字证书的归档、密钥归档及历史数据归档

### PKI
PKI = Public Key Infrastructure，是一整套安全相关标准

### CA
CA = Certificate authority， 是 PKI 的”核心”，即数字证书的申请及签发机关，CA 必须具备权威性的特征，它负责管理 PKI 结构下的所有用户 (包括各种应用程序) 的证书，把用户的公钥和用户的其他信息捆绑在一起，在网上验证用户的身份，CA 还要负责用户证书的黑名单登记和黑名单发布 。

### X.509
X.509，当前使用很广泛的一套证书标准，它规范了公开密钥认证、证书吊销列表、授权证书、证书路径验证算法、证书内容及格式等（如 https 证书）。X509 V3 版本的证书基本语法如下（只列举了 Certificate 和 TBSCertificate 这两个结构），其他的描述见 RFC5280。其中 tbsCertificate 的数据段被拿来做 Digest，并且用上级证书的私钥加密后形成签名置入 https 证书中

### CSR
CSR = Certificate signing request，用户申请 CA 证书的签名申请

## 证书文件和格式

### 证书的基础
证书的作用是用来实现身份认证，举个例子来说，A 和 B 需要认证对方，但 A、B 都信任可信中间人 C，如果 C 分别颁发证书 CERT-A 和 CERT-B 给 A 和 B，那么 A、B 在交换证书时，分别验证证书中 C 的身份，那么 A 和 B 就完成了互相认证。在当今的流行的应用中，证书在系统安全的构建中也是极为广泛，比如 Kubernetes 系统中的使用的证书对。通常，证书就是一个包含如下身份信息的文件：

* *证书所有组织的信息*  
* *颁发者公钥*  
* *证书颁发组织的信息*  
* *证书颁发组织授予的权限，如证书有效期、适用的主机名、用途等*  
* *使用证书颁发组织私钥创建的数字签名*
* *证书申请者提交的 CSR 属性*  

既然是文件，那么必可不少的就是文件的编码属性：
* ASN.1（Abstract Syntax Notation One），一种描述数字对象的方法和标准。ASN.1 提供了多种数据编码方法。包括了 BER、DER、PER 和 XER 等。这些编码方法规定了将数字对象转换成应用程序能够处理、保存和网络传输的二进制编码形式的一组规则
* DER 编码（Distinguished Encoding Rules）：属于 ASN.1 下的 BER（Basic Encode Rules）编码派生出来的编码规则，这种编码规则下，一个相同的 ASN.1 对象编码后能得到唯一的编码数据（BER 编码不能保证这一点，即一个对象编码后可能产生多个不同的编码数据）
* PEM 编码（Privacy Enhanced Mail）：是一种保密邮件的编码标准，在 rfc1421 规范中规定。X.509 的证书在 DER 编码的基础上进行 base64 编码，然后添加一些头、尾标志就是 PEM 格式编码了，头尾的标志也是 PEM 的一部分，不要随意改动

在项目中最常用的是 PEM 编码格式，PEM 格式的文件如下，Head 和 Tail 告诉我们这是一个 Certificate：

> -----BEGIN CERTIFICATE-----  
> xxxxxxxxxxxxxxxxxxxxxxxxxxxx  
> -----END CERTIFICATE-----  

X.509 证书的结构图如下，从图中可以清晰的看出 V1/V2/V3 版本的区别：
![X509 Version]({{ site.baseurl }}/image/post/2020/11/30/X509/X509-Version.png)

X.509 证书基本部分如下：

* 版本：标识证书的版本 (版本 1、2、3)，现行通用版本是 V3  
* 序号：标识证书的唯一整数，由证书颁发者分配的本证书的唯一标识符，特别在撤消证书的时候有用  
* 主体：拥有此证书的法人或自然人身份或机器，包括：  
* -- 国家（C，Country）  
* -- 州 / 省（S，State）  
* -- 地域 / 城市（L，Location）  
* -- 组织 / 单位（O，Organization）  
* -- 通用名称（CN，Common Name）：在 TLS 应用上，此字段一般是网域  
* 发行者：以数字签名形式签署此证书的数字证书认证机构  
* 有效期开始时间：Not Before，此证书的有效开始时间，在此前该证书并未生效  
* 有效期结束时间：Not After，此证书的有效结束时间，在此后该证书作废  
* 公开密钥用途：指定证书上公钥的用途，例如数字签名、服务器验证、客户端验证等  
* 公开密钥：主体的公钥（以及算法标识符）  
* 公开密钥指纹  
* 数字签名：用于签证书的算法标识，由对象标识符加上相关的参数组成，用于说明本证书所用的数字签名算法。例如，SHA-1 和 RSA 的对象标识符就用来说明该数字签名是利用 RSA > 对 SHA-1 杂凑加密  
* 数字签名算法  
* 主体别名：例如一个网站可能会有多个网域（www.wikipedia.org, zh.wikipedia.org, zh.m.wikipedia.org 都是维基百科）、一个组织可能会有多个网站（*.wikipedia.org, *.wikibooks.org, *.wikidata.org 都是维基媒体基金会旗下的网域），不同的网域可以一并使用同一张证书，方便实现应用及管理  

### CSR 的意义
CSR(Certificate Signing Request)，它是向 CA 机构申请数字证书时使用的请求文件。在生成请求文件前，我们需要准备一对对称密钥。私钥信息自己保存，请求中会附上公钥信息以及国家，城市，域名，Email 等信息，CSR 中还会附上签名信息。当我们准备好 CSR 文件后就可以提交给 CA 机构，等待他们给我们签名，签好名后我们会收到 crt 文件，即证书。 注意：CSR 并不是证书。而是向权威证书颁发机构获得签名证书的申请。CSR 的主要作用是 CA 会利用 CSR 文件进行签名使得攻击者无法伪装或篡改原有证书 把 CSR 交给权威证书颁发机构, 权威证书颁发机构对此进行签名, 完成。保留好 CSR, 当权威证书颁发机构颁发的证书过期的时候, 你还可以用同样的 CSR 来申请新的证书, key 保持不变。

在 CSR 中，比较重要的字段是 `Common Name`（服务器一般会校验这个字段）, 另外在 `Kubernetes RBAC` 体系对证书的鉴权中，用 `Common Name` 表示用户名，`Organization` 表示用户组，因为证书一经签发后，所有的字段都是无法被伪造的，这样拿来做权限管控实在太合适不过了。

### 数字证书常见标准

X509是数字证书的基本规范，而P7和P12则是两个实现规范，P7用于数字信封，P12则是带有私钥的证书实现规范。

* 符合PKI ITU-T X509标准，传统标准（.DER .PEM .CER .CRT）
* PKCS系列标准
    * 符合PKCS#7 加密消息语法标准(.P7B .P7C .SPC .P7R)
    * 符合PKCS#10 证书请求标准(.p10)
    * 符合PKCS#12 个人信息交换标准（.pfx *.p12）
        - P12是把证书压成一个文件，xxx.pfx 。主要是考虑分发证书，私钥是要绝对保密的，不能随便以文本方式散播。所以P7格式不适合分发。.pfx中可以加密码保护，所以相对安全些。

### 文件格式

* PEM：Privacy Enhanced Mail
纯文本数据，看到的是一串可见的字符串，一般用于分发公钥，通常以 .crt，.cer，.key 为文件后缀。
查看PEM格式证书的信息：`openssl x509 -in certificate.pem -text -noout`
查看内容，以”—–BEGIN…”开头，以”—–END…”结尾。Apache和*NIX服务器偏向于使用这种编码格式。

* DER：Distinguished Encoding Rules
二进制文件，可以以 .cer 为文件后缀。
查看DER格式证书的信息：`openssl x509 -in certificate.der -inform der -text -noout`
Java和Windows服务器偏向于使用这种编码格式。

### 文件后缀

文件的内容和后缀没有必然的关系，但是一般使用这些后缀来表示这是什么文件。
* JKS：Java Key Store(JKS)。
* CSR：证书请求文件(Certificate Signing Request)。
    这个并不是证书，而是向权威证书颁发机构获得签名证书的申请，其核心内容是一个公钥(当然还附带了一些别的个人信息)。查看的办法：`openssl req -noout -text -in my.csr`
    - PEM格式：`openssl req -in my.csr -text -noout`
    - DER格式：`openssl req -in my.csr -inform der -text -noout`
* CER：一般指使用DER格式的证书。
    - 查看DER格式证书的信息：`openssl x509 -in my.der -inform der -text -noout`
* CRT：证书文件。可以是PEM格式。
    - 查看PEM格式证书的信息：`openssl x509 -in my.pem -text -noout`
* KEY：通常用来存放一个公钥或者私钥。
    查看KEY的办法（这是使用RSA算法生成的key这么查看，DSA算法生成的使用dsa参数）：
    - PEM格式：`openssl rsa -in my.key -text -noout`
    - PEM格式：`openssl rsa -in my.key -inform der -text -noout`

## 签发 X509 证书
证书签发的过程，如下图，包含了 CA 的生成和 Server 证书的生成两个步骤：

![X509 sign]({{ site.baseurl }}/image/post/2020/11/30/X509/X509-sign.png)

在生成CA证书的过程中，有两种方式：
* 生成 CA私钥 => 生成 CA证书，生成证书的时候需要填写相关信息或者以`-`参数的形式输入

### 范例
一般在我们实际内部项目中，大都是使用自签证书，ca.key 由自己生成和保管，使用 ca.key 签发下一层级的证书。下面我们就来操作下证书生成及签发这一过程。
自建证书体系包含了，CA 根证书、客户端证书及服务端证书。最终的存储目录如下：
```
conf
├── ca.key  -- caroot 的私钥
├── ca.csr  -- caroot 的请求文件
├── ca.pem  -- caroot 的根证书，也可以以 .crt 结尾
├── client
│   ├── client.csr  -- client 请求签名文件
│   ├── client.key  -- client 的私钥
│   └── client.pem  -- client 的证书（由 caroot 签发），也可以以 .crt 结尾
└── server
    ├── server.csr
    ├── server.key
    └── server.pem
```

#### 1. 生成 caroot 的私钥
可使用 openssl rsa -in ca.key -text -noout 查看具体信息：
```
openssl genrsa -out ca.key 2048
```

#### 2. 生成 caroot 的根证书，有两种方式

##### 2.1 第一种方式，直接生成根证书，在生成的过程中输入所需信息
```
# 可使用 openssl x509 -in my.pem -text -noout 查看具体信息：
openssl req -new -x509 -new -key ca.key -days 28885 -out ca.pem
```
##### 2.2 第二种方式，先生成csr请求文件，再生成根证书

```
# csr请求文件，可使用 openssl req -in ca.csr -text -noout 查看具体信息
openssl req -new -key ca.key -out ca.csr
# caroot 的根证书，可使用 openssl x509 -in my.pem -text -noout 查看具体信息：
openssl x509 -req -in ca.csr -signkey ca.key -days 28885 -out ca.pem
```

生成 caroot 请求文件的具体参数备忘： 
```
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) []:CN
State or Province Name (full name) []:ZheJiang
Locality Name (eg, city) []:HangZhou
Organization Name (eg, company) []:My Root CA
Organizational Unit Name (eg, section) []:Bob.Zhu
Common Name (eg, fully qualified host name) []:Mynetty Root CA
Email Address []:0haizhu0@gmail.com

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:
```

#### 3. 将根证书导出为P12格式

```
openssl pkcs12 -export -clcerts -in ca.pem -inkey ca.key -out ca.p12 -password pass:mynetty-root-ca
```
将p12文件导出为base64字符串，这样可配置到surge等软件中：

```
base64 ca.p12 >> ca.p12.text
```



#### 生成服务端证书
以网易云音乐为例：

##### 第一步：生成服务端私钥

```
openssl genrsa -out netease-server.key 1024
```

##### 第二步：生成服务端公钥
一般情况下应该用不到这个公钥
```
openssl rsa -in netease-server.key -pubout -out netease-server-public.pem
```

##### 第三步：生成服务端请求文件

```
openssl req -new -key netease-server.key -out netease-server.csr
```
生成 Netease 请求文件的具体参数备忘： 
```
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) []:CN
State or Province Name (full name) []:ZheJiang
Locality Name (eg, city) []:HangZhou
Organization Name (eg, company) []:163.com
Organizational Unit Name (eg, section) []:Netease Music
Common Name (eg, fully qualified host name) []:*.music.163.com
Email Address []:0haizhu0@gmail.com

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:
```

##### 第四步：生成服务端证书
```
openssl x509 -req -CA ca.pem -CAkey ca.key -CAcreateserial -in netease-server.csr -days 28885 -out netease-server.pem
```


## 参考资料

* [证书（Certificate）的那些事](https://pandaychen.github.io/2019/07/24/auth/)
* [openssl 自建ca，颁发客户端证书](https://blog.csdn.net/do_bset_yourself/article/details/78156161)
* [自制CA证书，自制客户端、服务端证书](https://www.jianshu.com/p/ba0964c41b50)
