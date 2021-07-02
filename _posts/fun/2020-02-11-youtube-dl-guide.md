---
layout:     post
title:      youtube-dl的使用
date:       2020-02-11 17:28:27 +0800
postId:     2020-02-11-17-28-27
categories: []
tags:       [其他,ffmpeg]
geneMenu:   true
excerpt:    youtube-dl的使用
---


```bash
# 查看可以下载的格式列表
youtube-dl -F url
# 下载指定的格式
youtube-dl 
# 下载质量最好的m4a格式音频
youtube-dl -f 'bestaudio[ext=m4a]' url
# 下载质量最好的mp4视频以及质量最好的m4a格式音频，并合并
youtube-dl -f 'bestvideo[ext=mp4]+bestaudio[ext=m4a]/mp4' url
```



## 参考资料

* [test](test.html)