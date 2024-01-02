---
layout:     post
title:      ffmpeg的使用
date:       2020-02-11 17:28:01 +0800
postId:     2020-02-11-17-28-01
categories: [影音]
keywords:   [影音,ffmpeg]
---

## 生成gif

### 图片转gif
使用ffmpeg将png转为gif：
```shell
# 模糊匹配：增加 -pattern_type glob 参数，匹配非数字开头的图片名称 
# 第一步：生成调色板
ffmpeg -pattern_type glob -i '*.png' -vf palettegen palette.png
# 第二步：在生成动图时使用调色板
ffmpeg  -framerate 1 -pattern_type glob -i '*.png' -i palette.png -lavfi paletteuse out.gif
```

### 视频转gif


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

* [用ffmpeg从多张图片生成动图](https://blog.caomingjun.com/ffmpeg-images-to-gif/#install-npm)
* [Vertically or horizontally stack (mosaic) several videos using ffmpeg?](https://stackoverflow.com/questions/11552565/vertically-or-horizontally-stack-mosaic-several-videos-using-ffmpeg/33764934#33764934)
* [Clarification for ffmpeg input option with image files as input](https://superuser.com/questions/666860/clarification-for-ffmpeg-input-option-with-image-files-as-input)
* [初次尝试：StackOverflow - Create animated gif from a set of jpeg images](https://stackoverflow.com/questions/3688870/create-animated-gif-from-a-set-of-jpeg-images/29542944#29542944)
* [找到 GIF 支持的 pixel format：FFmpeg-user - Find out supported pixel formats for a codec](https://ffmpeg.org/pipermail/ffmpeg-user/2016-August/033261.html)
* [使用调色板解决颜色偏差问题：StackOverflow - colors messed up (distorted) when making a gif from png files using ffmpeg](https://stackoverflow.com/questions/58832085/colors-messed-up-distorted-when-making-a-gif-from-png-files-using-ffmpeg/58832086#58832086)
* [生成概览图：StackOverflow - Vertically or horizontally stack (mosaic) several videos using ffmpeg?](https://stackoverflow.com/questions/11552565/vertically-or-horizontally-stack-mosaic-several-videos-using-ffmpeg/33764934#33764934)
