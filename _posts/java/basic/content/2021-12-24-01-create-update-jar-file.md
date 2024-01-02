---
layout:     post
title:      打包或更新jar包
date:       2021-12-24 13:45:04 +0800
postId:     2021-12-24-13-45-04
categories: [Java]
keywords:   [Java]
---

对于不知道源码的项目，或者已经打包好，只需要修改少量文件的项目，可以直接操作jar包中的文件。

## 创建jar包
```
jar cvf jar-file input-file(s)
```
### 参数详解
* `c` ：表示创建一个jar文件
* `f` ：option indicates that you want the output to go to a file rather than to stdout.
* `jar-file.jar`：创建的jar文件名，最好指定jar后缀名
* `input-file(s)`：空格分割需要压缩的文件即可

### 可选参数
* `v`：打印日志
* `0`：数字0，表示不压缩
* `M`：Indicates that the default manifest file should not be produced.
* `m`：Used to include manifest information from an existing manifest file：`jar cmf jar-file existing-manifest input-file(s)`
* `-C`：To change directories during execution of the command. See below for an example.

## 解压jar包
```
jar xvf jar-file [archived-file(s)]
```

### 参数详解
* `x`：表示解压
* `f`：indicates that the JAR file from which files are to be extracted is specified on the command line, rather than through stdin.
* `jar-file`：需要解压的文件

### 可选参数
* `archived-file(s)`：consisting of a space-separated list of the files to be extracted from the archive. If this argument is not present, the Jar tool will extract all the files in the archive.

## 更新文件
```
jar uf jar-file input-file(s)
```

### 参数详解
* `u`：option indicates that you want to update an existing JAR file.
* `f`：option indicates that the JAR file to update is specified on the command line.
* `jar-file`：is the existing JAR file that is to be updated.
* `input-file(s)`：is a space-delimited list of one or more files that you want to add to the JAR file.

## 删除文件
直接使用zip命令进行删除操作：
```shell
zip -d jar-file delete-file(s)
```

## 使用范例
将 `cloud-sunyur-interface-prod.jar` 包中的`nacos-1.1.1`替换为`nacos-1.2.1`： 

```shell
# 解压jar包
jar xvf cloud-sunyur-interface-prod.jar

# 查看解压结果
$ ll
total 118272
drwxr-xr-x  4 adolphor  staff   128B Aug 25  2020 BOOT-INF
drwxr-xr-x  4 adolphor  staff   128B Aug 25  2020 META-INF
-rwxr-xr-x@ 1 adolphor  staff    57M Dec 24 09:18 cloud-sunyur-interface-prod.jar
drwxr-xr-x  3 adolphor  staff    96B Aug 25  2020 org

# 然后就可以将需要添加或更新的文件更新到jar包了，要注意：
# 1. 首先需要将更新的文件在解压出来的目录中更新掉
# 2. 然后在运行如下命令更新或新增文件：要注意如果是jar包必须使用 uvf0 而不是 uvf 参数来更新，避免把jar包压缩了，导致jvm不识别
$ jar uvf0 cloud-sunyur-interface-prod.jar BOOT-INF/lib/nacos-api-1.2.1.jar
$ jar uvf0 cloud-sunyur-interface-prod.jar BOOT-INF/lib/nacos-client-1.2.1.jar
$ jar uvf0 cloud-sunyur-interface-prod.jar BOOT-INF/lib/nacos-common-1.2.1.jar

# 使用zip命令删除
$ zip -d cloud-sunyur-interface-prod.jar BOOT-INF/lib/nacos-api-1.1.1.jar
$ zip -d cloud-sunyur-interface-prod.jar BOOT-INF/lib/nacos-client-1.1.1.jar
$ zip -d cloud-sunyur-interface-prod.jar BOOT-INF/lib/nacos-common-1.1.1.jar

# 然后验证是否能够运行即可
$ java -jar cloud-sunyur-interface-prod.jar
```

## 参考资料
* [打包或更新jar包]({% post_url java/basic/content/2021-12-24-01-create-update-jar-file %})
* [Oracle - Creating a JAR File](https://docs.oracle.com/javase/tutorial/deployment/jar/build.html)
* [Oracle - Extracting the Contents of a JAR File](https://docs.oracle.com/javase/tutorial/deployment/jar/unpack.html)
* [Oracle - Updating a JAR File](https://docs.oracle.com/javase/tutorial/deployment/jar/update.html)
