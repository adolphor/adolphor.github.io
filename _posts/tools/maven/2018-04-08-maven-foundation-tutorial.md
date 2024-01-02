---
layout:     post
title:      Maven 相关基本操作
date:       2018-04-08 00:44:06 +0800
postId:     2018-04-08-00-44-06
categories: [Maven]
keywords:   [Tools, Maven]
---

## 基本操作

### 版本管理

* 更改版本

```
mvn versions:set -DnewVersion=0.0.1-SNAPSHOT
```

### JAR依赖树结构

控制台查看：

```
# 基本信息
mvn dependency:tree

# 冲突信息
mvn dependency:tree -Dverbose
```

导出到文本：

```
# 基本信息
mvn dependency:tree > tree.txt

# 冲突信息
mvn dependency:tree -Dverbose > tree.txt
```

### 打包源码

需要在项目自己的pom.xml文件中配置，父类的配置不生效

```xml

<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-source-plugin</artifactId>
  <executions>
    <execution>
      <id>attach-sources</id>
      <phase>verify</phase>
      <goals>
        <goal>jar-no-fork</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

## 生命周期

有了生命周期，就可以将各种maven插件的执行时间绑定在某个生命节点上。

### clean生命周期

1. pre-clean ：执行清理前的工作；
2. clean ：清理上一次构建生成的所有文件；
3. post-clean ：执行清理后的工作

### default生命周期

1. validate
2. initialize
3. generate-sources
4. process-sources
5. generate-resources
6. process-resources ：复制和处理资源文件到target目录，准备打包；
7. compile ：编译项目的源代码；
8. process-classes
9. generate-test-sources
10. process-test-sources
11. generate-test-resources
12. process-test-resources
13. test-compile ：编译测试源代码；
14. process-test-classes
15. test ：运行测试代码；
16. prepare-package
17. package ：打包成jar或者war或者其他格式的分发包；
18. pre-integration-test
19. integration-test
20. post-integration-test
21. verify
22. install ：将打好的包安装到本地仓库，供其他项目使用；
23. deploy ：将打好的包安装到远程仓库，供其他项目使用；

### site生命周期

1. pre-site
2. site ：生成项目的站点文档；
3. post-site
4. site-deploy ：发布生成的站点文档

## 推送到仓库

详情查看：[Maven deploy 上传jar包到私服的方法及其配置文件]({% post_url tools/maven/2021-09-18-02-maven-deploy-private-jar %})

### 推送maven项目

先配置好 `settings-adolphor.xml` 配置文件：
配置多个profile：jdk-1.8, adolphor-nexus, rdc-nexus，
上传的时候通过 `-P` 参数指定使用哪个profile即可：

```shell
# 启用 adolphor，停用 rdc
mvn deploy -D skipTests --settings /Users/adolphor/.m2/settings-adolphor.xml -P adolphor,!rdc
# 停用 adolphor，启用 rdc
mvn deploy -D skipTests --settings /Users/adolphor/.m2/settings-adolphor.xml -P !adolphor,rdc
```
profile范例：

```xml
<profile>
  <id>aliyun-repository</id>
  <properties>
    <altReleaseDeploymentRepository>
      aliyun-repository-releases::default::https://packages.aliyun.com/maven/repository/release-test/
    </altReleaseDeploymentRepository>
    <altSnapshotDeploymentRepository>
      aliyun-repository-snapshots::default::https://packages.aliyun.com/maven/repository/snapshot-test/
    </altSnapshotDeploymentRepository>
  </properties>
  <repositories>
    <repository>
      <id>aliyun-repository-releases</id>
      <url>https://packages.aliyun.com/maven/repository/release-test/</url>
      <releases>
        <enabled>true</enabled>
      </releases>
      <snapshots>
        <enabled>false</enabled>
      </snapshots>
    </repository>
    <repository>
      <id>aliyun-repository-snapshots</id>
      <url>https://packages.aliyun.com/maven/repository/snapshot-test/</url>
      <releases>
        <enabled>false</enabled>
      </releases>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
    </repository>
  </repositories>
</profile>
```


### 推送普通jar包

```
mvn install:install-file -DgroupId=com.wlwx -DartifactId=wlwx-sms-sdk -Dversion=1.1.0 -Dpackaging=jar -Dfile=/Users/adolphor/Downloads/wlwx-sms-sdk-1.1.0.jar
mvn   deploy:deploy-file -DgroupId=com.wlwx -DartifactId=wlwx-sms-sdk -Dversion=1.1.0 -Dpackaging=jar -Dfile=/Users/adolphor/Downloads/wlwx-sms-sdk-1.1.0.jar -Durl=http://maven.adolphor.github.io/repository/maven-releases/ -DrepositoryId=adolphor-releases

groupId=com.adolphor.android
artifactId=link-library
version=1.0-SNAPSHOT
packaging=aar
file=/Users/adolphor/Downloads/temp/adolphorlibrary-release.aar
mvn deploy:deploy-file -DgroupId=$groupId -DartifactId=$artifactId -Dversion=$version -Dpackaging=$packaging -Dfile=$file -Durl=http://maven.adolphor.github.io/repository/maven-snapshots/ -DrepositoryId=adolphor-snapshots
```

### 剔除某个module
如果一个项目有多个module，有些module不希望推送到仓库，那么可以使用如下插件，在推送的时候过滤掉不需要推送的module：
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-deploy-plugin</artifactId>
    <version>2.8.2</version>
    <configuration>
        <skip>true</skip>
    </configuration>
</plugin>
```

## 参考资料

* [Maven 相关基本操作]({% post_url tools/maven/2018-04-08-maven-foundation-tutorial %})
* [Maven入门指南⑦：Maven的生命周期和插件](https://www.cnblogs.com/luotaoyeah/p/3819001.html)
* [druid 1.2.6 依赖 openjdk 的问题]({% post_url tools/maven/2021-09-02-01-maven-druid-openjdk %})
* [Maven deploy 上传jar包到私服的方法及其配置文件]({% post_url tools/maven/2021-09-18-02-maven-deploy-private-jar %})
