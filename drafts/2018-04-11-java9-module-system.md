---
layout:     post
title:      Java9 模块化
date:       2018-04-11 15:25:59 +0800
postId:     2018-04-11-15-25-59
categories: [blog]
tags:       [Java,JVM]
geneMenu:   true
excerpt:    Java9 模块化
---

## jlink 

### 增加 module-info 信息

虽然项目编译的时候可以使用 automatic 特性来引入未模块化的jar包，但jlink时如果含有未模块化的jar的时候还是会报错。
报错的原因是automatic引入的模块具有访问classpath目录的权限，而如果jlink的时候不进行限制，那么就无法判定需要哪些包。
wield解决这个问题，需要将这个jar包加入 module-info.java 信息，这个信息可以使用jdeps工具进行生成。
比如 `commons-net-3.6.jar`，这个版本没有模块化，可以使用如下指令进行信息的生成：

```Shell
cd ~/.m2/repository/commons-net/commons-net/3.6
jdeps --module-path $JAVA_HOME/jmods \
      --add-modules=ALL-MODULE-PATH \
      --generate-module-info out commons-net-3.6.jar

# 结果
writing to out/commons.net/module-info.java
```

生成module信息之后可以使用Maven的[moditect](https://github.com/moditect/moditect)插件将生成的信息打包到 JRE，
配置如下信息之后，执行 `mvn clean package` 即可：
```Shell
<plugin>
  <groupId>org.moditect</groupId>
  <artifactId>moditect-maven-plugin</artifactId>
  <version>1.0.0.Beta1</version>
  <executions>
    <execution>
      <id>add-module-infos</id>
      <phase>generate-resources</phase>
      <goals>
        <goal>add-module-info</goal>
      </goals>
      <configuration>
        <outputDirectory>${project.build.directory}/modules</outputDirectory>
        <modules>
          <module>
            <artifact>
              <groupId>commons-net</groupId>
              <artifactId>commons-net</artifactId>
            </artifact>
            <moduleInfoSource>
              module commons.net {
                exports org.apache.commons.net;
                exports org.apache.commons.net.bsd;
                exports org.apache.commons.net.chargen;
                exports org.apache.commons.net.daytime;
                exports org.apache.commons.net.discard;
                exports org.apache.commons.net.echo;
                exports org.apache.commons.net.finger;
                exports org.apache.commons.net.ftp;
                exports org.apache.commons.net.ftp.parser;
                exports org.apache.commons.net.imap;
                exports org.apache.commons.net.io;
                exports org.apache.commons.net.nntp;
                exports org.apache.commons.net.ntp;
                exports org.apache.commons.net.pop3;
                exports org.apache.commons.net.smtp;
                exports org.apache.commons.net.telnet;
                exports org.apache.commons.net.tftp;
                exports org.apache.commons.net.time;
                exports org.apache.commons.net.util;
                exports org.apache.commons.net.whois;
              }
            </moduleInfoSource>
          </module>
          <!--<module>-->
            <!--...-->
          <!--</module>-->
        </modules>
      </configuration>
    </execution>
  </executions>
</plugin>
```
### 生成 JRE



## 参考资料

* [The State of the Module System](http://openjdk.java.net/projects/jigsaw/spec/sotms/#automatic-modules)
* [JEP 261: Module System](http://openjdk.java.net/jeps/261)
* [Jlink and automatic module](http://mail.openjdk.java.net/pipermail/jigsaw-dev/2016-July/008559.html)
* [JDK9: Howto Create a Java Run-Time Image With Maven?](http://blog.soebes.de/blog/2017/06/06/howto-create-a-java-run-time-image-with-maven/)
* [Github - moditect](https://github.com/moditect/moditect)