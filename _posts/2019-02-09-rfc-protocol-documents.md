---
layout:     post
title:      RFC相关协议整理
date:       2019-02-09 00:56:20 +0800
postId:     2019-02-09-00-56-20
categories: [article]
tags:       [网络]
geneMenu:   true
excerpt:    RFC相关协议整理
---

- <span id="RFC1945">[1996年 - RFC 1945 - Hypertext Transfer Protocol -- HTTP/1.0](https://www.rfc-editor.org/info/rfc1945)</span>
- <span id="RFC2068">[1997年 - RFC 2068 - Hypertext Transfer Protocol -- HTTP/1.1](https://www.rfc-editor.org/info/rfc2068)</span>
    - 已废弃，当前版本协议参考 RFC2616（ —— RFC2616文档中已标注 RFC2068 为 Obsoletes）
    - Obsoleted by:
        - [RFC 2616](#RFC2616)
- <span id="RFC2145">[1997年 - RFC 2145 - Use and Interpretation of HTTP Version Numbers](https://www.rfc-editor.org/info/rfc2145)</span>
    - Obsoleted by:
        - [RFC 7230](#RFC7230)
- <span id="RFC2616">[1999年 - RFC 2616 - Hypertext Transfer Protocol -- HTTP/1.1](https://www.rfc-editor.org/info/rfc2616)</span>
    - 176 pages，当前HTTP协议版本HTTP/1.1的官方规范（ —— HTTP权威指南）
    - Obsoletes:
        - [RFC 2068](#RFC2068)
    - Obsoleted by:
        - [RFC 7230](#RFC7230), [RFC 7231](#RFC7231), [RFC 7232](#RFC7232), [RFC 7233](#RFC7233), [RFC 7234](#RFC7234), [RFC 7235](#RFC7235)
    - Updated by:
        - [RFC 2817](#RFC2817), [RFC 5785](#RFC5785), [RFC 6266](#RFC6266), [RFC 6585](#RFC6585)
- <span id="RFC2617">[1999年 - RFC 2617 - HTTP Authentication: Basic and Digest Access Authentication](https://www.rfc-editor.org/info/rfc2617)</span>
    - Obsoletes:
        - RFC 2069
    - Obsoleted by:
        - [RFC 7235](#RFC7235), [RFC 7615](#RFC7615), [RFC 7616](#RFC7616), [RFC 7617](#RFC7617)
- <span id="RFC2817">[2000年 - RFC 2817 - Upgrading to TLS Within HTTP/1.1](https://www.rfc-editor.org/info/rfc2817)</span>
    - Updates:
        - [RFC 2616](#RFC2616#RFC 2616)
    - Updated by:
        - [RFC 7230](#RFC7230), [RFC 7231](#RFC7231)
- <span id="RFC2818">[2000年 - RFC 2818 - HTTP Over TLS](https://www.rfc-editor.org/info/rfc2818)</span>
    - Updated by:
        - [RFC 5785](#RFC5785), [RFC 7230](#RFC7230)
- <span id="RFC5785">[2010年 - RFC 5785 - Defining Well-Known Uniform Resource Identifiers (URIs)](https://www.rfc-editor.org/info/rfc5785)</span>
    - Updates:
        - [RFC 2616](#RFC2616), [RFC 2818](#RFC2818)
- <span id="RFC6266">[2011年 - RFC 6266 - Use of the Content-Disposition Header Field in the Hypertext Transfer Protocol (HTTP)](https://www.rfc-editor.org/info/rfc6266)</span>
    - Updates:
        - [RFC 2616](#RFC2616)
- <span id="RFC6585">[2012年 - RFC 6585 - This document specifies additional HyperText Transfer Protocol (HTTP) status codes for a variety of common situations.](https://www.rfc-editor.org/info/rfc6585)</span>
    - Updates:
        - [RFC 2616](#RFC2616)
- <span id="RFC7230">[2014年 - RFC 7230 - Hypertext Transfer Protocol (HTTP/1.1): Message Syntax and Routing](https://www.rfc-editor.org/info/rfc7230)</span>
    - Obsoletes:
        - [RFC 2145](#RFC2145), [RFC 2616](#RFC2616)
    - Updates:
        - [RFC 2817](#RFC2817), [RFC 2818](#RFC2818)
- <span id="RFC7231">[2014年 - RFC 7231 - Hypertext Transfer Protocol (HTTP/1.1): Semantics and Content](https://www.rfc-editor.org/info/rfc7231)</span>
    - Obsoletes:
        - [RFC 2616](#RFC2616)
    - Updates:
        - [RFC 2817](#RFC2817)
- <span id="RFC7232">[2014年 - RFC 7232 - Hypertext Transfer Protocol (HTTP/1.1): Conditional Requests](https://www.rfc-editor.org/info/rfc7232)</span>
    - Obsoletes:
        - [RFC 2616](#RFC2616)
- <span id="RFC7233">[2014年 - RFC 7233 - Hypertext Transfer Protocol (HTTP/1.1): Range Requests](https://www.rfc-editor.org/info/rfc7233)</span>
    - Obsoletes:
        - [RFC 2616](#RFC2616)
- <span id="RFC7234">[2014年 - RFC 7234 - Hypertext Transfer Protocol (HTTP/1.1): Caching](https://www.rfc-editor.org/info/rfc7234)</span>
    - Obsoletes:
        - [RFC 2616](#RFC2616)
- <span id="RFC7235">[2014年 - RFC 7235 - Hypertext Transfer Protocol (HTTP/1.1): Authentication](https://www.rfc-editor.org/info/rfc7235)</span>
    - Obsoletes:
        - [RFC 2616](#RFC2616), [RFC 2617](#RFC2617)
- <span id="RFC7540">[2015年 - RFC 7540 - Hypertext Transfer Protocol Version 2 (HTTP/2)](https://www.rfc-editor.org/info/rfc7540)</span>
- <span id="RFC7615">[2015年 - RFC 7615 - HTTP Authentication-Info and Proxy-Authentication-Info Response Header Fields](https://www.rfc-editor.org/info/rfc7615)</span>
    - Obsoletes:
        - [RFC 2617](#RFC2617)
- <span id="RFC7616">[2015年 - RFC 7616 - HTTP Digest Access Authentication](https://www.rfc-editor.org/info/rfc7616)</span>
    - Obsoletes:
        - [RFC 2617](#RFC2617)
- <span id="RFC7617">[2015年 - RFC 7617 - The 'Basic' HTTP Authentication Scheme](https://www.rfc-editor.org/info/rfc7617)</span>
    - Obsoletes:
        - [RFC 2617](#RFC2617)