---
layout:     post
title:      Maven deploy 上传jar包到私服
date:       2021-09-18 16:56:20 +0800
postId:     2021-09-18-16-56-20
categories: [Tools]
keywords:   [maven]
---

如果搭建了maven私服，那么需要将项目基础脚手架，或者依赖项目上传到私服，供其他人下载使用，
备份一下配置，后续快速复用~

## settings.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <mirrors>
        <mirror>
            <id>mirror</id>
            <mirrorOf>central,jcenter,!rdc-releases,!rdc-snapshots</mirrorOf>
            <name>mirror</name>
            <url>https://maven.aliyun.com/nexus/content/groups/public</url>
        </mirror>
    </mirrors>
    <servers>
        <server>
            <id>rdc-releases</id>
            <username>username</username>
            <password>password</password>
        </server>
        <server>
            <id>rdc-snapshots</id>
            <username>username</username>
            <password>password</password>
        </server>
        <server>
            <id>adolphor-releases</id>
            <username>username</username>
            <password>password</password>
        </server>
        <server>
            <id>adolphor-snapshots</id>
            <username>username</username>
            <password>password</password>
        </server>
    </servers>
    <profiles>
        <profile>
            <id>rdc</id>
            <properties>
                <altReleaseDeploymentRepository>
                    rdc-releases::default::https://packages.aliyun.com/maven/repository/80808-release/
                </altReleaseDeploymentRepository>
                <altSnapshotDeploymentRepository>
                    rdc-snapshots::default::https://packages.aliyun.com/maven/repository/80808-snapshot/
                </altSnapshotDeploymentRepository>
            </properties>
            <repositories>
                <repository>
                    <id>central</id>
                    <url>https://maven.aliyun.com/nexus/content/groups/public</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </repository>
                <repository>
                    <id>snapshots</id>
                    <url>https://maven.aliyun.com/nexus/content/groups/public</url>
                    <releases>
                        <enabled>false</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </repository>
                <repository>
                    <id>rdc-releases</id>
                    <url>https://packages.aliyun.com/maven/repository/80808-release/</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </repository>
                <repository>
                    <id>rdc-snapshots</id>
                    <url>https://packages.aliyun.com/maven/repository/80808-snapshot/</url>
                    <releases>
                        <enabled>false</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <id>central</id>
                    <url>https://maven.aliyun.com/nexus/content/groups/public</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </pluginRepository>
                <pluginRepository>
                    <id>snapshots</id>
                    <url>https://maven.aliyun.com/nexus/content/groups/public</url>
                    <releases>
                        <enabled>false</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </pluginRepository>
                <pluginRepository>
                    <id>rdc-releases</id>
                    <url>https://packages.aliyun.com/maven/repository/80808-release/</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </pluginRepository>
                <pluginRepository>
                    <id>rdc-snapshots</id>
                    <url>https://packages.aliyun.com/maven/repository/80808-snapshot/</url>
                    <releases>
                        <enabled>false</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </pluginRepository>
            </pluginRepositories>
        </profile>
        <profile>
            <id>adolphor</id>
            <properties>
                <altReleaseDeploymentRepository>
                    adolphor-releases::default::http://maven.dev.adolphor.com/repository/maven-releases/
                </altReleaseDeploymentRepository>
                <altSnapshotDeploymentRepository>
                    adolphor-snapshots::default::http://maven.dev.adolphor.com/repository/maven-snapshots/
                </altSnapshotDeploymentRepository>
            </properties>
            <repositories>
                <repository>
                    <id>adolphor-releases</id>
                    <url>http://maven.dev.adolphor.com/repository/maven-releases/</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </repository>
                <repository>
                    <id>adolphor-snapshots</id>
                    <url>http://maven.dev.adolphor.com/repository/maven-snapshots/</url>
                    <releases>
                        <enabled>false</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <id>adolphor-releases</id>
                    <url>http://maven.dev.adolphor.com/repository/maven-releases/</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </pluginRepository>
                <pluginRepository>
                    <id>adolphor-snapshots</id>
                    <url>http://maven.dev.adolphor.com/repository/maven-snapshots/</url>
                    <releases>
                        <enabled>false</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </pluginRepository>
            </pluginRepositories>
        </profile>
    </profiles>
    <activeProfiles>
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
可以将命令行方式，因为上面 `settings.xml` 配置了两个profile，所以指定使用哪个profile即可
```
mvn clean deploy -D skipTests -P adolphor
```

可以将这个指令配置成快捷指令：
```
alias mvnadodpl='mvn clean deploy -D skipTests -P adolphor'
alias mvnalidpl='mvn clean deploy -D skipTests -P rdc'
```

如果没有配置profile，可以直接配置全部的url地址信息，但不好的地方在于，还需要区分release和snapshots
```
mvn clean deploy -D skipTests -D altDeploymentRepository=adolphor-releases::default::http://maven.dev.adolphor.com/repository/maven-snapshots/
```

## 独立文件
只能使用命令行方式，指令如下：
```
mvn deploy:deploy-file -DgroupId=com.adolphor.cloud \
                       -DartifactId=cloud-sunyur-interface \
                       -Dversion=2.0-SNAPSHOT \
                       -Dpacckaging=jar \
                       -Dfile=cloud-sunyur-interface-prod.jar \
                       -DrepositoryId=adolphor-snapshots \
                       -Durl=http://maven.dev.adolphor.com/repository/maven-snapshots/
```

## 参考资料
* [Maven deploy 上传jar包到私服]({% post_url tools/maven/2021-09-18-02-maven-deploy-private-jar %})
