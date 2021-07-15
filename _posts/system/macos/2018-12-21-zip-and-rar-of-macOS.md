---
layout:     post
title:      macOS 压缩和解压缩
date:       2018-12-21 11:23:51 +0800
postId:     2018-12-21-11-23-51
categories: []
keywords:   [Tools]
---

## tar

* 解压 
    - tar -zxvf 文件名.tar.gz  

* 压缩 
    - tar -zcvf 文件名.tar.gz 目标名  
    - tar -zcvf 文件名 -C 父目录名(绝对路径，后面可以跟/) 目录名(后面可以跟/)  

范例：
```
tar -zcvf jdk160_38.tar.gz -C /mnt/software/ jdk1.6.0_38/
```


## 参考资料


* [Mac OS分卷压缩包怎么解压 苹果系统分卷解压方法，macos](http://www.bkjia.com/macjc/1269317.html)
