---
layout:     post
title:      Maven deploy 上传jar包到私服的方法及其配置文件
date:       2021-09-18 16:56:20 +0800
postId:     2021-09-18-16-56-20
categories: [Maven]
keywords:   [Tools,Maven]
---

如果搭建了maven私服，那么需要将项目基础脚手架，或者依赖项目上传到私服，供其他人下载使用，
备份一下配置，后续快速复用~

## settings.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <!-- 参考：https://cloud.tencent.com/developer/article/1799571 -->
  <mirrors>
    <mirror>
      <id>aliyun-nexus</id>
      <mirrorOf>central,jcenter,spring-milestone,spring-snapshot</mirrorOf>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    </mirror>
  </mirrors>

  <!-- 账号信息 -->
  <servers>
    <server>
      <!-- 对应 profile 中 adolphor 下的 adolphor-repository 仓库下载权限 -->
      <id>adolphor-repository</id>
      <username>longkui</username>
      <password>4kP8N66j</password>
    </server>

    <server>
      <!-- 对应 profile 中 adolphor 下的 altReleaseDeploymentRepository deploy上传权限 -->
      <id>adolphor-releases</id>
      <username>longkui</username>
      <password>4kP8N66j</password>
    </server>
    <server>
      <!-- 对应 profile 中 adolphor 下的 altReleaseDeploymentRepository deploy上传权限 -->
      <id>adolphor-snapshots</id>
      <username>longkui</username>
      <password>4kP8N66j</password>
    </server>

    <server>
      <id>rdc-releases</id>
      <username>610b40e0fa25fa3e2495c919</username>
      <password>ibJ54gNN8wpu</password>
    </server>
    <server>
      <id>rdc-snapshots</id>
      <username>610b40e0fa25fa3e2495c919</username>
      <password>ibJ54gNN8wpu</password>
    </server>
  </servers>

  <profiles>
    <!-- 配置JDK -->
    <profile>
      <id>jdk8</id>
      <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
      </properties>
      <repositories>
        <repository>
          <id>jdk1.8</id>
          <name>Repository for JDK 1.8 builds</name>
          <url>/Library/Java/JavaVirtualMachines/jdk1.8.0_291.jdk/Contents/Home</url>
          <layout>default</layout>
        </repository>
      </repositories>
    </profile>
    <profile>
      <id>adolphor</id>
      <properties>
        <altReleaseDeploymentRepository>
          adolphor-releases::default::http://maven.adolphor.github.io/repository/maven-releases/
        </altReleaseDeploymentRepository>
        <altSnapshotDeploymentRepository>
          adolphor-snapshots::default::http://maven.adolphor.github.io/repository/maven-snapshots/
        </altSnapshotDeploymentRepository>
      </properties>
      <repositories>
        <repository>
          <id>adolphor-releases</id>
          <url>http://maven.adolphor.github.io/repository/maven-releases/</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
        <repository>
          <id>adolphor-snapshots</id>
          <url>http://maven.adolphor.github.io/repository/maven-snapshots/</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
    </profile>
    <profile>
      <id>rdc</id>
      <properties>
        <altReleaseDeploymentRepository>
          rdc-releases::default::https://packages.aliyun.com/maven/repository/2126419-release/
        </altReleaseDeploymentRepository>
        <altSnapshotDeploymentRepository>
          rdc-snapshots::default::https://packages.aliyun.com/maven/repository/2126419-snapshot/
        </altSnapshotDeploymentRepository>
      </properties>
      <repositories>
        <repository>
          <id>rdc-releases</id>
          <url>https://packages.aliyun.com/maven/repository/2126419-release/</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
        </repository>
        <repository>
          <id>rdc-snapshots</id>
          <url>https://packages.aliyun.com/maven/repository/2126419-snapshot/</url>
          <releases>
            <enabled>false</enabled>
          </releases>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>jdk8</activeProfile>
    <activeProfile>adolphor</activeProfile>
    <activeProfile>rdc</activeProfile>
  </activeProfiles>
</settings>
```

## 源码编译
使用有两种方式，第一种方式是在项目的 `pom.xml` 中指定需要上传的仓库地址，但弊端是
只能配置一个私服地址，而且如果泄露源码，那么会会泄露私服地址。

### pom.xml
```xml
<distributionManagement>
    <repository>
        <id>rdc-releases</id>
        <name>Nros Release Repository</name>
        <url>https://packages.aliyun.com/maven/repository/80808-release/</url>
    </repository>
    <snapshotRepository>
        <id>rdc-snapshots</id>
        <name>Nros Snapshot Repository</name>
        <url>https://packages.aliyun.com/maven/repository/80808-snapshot/</url>
    </snapshotRepository>
</distributionManagement>
```

### 命令行
可以将命令行方式，因为上面 `settings.xml` 配置了两个profile，所以指定使用哪个profile即可，可以将这个指令配置成快捷指令：
```
# 启用 adolphor，停用 rdc
mvn deploy -D skipTests --settings /Users/adolphor/.m2/settings.adolphor.xml -P adolphor,!rdc
# 停用 adolphor，启用 rdc
mvn deploy -D skipTests --settings /Users/adolphor/.m2/settings.adolphor.xml -P !adolphor,rdc
```

如果没有配置profile，可以直接配置全部的url地址信息，但不好的地方在于，还需要区分release和snapshots
```
mvn clean deploy -D skipTests -D altDeploymentRepository=adolphor-releases::default::http://maven.adolphor.github.io/repository/maven-snapshots/
```

## 独立文件

对于非pom项目或者已经编译好的jar包，只能使用命令行方式，指令如下：
```
mvn deploy:deploy-file -DgroupId=com.adolphor.cloud \
                       -DartifactId=cloud-sunyur-interface \
                       -Dversion=2.0-SNAPSHOT \
                       -Dpacckaging=jar \
                       -Dfile=cloud-sunyur-interface-prod.jar \
                       -DrepositoryId=adolphor-snapshots \
                       -Durl=http://maven.adolphor.github.io/repository/maven-snapshots/
mvn deploy:deploy-file -DgroupId=com.adolphor.cloud \
                       -DartifactId=cloud-sunyur-interface \
                       -Dversion=2.2 \
                       -Dpacckaging=jar \
                       -Dfile=cloud-sunyur-interface-prod.jar \
                       -DrepositoryId=adolphor-release \
                       -Durl=http://maven.adolphor.github.io/repository/maven-release/
```

## 参考资料
* [Maven deploy 上传jar包到私服]({% post_url tools/maven/2021-09-18-02-maven-deploy-private-jar %})
* [聊聊项目打包发布到maven私仓常见的几种方式](https://cloud.tencent.com/developer/article/1799571)
