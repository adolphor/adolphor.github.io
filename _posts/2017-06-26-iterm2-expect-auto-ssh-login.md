---
layout:     post
title:      iterm2 配合 expect 实现 SSH 自动登陆
date:       2017-06-26 10:20:54 +0800
postId:     2017-06-26-10-20-54
categories: [blog]
tags:       [shell]
geneMenu:   true
excerpt:    iterm2 配合 expect 实现 SSH 自动登陆
---

在macOS中，文件传输工具使用小卡车，但是远程连接的shell没有找到好用的工具，
在知道了`expect`和`iterm2`之后，发现可以结合两者，实现自动输入密码快捷
登录远程服务器的功能，废话少说，配置开始。

# 安装expect
使用HomeBrew的安装方式，首先确保此管理工具已经安装：

{% highlight shell %}
brew --version
{% endhighlight %}


```
Homebrew 1.2.3
Homebrew/homebrew-core (git revision c851b; last commit 2017-06-25)
```

没有安装的话参考其他资料安装吧，下面使用HomeBrew安装expect：

{% highlight shell %}
brew install expect
{% endhighlight %}


```
==> Downloading https://homebrew.bintray.com/bottles/expect-5.45.sierra.bottle.tar.gz
Already downloaded: /Users/adolphor/Library/Caches/Homebrew/expect-5.45.sierra.bottle.tar.gz
==> Pouring expect-5.45.sierra.bottle.tar.gz
==> Using the sandbox
🍺  /usr/local/Cellar/expect/5.45: 48 files, 702.9KB

```

{% highlight shell %}
expect -version
{% endhighlight %}

```
expect version 5.45
```    

# 安装iterm2

官网下载：`http://iterm2.com`，解压之后拖至应用程序即可。

# 配置自动登录

## expect脚本
在`/usr/local/bin`目录新建`iterm2login.sh`文件，以及内容参考如下：

{% highlight shell %}
cd /usr/local/bin
vim iterm2login.sh
{% endhighlight %}


```
#!/usr/bin/expect

set timeout 30
spawn ssh -p [lindex $argv 0] [lindex $argv 1]@[lindex $argv 2]
expect {
        "(yes/no)?"
        {send "yes\n";exp_continue}
        "password:"
        {send "[lindex $argv 3]\n"}
}
interact
```
{% highlight shell %}
chmod +x iterm2login.sh
{% endhighlight %}


## 配置itrem2

添加profile，配置如下：
```
# iterm2login.sh 端口号 用户 IP地址 密码
iterm2login.sh 22 root 192.168.0.2 admin
```

![iterm2login](/image/post/2017/06/26/20170626-0101-iterm2login.png)


# 使用

想要登陆的时候，选择profile即可自动登录了，现在的问题是不能改变title，当同时打开
多个窗口的时候，不能够明显分辨出是哪个服务器，后续优化吧。