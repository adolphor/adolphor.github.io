---
layout:     post
title:      在K8S集群的CoreDNS中自定义配置域名解析
date:       2023-11-30 11:29:48 +0800
postId:     2023-11-30-11-29-48
categories: [K8S]
keywords:   [K8S]
---

## 配置入口
1. 找到`配置管理`
2. 找到`配置项`
3. 选择命名空间：`kube-system`
4. 找到服务：`coredns`
5. 点击`YAML编辑`

![k8s-coredns]({{ site.baseurl }}/image/post/2023/11/30/01/k8s-coredns.png)

## 具体配置

* 自定义设置特定域名的DNS服务器：`adolphor.dns` 域名以及子域名使用 `192.168.0.125` 服务器进行解析
* 如果匹配到一级域名：则需要在一级域名下配置二级域名的解析规则，比如 `adolphor.dns` 下的 `nacos.adolphor.dns`
* 设置 `nacos.adolphor.dns` 的目的是本地缓存域名对应的IP，减少DNS请求，比如nacos对于域名解析的时效性要求很高，频繁访问可以缓存到本地
* 如果没有配置一级域名，则可以配置在 `.53` 规则下，比如 `www.yahaha.com`

```yaml
apiVersion: v1
kind: ConfigMap
data:
  Corefile: |
    .:53 {
        errors
        health {
           lameduck 15s
        }
        ready

        kubernetes cluster.local in-addr.arpa ip6.arpa {

          pods verified
          fallthrough in-addr.arpa ip6.arpa
        }
        hosts {
          127.0.0.1 www.yahaha.com
          fallthrough
        }
        prometheus :9153
        forward . /etc/resolv.conf
        cache 30
        loop
        reload
        loadbalance
    }
    adolphor.dns:53 {
        errors
        hosts {
          192.168.1.68 nacos.adolphor.dns
          fallthrough
        }
        cache 30
        forward . 192.168.0.125
    }
```

## 重启生效
1. 找到`工作负载`
2. 找到`无状态`
3. 选择命名空间：`kube-system`
4. 找到服务：`coredns`
5. 升级策略配置：滚动升级，不可用Pod数量 `0`，超过期望Pod `1`，最小准备时间 `30`
6. 点击`重新部署`

## 验证
找当前集群下的一个Pod，进入控制台，
```
$ ping nacos.adolphor.dns
PING nacos.adolphor.dns (192.168.1.68) 56(84) bytes of data.
64 bytes from nacos.adolphor.dns (192.168.1.68): icmp_seq=1 ttl=101 time=2.00 ms
64 bytes from nacos.adolphor.dns (192.168.1.68): icmp_seq=2 ttl=101 time=1.95 ms
64 bytes from nacos.adolphor.dns (192.168.1.68): icmp_seq=3 ttl=101 time=1.98 ms
64 bytes from nacos.adolphor.dns (192.168.1.68): icmp_seq=4 ttl=101 time=2.00 ms
64 bytes from nacos.adolphor.dns (192.168.1.68): icmp_seq=5 ttl=101 time=1.99 ms
```


## 参考资料
* [在K8S集群的CoreDNS中自定义配置域名解析]({% post_url micro-service/k8s/2023-11-30-01-k8s-coredns-domain-configuration %})

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/11/30/01/xxx.png)
```
