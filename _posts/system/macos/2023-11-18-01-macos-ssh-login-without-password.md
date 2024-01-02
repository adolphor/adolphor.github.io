---
layout:     post
title:      【转】mac 免密ssh登陆配置不坑指南
date:       2023-11-18 20:49:14 +0800
postId:     2023-11-18-20-49-14
categories: [macOS]
keywords:   [macOS]
---

每次新建一个服务器，需要使用ssh远程连接的服务器的时候我就会回忆起每次恶心的配置ssh公私钥免密登陆过程。
每次配置都会折腾很多次，一开始不懂公私钥的概念，所以配置上思路出了点问题。如今已经很理解公私钥了，还是被坑。
近来认真一看网上的文章，我发现大部分教程对于mac用户是有问题的。那就是少了一步ssh-add。
本文给出mac设置公私钥免密登录的正确步骤。

## 检查是否已存在公私钥对
在mac上打开terminal。更改目录到~/.ssh目录。list当前目录可以看到所有公私钥文件。
拥有.pub结尾的文件是公钥文件，无.pub的是私钥文件。

```shell
cd ~/.ssh  
# 更换目录到 ~/.ssh
ls             
# 查看当前目录公私钥文件
```

## 生成公私钥对（可选步骤）
这部适用于上一步list观察之后当前文件夹不存在公私钥对的情况。
如果已经存在公私钥对，可以跳过这一步。

```shell
ssh-keygen  
# 根据交互，输入你想要的名字（默认id_rsa）
# 然后是passphrase，设置为空即可。这样就生成了一对公私钥  
ls                
# 这时候当前目录下会多了一对公私钥对。
```

## 上传公钥到服务器
这一步从本地将公钥文件上传到服务器指定目录。
```shell
ssh-copy-id -i [公钥文件] user@host 
# user是你的ssh的用户，host是服务器地址，这时候还要输入密码。
# 例子：ssh-copy-id -i id_rsa.pub root@111.111.111.111
```

## ssh-add（mac的坑点）
好一般别的系统到第三步就可以免密登陆了，但是mac并不这样。还要调用ssh-add。
```shell
ssh-add -K [你的私钥文件, 就是那个不加.pub结尾的文件] 
# 例如，ssh-add -K id_rsa
```
按照这四个步骤执行就可以免秘登陆远程的linux服务器了。注意以上所有命令都是在本地机子执行。
第三步用到了上传工具ssh-copy-id，这个工具直接讲公钥上传到了服务器，所以简化了使用ssh连接服务器，
再在服务器上注册公钥这个步骤。

## 搞了这么久，到底在干嘛？
ssh是一个远程连接服务器的工具。普通的情况下，用户远程连接的时候需要给出用户的密码。所以每次使用ssh连接都要输入密码。
如果你是一个运维，或者一个喜欢在服务器上直接写代码的程序员你可能经常要重复输入密码。这样子的结果就是很烦。

公私钥是一个加密学的技术。原理我们跳过，只要知道这歌技术很安全，很难破解。我们可以在服务器上丢一个公钥，
然后本地只要有与之对应的私钥。就可以不需要密码验证就能登录服务器。这样就免去了每次输入密码的痛苦。
而且这样登录相对使用密码登录更加安全

## 其他
如果出现如下错误
```
'The -K and -A flags are deprecated and have been replaced
by the --apple-use-keychain and --apple-load-keychain
flags, respectively. To suppress this warning, set the
environment variable APPLE_SSH_ADD_BEHAVIOR as described in
the ssh-add(1) manual page. '
```
`-K`在可能弃用了，尝试一下`--apple-use-keychain`。

## 参考资料
* [mac 免密ssh登陆配置不坑指南]({% post_url system/macos/2023-11-18-01-macos-ssh-login-without-password %})
* [mac 免密ssh登陆配置不坑指南](https://zhuanlan.zhihu.com/p/32279976)

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/11/18/01/xxx.png)
```
