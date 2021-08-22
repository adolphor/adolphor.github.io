---
layout:     post
title:      ffmpeg的使用
date:       2020-02-11 17:28:01 +0800
postId:     2020-02-11-17-28-01
categories: []
keywords:   [其他,ffmpeg]
---

## 批量处理

### macOS 批量处理
```
ffmpeg -i /Users/adolphor/Downloads/temp/视频.mov -vf scale=1920:1080 视频scale.mp4
ffmpeg -i /Users/adolphor/Downloads/temp/视频.mov -c:v libx264 -crf 30 -c:a aac -vf scale=1920:1080 视频scale.mp4
find ./ -name '*.mp4' -exec sh -c 'ffmpeg -i "$0" -c:v libx264 -crf 30 -c:a aac -vf scale=1920:1080 "${0%%.mp4}.small.mp4"' {} \;
```

### Windows 批量处理
```
for %%i in (*.mp4) do ffmpeg -i "%%i" -c:v libx264 -crf 30 -c:a aac -vf scale=1920:1080 "%%i.small.mp4"
```

## 参考资料

* [test](test.html)
