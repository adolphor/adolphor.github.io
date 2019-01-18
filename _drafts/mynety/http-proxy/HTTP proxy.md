# Http 代理

实现http代理，之后所有消息的流转完整顺序如下：

* 只分析HTTP请求，跟socks代理无关
```
client <--> dst server
```
* 不需要HTTP转发到socks，根据代理规则判断不需要使用socks代理时：
```
client <--> socks5 client <--> dst server
```
* 需要socks代理时：
```
client <--> socks5 client <--> socks5 server <--> dst server
```

# http && https

* 如果是https请求，postman会发送method为connection的请求
* 如果是http请求，postman发送的就是原请求方式

# 参考

* [JAVA写HTTP代理服务器(二)-netty实现](https://www.jianshu.com/p/005acada04e3)
* [TCP 代理服务器浅析](https://zhuanlan.zhihu.com/p/27670502)
* [基于netty的tcp代理工具，可以借助公网服务器，访问内网主机tcp服务](https://github.com/ztgreat/proxy)