---
layout:     post
title:      Mac科学上网
date:       2018-03-23 11:37:37 +0800
postId:     2018-03-23-11-37-37
categories: [article]
tags:       [网络]
geneMenu:   true
excerpt:    科学上网
---

## 安装

```
$ brew cask search shadowsocks
$ brew cask install shadowsocksx-ng
```

## 自定义规则

1. 通配符支持，如 `*.example.com/*` 实际书写时可省略* 如 `.example.com/` 意即 `*.example.com/*`

2. 正则表达式支持，以`\`开始和结束， 如 `\[\w]+:\/\/example.com\`

3. 例外规则 `@@`，如 `@@*.example.com/*` 满足@@后规则的地址不使用代理

4. 匹配地址开始和结尾 `|`，如 `|http://example.com`、`example.com|` 分别表示以 `http://example.com` 开始和以 `example.com` 结束的地址

5. `||` 标记，如 `||example.com` 则 `http://example.com`、`https://example.com`、`ftp://example.com` 等地址均满足条件

6. 注释 `!` 如 `! Comment`

## 参考资料

* [Adblock Plus filters](https://adblockplus.org/en/filter-cheatsheet)
* [user-rule设置方法](https://www.duoluodeyu.com/1337.html)
