
# wireshark

wireshark不能抓取本地回环请求数据……

```shell
tcp && host adolphor.com
```

# postman

```shell
https://adolphor.com/js/main.js?version=20180129
http://127.0.0.1:8080/
http://mynetylan.adolphor.com:8080/
```

# terminal

```
wget -e “http_proxy=127.0.0.1:9999″ 
curl -x 127.0.0.1:9999
```

# chrome

SwitchyOmega 过滤如下网址不走代理，以免影响测试数据

```log
127.0.0.1
[::1]
localhost
api.x.xmt.cn
*.google.com
*.googleapis.com
*.google-analytics.com
*.github.com
*.baidu.com
*.disqus.com
*.gstatic.com
*.qbox.me
0.1.0.1
```

# 测试地址

* http://jekyllthemes.org/assets/css/syntax.css
* https://adolphor.com/js/main.js?version=20180129





