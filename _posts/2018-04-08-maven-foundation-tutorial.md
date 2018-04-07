---
layout:     post
title:      Maven 相关基本操作
date:       2018-04-08 00:44:06 +0800
postId:     2018-04-08-00-44-06
categories: [blog]
tags:       [Maven]
geneMenu:   true
excerpt:    Maven 相关基本操作
---

## 声明周期

有了生命周期，就可以将各种maven插件的执行时间绑定在某个生命节点上。

### clean生命周期

1. pre-clean    ：执行清理前的工作；
2. clean    ：清理上一次构建生成的所有文件；
3. post-clean    ：执行清理后的工作

### default生命周期

1. validate
2. initialize
3. generate-sources
4. process-sources
5. generate-resources
6. process-resources    ：复制和处理资源文件到target目录，准备打包；
7. compile    ：编译项目的源代码；
8. process-classes
9. generate-test-sources
10. process-test-sources
11. generate-test-resources
12. process-test-resources
13. test-compile    ：编译测试源代码；
14. process-test-classes
15. test    ：运行测试代码；
16. prepare-package
17. package    ：打包成jar或者war或者其他格式的分发包；
18. pre-integration-test
19. integration-test
20. post-integration-test
21. verify
22. install    ：将打好的包安装到本地仓库，供其他项目使用；
23. deploy    ：将打好的包安装到远程仓库，供其他项目使用；

### site生命周期

1. pre-site
2. site    ：生成项目的站点文档；
3. post-site
4. site-deploy    ：发布生成的站点文档

## 参考资料

* [Maven入门指南⑦：Maven的生命周期和插件](https://www.cnblogs.com/luotaoyeah/p/3819001.html)

