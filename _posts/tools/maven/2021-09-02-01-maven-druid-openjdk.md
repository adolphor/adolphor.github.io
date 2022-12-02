---
layout:     post
title:      druid 1.2.6 依赖 openjdk 的问题
date:       2021-09-02 14:56:45 +0800
postId:     2021-09-02-14-56-45
categories: [Tools]
keywords:   [Maven]
---

如果项目中引用了 `com.alibaba.druid-spring-boot-starter` 这个jar包的 `1.2.6` 版本，
那么编译的时候会遇到找不到openJDK的问题，这是因为再druid的pom描述文件中，引用了本地定义的
openjdk路径，导致编译报错，具体原因和解决方案如下。

## 报错信息
```
$ mvn clean source:jar install -D skipTests
[ERROR] Failed to execute goal on project cloud-common: Could not resolve dependencies for project com.adolphor:cloud-common:jar:0.0.2-SNAPSHOT: The following artifacts could not be resolved: com.sun:tools:jar:1.8, com.sun:jconsole:jar:1.8: Could not find artifact com.sun:tools:jar:1.8 at specified path /Users/adolphor/.m2/repository/com/alibaba/druid/1.2.6/lib/openjdk-1.8-tools.jar -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/DependencyResolutionException
[ERROR] 
[ERROR] After correcting the problems, you can resume the build with the command
[ERROR]   mvn <args> -rf :cloud-common
```

## 原因分析
进入 `~/.m2/repository/com/alibaba/druid/1.2.6` 目录，找到 `druid-1.2.6.pom` 描述文件，
可以看到如下内容：
```xml
<dependency>
    <groupId>com.sun</groupId>
    <artifactId>tools</artifactId>
    <version>1.8</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/openjdk-1.8-tools.jar</systemPath>
</dependency>
<dependency>
    <groupId>com.sun</groupId>
    <artifactId>jconsole</artifactId>
    <version>1.8</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/openjdk-1.8-jconsole.jar</systemPath>
</dependency>
```
可以看到，pom中使用了自定义的本地环境的 openjdk 包，其他人找不到这个jar包导致编译报错。

## 解决方案
原因是在定义的目录中找不到需要的jar包，那么解决方案有如下三种，采用任何一种都可以解决这个问题。


### 方法1：剔除openjdk依赖声明
将druid的tools依赖剔除掉：
```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>${alibaba.druid.version}</version>
    <exclusions>
        <exclusion>
            <groupId>com.sun</groupId>
            <artifactId>tools</artifactId>
        </exclusion>
        <exclusion>
            <groupId>com.sun</groupId>
            <artifactId>jconsole</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### 方法2：重新定义本地环境的openjdk依赖
在自己项目的 `pom.xml` 中添加jar包路径声明：
```xml
<dependency>
    <groupId>com.sun</groupId>
    <artifactId>tools</artifactId>
    <version>1.8</version>
    <scope>system</scope>
    <systemPath>${env.JAVA_HOME}/lib/tools.jar</systemPath>
</dependency>
<dependency>
    <groupId>com.sun</groupId>
    <artifactId>jconsole</artifactId>
    <version>1.8</version>
    <scope>system</scope>
    <systemPath>${env.JAVA_HOME}/lib/jconsole.jar</systemPath>
</dependency>
```
### 方法3：将jar包拷贝至所需目录
另外一种方式，就是将所需要的 openjdk 文件放在所需的位置即可，通过上面的报错信息可知，保证文件
`~/.m2/repository/com/alibaba/druid/1.2.6/lib/openjdk-1.8-tools.jar` 存在即可：

```
cd ~/.m2/repository/com/alibaba/druid/1.2.6/
mkdir lib
cp --path $(/usr/libexec/java_home -v 1.8)/lib/tools.jar lib/openjdk-1.8-tools.jar
cp --path $(/usr/libexec/java_home -v 1.8)/lib/jconsole.jar lib/openjdk-1.8-jconsole.jar
```

## 参考资料
* [druid 1.2.6 依赖 openjdk 的问题]({% post_url tools/maven/2021-09-02-01-maven-druid-openjdk %})
