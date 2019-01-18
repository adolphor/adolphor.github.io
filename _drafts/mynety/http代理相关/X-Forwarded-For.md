
客户端不管是通过代理服务器，还是直接访问后端服务器对于最终的结果是没有区别的，也就是说大多数情况下客户端根本不关心它访问的到底是什么，只需要（准确快速地）拿到想要的信息就够了。但是有时候，我们还是希望知道请求到底在中间经历了哪些代理，比如用来调试网络异常，或者做数据统计，而 HTTP 协议也提供了响应的功能。

虽然 RFC 2616 定义了 Via 头部字段来跟踪 HTTP 请求经过的代理路径，但在实际中用的更多的还是 X-Forwarded-For 字段，X-Forwarded-For 是 Squid 缓存代理服务软件引入的，目前已经在规范化在 RFC 7239 文档。

X-Forwarded-For 头部格式也比较简单，比如某个服务器接受到请求的对应头部可能是：

```
X-Forwarded-For: client, proxy1, proxy2
```

对应的值有多个字段，每个字段代表中间的一个节点，它们之间由逗号和空格隔开，从左到右距离当前节点越来越近。

每个代理服务器会在 X-Forwarded-For 头部填上前一个节点的 ip 地址，这个地址可以通过 TCP 请求的 remote address 获取。为什么每个代理服务器不填写自己的 ip 地址呢？有两个原因，如果由代理服务器填写自己的 ip 地址，那么代理可以很简单地伪造这个地址，而上一个节点的 remote address 是根据 TCP 连接获取的（如果不建立正确的 TCP 连接是无法进行 HTTP 通信的）；另外一个原因是如果由当前节点填写 X-Forwarded-For，那么很多情况客户端无法判断自己是否会通过代理的。


NOTE：

* 最终客户端或者服务器端接受的请求，X-Forwarded-For 是没有最邻近节点的 ip 地址的，而这个地址可以通过 remote address 获取
* 每个节点（不管是客户端、代理服务器、真实服务器）都可以随便更改 X-Forwarded-For 的值，因此这个字段只能作为参考

参考：
* [X-Forwarded-For](https://en.wikipedia.org/wiki/X-Forwarded-For)
* [HTTP 代理原理和实现](http://cizixs.com/2017/03/21/http-proxy-and-golang-implementation/)


