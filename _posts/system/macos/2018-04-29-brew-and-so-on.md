---
layout:     post
title:      brew 使用相关
date:       2018-04-29 20:58:37 +0800
postId:     2018-04-29-20-58-37
categories: [macOS]
keywords:   [brew]
---
## 安装



## 一般应用

```bash
# 检查过时应用
brew outdated
# 升级
brew update && brew upgrade
# 清除安装包
brew cleanup --prune=10
```

## 可视化应用

```bash
# 首先确保本命令执行过
brew tap buo/cask-upgrade
# 检查过时
brew cask outdated
# 升级
brew cu -a -y --cleanup
# 清除安装包
brew cask cleanup --outdated
```




## 参考资料

* [Github - cask-upgrade-workflow](https://github.com/NotAlexNoyle/cask-upgrade-workflow)

