---
layout:     post
title:      使用 jrecreate 工具创建自己的 JRE
date:       2018-04-10 20:56:12 +0800
postId:     2018-04-10-20-56-13
categories: [article]
tags:       [Java,JVM]
geneMenu:   true
excerpt:    使用 jrecreate 工具创建自己的 JRE
---

Done：Bullshit！不能用来精简JRE，GAME OVER ~

本章讲述如何使用 jrecreate 工具创建自己的 Java 运行环境，包括如下内容：

* [运行 jrecreate](#Running-jrecreate)
* [jrecreate 命令语法](#jrecreate-Command——Syntax)
* [jrecreate 参数选项](#jrecreate-Options)
* [jrecreate 命令范例](#jrecreate-Command-Examples)
* [jrecreate 命令输出结果](#jrecreate-Command-Output)
* [JRE 文件夹](#JRE-Directories)
* [配置 Swing/AWT 版本的 JRE](#Configuring-the-JRE-for-Swing-AWT)

## 运行 jrecreate  {#Running-jrecreate}

你需要在电脑上运行 jrecreate，然后将生成的 JRE 拷贝到需要集成的目标设备。

注意：jrecreate 运行的时候需要将近 1G 的可用内存。

## jrecreate 命令语法 {#jrecreate-Command——Syntax}

```Shell
jrecreate --dest host-destination-directory [options]
```
## jrecreate 参数选项 {#jrecreate-Options}

这些参数都是可选项，但有一个参数必须进行特殊指定 —— 目标文件夹，也就是生成的 JRE 保存在什么地方。

注意：请注意，所有可选项的长格式之前是双连字符（短格式之前是单连字符）。

另外：打包 ARM Swing/AWT 的时候，有些可选参数暂不支持，
详见[Configuring the JRE for Swing/AWT Headful Applications](#Configuring-the-JRE-for-Swing-AWT)

```Shell
-h
--help
```
帮助：查看 jrecreate 的语法和支持的参数

```Shell
-d path
--dest path
```
文件夹：必选项，指定写入 JRE 镜像和关联文件的位置，且此文件夹当前不存在。

```Shell
-p profile
--profile profile
```
配置文件：默认，全部 JRE API。

可选项有三个：

- compact1 (大约12MB)
- compact2 (17MB)
- compact3 (24M)
- 默认     (50MB)


```Shell
compact1                     compact2                    compact3
--------------------------   -----------------------     --------------------------
java.io                      java.rmi                    java.lang.instrument
java.lang                    java.rmi.activation         java.lang.management
java.lang.annotation         java.rmi.registry           java.security.acl
java.lang.invoke             java.rmi.server             java.util.prefs
java.lang.ref                java.sql                    javax.annotation.processing
java.lang.reflect            javax.rmi.ssl               javax.lang.model
java.math                    javax.sql                   javax.lang.model.element
java.net                     javax.transaction           javax.lang.model.type
java.nio                     javax.transaction.xa        javax.lang.model.util
java.nio.channels            javax.xml                   javax.management
java.nio.channels.spi        javax.xml.datatype          javax.management.loading
java.nio.charset             javax.xml.namespace         javax.management.modelbean
java.nio.charset.spi         javax.xml.parsers           javax.management.monitor
java.nio.file                javax.xml.stream            javax.management.openmbean
java.nio.file.attribute      javax.xml.stream.events     javax.management.relation
java.nio.file.spi            javax.xml.stream.util       javax.management.remote
java.security                javax.xml.transform         javax.management.remote.rmi
java.security.cert           javax.xml.transform.dom     javax.management.timer
java.security.interfaces     javax.xml.transform.sax     javax.naming
java.security.spec           javax.xml.transform.stax    javax.naming.directory
java.text                    javax.xml.transform.stream  javax.naming.event
java.text.spi                javax.xml.validation        javax.naming.ldap
java.util                    javax.xml.xpath             javax.naming.spi
java.util.concurrent         org.w3c.dom                 javax.script
java.util.concurrent.atomic  org.w3c.dom.bootstrap       javax.security.auth.kerberos
java.util.concurrent.locks   org.w3c.dom.events          javax.security.sasl
java.util.jar                org.w3c.dom.ls              javax.sql.rowset
java.util.logging            org.xml.sax                 javax.sql.rowset.serial
java.util.regex              org.xml.sax.ext             javax.sql.rowset.spi
java.util.spi                org.xml.sax.helpers         javax.tools
java.util.zip                                            javax.xml.crypto
javax.crypto                                             javax.xml.crypto.dom
javax.crypto.interfaces                                  javax.xml.crypto.dsig
javax.crypto.spec                                        javax.xml.crypto.dsig.dom
javax.net                                                javax.xml.crypto.dsig.keyinfo
javax.net.ssl                                            javax.xml.crypto.dsig.spec
javax.security.auth                                      org.ieft.jgss
javax.security.auth.callback
javax.security.auth.login
javax.security.auth.spi
javax.security.auth.x500
javax.security.cert




```


```Shell
--vm jvm
```
VM：可选项，如果 vm 和 profile 都没有指定，那么所有的 JVMs 都会打包到 JRE。

可选项：
    - minimal
    - client
    - server (if available for the target computer)
    - all (all available for the target computer)

如果没有指定 VM 这个参数，那么就会按照指定的 profile 对应的参数来运行：
    - compact1: minimal
    - compact2: minimal
    - compact3: client

```Shell
-x extension [,extension ...]
--extension extension [,extension ...]
```
没有默认值，

添加可选组件到 JRE 的方式有两种：
    - 多个指令
    ```
    --extension sunec \
    --extension sunpkcs11
    ```
    - 单个指令，多个参数
    ```
    --extension sunec,sunpkcs11
    ```

可以被添加的组件有：
    - fx:graphics
    - fx:controls
    - sunec
    - sunpkcs11
    - locales
    - charsets
    - nashorn

```Shell
-g
--debug
```
调试：运行时候对应用的DEBUG支持，默认不支持。

注意：
    - minimal JVM 下不能使用本参数，如果指定本参数，那么就会生成 client JVM。
    - DEBUG 的时候，需要指定 -XX:+UsePerfData 参数，此参数默认是关闭状态。

```Shell
-k
--keep-debug-info
```
调试信息：默认不支持。

```Shell
--no-compression
```
压缩：默认压缩。

```Shell
-n
--dry-run
```
生成报告：默认生成 JRE。使用此命令就会生成报告，而不生成 JRE。

```Shell
-v
--verbose
```
生成JRE的实时日志：默认，否。

```Shell
--ejdk-home path
```
Optional. Default: value of EJDK_HOME environment variable if set; otherwise /ejdk<version>

## jrecreate 命令范例 {#jrecreate-Command-Examples}
### Example 1
Smallest JRE: headless, compact1 profile, minimal JVM (default for compact1).
```Shell
% installDir/ejdk<version>/bin/jrecreate.sh \
--profile compact1 \
--dest /tmp/defaultJRE/
```   
### Example 2
compact2 profile, client JVM with debugging support.
```Shell
% installDir/ejdk<version>/bin/jrecreate.sh \
    --dest /tmp/exampleJRE1/ \
    --profile compact2 \
    --vm client \ 
    --keep-debug-info \
    --debug
```

### Example 3
JavaFX controls and Java SE locales added to server JVM and compact3 profile.  
```Shell
% installDir/ejdk<version>/bin/jrecreate.sh \
    --dest /tmp/exampleJRE2 \
    --profile compact3 \
    --vm server \
    --extension fx:controls \
    --extension locales 
``` 
### Example 4
Full JRE APIs, all JVMs (default).
```Shell
% installDir/ejdk<version>/bin/jrecreate.sh \
    --dest /tmp/exampleJRE3 
```    

## jrecreate 命令输出结果 {#jrecreate-Command-Output}
执行时显示信息如下：
```Shell
Building JRE using options Options {
    ejdk-home: /home/xxxx/ejdk/ejdk<version>
    dest: /tmp/testjre
    target: linux_i586
    vm: minimal
    runtime: compact1 profile
    debug: false
    keep-debug-info: false
    no-compression: false
    dry-run: false
    verbose: false
    extension: []
}
```

## JRE 文件夹 {#JRE-Directories}

指定完毕之后就会在目标文件夹出现如下的文件夹：

* bin/: Target-native commands, minimally including the java JRE application launcher. The complement of tools varies according to the value of the --profile option.
* lib/: The files that make up the core of the JRE, including classes, JVMs, time zone information, and other resources.
* release: A text file that tools can read to obtain attributes of the generated JRE, such as the Java version, profile name (if applicable), operating system name, and CPU architecture.
* bom: A text file that documents how the JRE was created, including the jrecreate command options and the files that the command used.
* COPYRIGHT, LICENSE, README, THIRDPARTYLICENSEREADME.txt: Legal and other documentation. Present only if --profile is not specified (full JRE APIs).

## 配置 Swing/AWT 版本的 JRE {#Configuring-the-JRE-for-Swing-AWT}


## 参考资料

* [Oracle - Create Your JRE with jrecreate](https://docs.oracle.com/javase/8/embedded/develop-apps-platforms/jrecreate.htm)
* [Oracle - Compact Profiles](https://docs.oracle.com/javase/8/docs/technotes/guides/compactprofiles/compactprofiles.html)
* [Oracle - Java™ Platform, Standard Edition Runtime Environment Version 8](http://www.oracle.com/technetwork/java/javase/jre-8-readme-2095710.html)
* [An Introduction to Java 8 Compact Profiles](https://blogs.oracle.com/jtc/an-introduction-to-java-8-compact-profiles)
