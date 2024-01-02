---
layout:     post
title:      Git分支、版本管理和项目发布
date:       2023-03-20 09:40:44 +0800
postId:     2023-03-20-09-40-44
categories: [Git]
keywords:   [Tools, Git]
---

Git分支管理和项目发布流程的流程经验，后续持续优化吧。

## 理想开发环境和分支管理
* 本地环境：对应feat分支
* 开发环境：对应dev分支
* 测试环境：对应test分支
* 正式环境：对应master分支

## 当前实践
* 按照功能模块或者迭代计划创建开发分支
* 开发完毕后，合并到test分支进行测试环境的发布测试，有BUG的话在test分支修复后再合并到test发布测试，直至所有问题解决
* 测试环境测试完成后，优化提交记录，通过rebase或者square等方式合并，然后合并到master分支
* 在master分支创建release标签，测试环境重新部署当前标签版本，进行正式环境发版前的最终验证，保证测试环境和正式环境发布验证的是同一个版本
* 正式环境发布验证release版本

## 参考资料
* [Git分支、版本管理和项目发布]({% post_url tools/git/2023-03-20-01-git-branches-and-project-version-to-deploy %})

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/03/20/01/xxx.jpg)
```
