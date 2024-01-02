---
layout:     post
title:      Git ignore 配置文件
date:       2022-04-22 10:47:58 +0800
postId:     2022-04-22-10-47-58
categories: [Git]
keywords:   [Tools, Git]
---

Git ignore 配置文件模板

## 参考配置
```
target/
!.mvn/wrapper/maven-wrapper.jar

### STS ###
.apt_generated
.classpath
.factorypath
.settings
.springBeans
.sts4-cache
bin/

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr

### NetBeans ###
/nbproject/private/
/build/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/

### vscode ###
.vscode

### gradle
.gradle
/build/
!gradle/wrapper/gradle-wrapper.jar

### mvn
.mvn

### log
log/

### test ng result ###
test-output/

### macOS
.DS_Store
```

## 参考资料
* [Git ignore 配置文件]({% post_url tools/git/2022-04-22-02-tools-git-ignore-file %})
