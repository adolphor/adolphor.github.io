---
layout:     post
title:      Windows 10 x64 下编译 Hadoop 源码
date:       2016-11-01 11:05:46 +0800
postId:     2016-11-01-11-05-46
categories: [blog]
tags:       [Hadoop]
geneMenu:   true
excerpt:    Windows 10 x64 下编译 Hadoop 源码
---

## 环境准备
Hadoop并没有提供官方的 Windows 10 下的安装包，所以需要自己手动来编译，官方文档中 `BUILDING.txt`
文件中说明了编译源码需要的软件环境：

* Hadoop源码
* Windows 系统
* JDK 1.6+
* Maven 3.0 or later
* Findbugs 1.3.9 (可省略)
* ProtocolBuffer 2.5.0
* CMake 2.6 or newer
* Windows SDK or Visual Studio 2010 Professional
* Cygwin: 为了使用 sh, mkdir, rm, cp, tar, gzip 等命令
* zlib
* 电脑需要联网

### Hadoop源码
使用git或者直接下载压缩包，地址：`https://github.com/apache/hadoop/releases/tag/rel%2Frelease-2.6.5`，
下载之后解压到C盘根目录并重命名为 `dfs265`，防止目录名称过长或者路径中包含空格。

### JDK

{% highlight shell %}
JAVA_HOME=JDK目录
CLASSPATH=.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\jre\lib;
PATH=%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;
{% endhighlight %}

注意：Hadoop2.6.5最好使用JDK1.7进行编译，使用1.8的时候，会有问题，我使用的版本如下：

```
$ java -version
java version "1.7.0_51"
Java(TM) SE Runtime Environment (build 1.7.0_51-b13)
Java HotSpot(TM) 64-Bit Server VM (build 24.51-b03, mixed mode)
```

### Maven
下载地址：`http://maven.apache.org/download.cgi`，配置如下：

{% highlight shell %}
M2_HOME=maven目录
PATH=%M2_HOME%\bin;
{% endhighlight %}

我使用的版本是3.3.9：

```
$ mvn -version
Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-11T00:41:47+08:00)
Maven home: C:\java\maven\bin\..
Java version: 1.7.0_51, vendor: Oracle Corporation
Java home: C:\java\JDK\jdk170\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 8", version: "6.2", arch: "amd64", family: "windows"
```

可以看出，Maven将Windows10识别成了 windows 8，这里应该会对编译时候系统的识别造成影响，visual studio那里再处理。

### ProtocolBuffer
下载地址：`https://github.com/google/protobuf/releases/tag/v2.5.0`，选择 `protoc-2.5.0-win32.zip` 版本,
解压到安装目录，并添加到PAHT，如下：
{% highlight shell %}
PATH=C:\java\protoc-2.5.0-win32;
{% endhighlight %}

测试：
```
$ protoc --version
libprotoc 2.5.0
```

### CMake
要求 CMake 2.6+ 的版本，下载地址：`https://cmake.org/download/`，
下载并解压到安装目录，并添加bin目录到PATH，由于cygwin中也带了个cmake，
所以在Path中，cmake 的bin目录得加在cygwin的bin 目录之前：

{% highlight shell %}
CMAKE_HOME=cmake目录
PATH=%CMAKE_HOME%\bin;
{% endhighlight %}

测试：
```
$ cmake --version
cmake version 3.7.0-rc2
CMake suite maintained and supported by Kitware (kitware.com/cmake).
```

### Visual Studio
VS的版本选择有两种，第一使用推荐的VS2010版，使用这个版本比较简单方便，或者使用最新的VS2015版，需要升级一些文件。
我选择的是 Visual Studio Community 2015 with Update 3 – Free 的 IOS 版本，
地址：`https://www.visualstudio.com/downloads/`。
上面maven安装的时候，maven将系统识别为了windows 8，所以安装 visual studio 2015的时候，需要一并安装 SDK 8.1。
安装完毕之后，如果使用的是VS2015版本需要升级相关的文件：

* C:\dfs265\hadoop-common-project\hadoop-common\src\main\native\native.sln
* C:\dfs265\hadoop-common-project\hadoop-common\src\main\winutils\winutils.sln

使用VS2015分别打开上述文件，提示升级的时候进行确认即可。升级成功的提示信息如下：

```
正在升级项目“native”...
	配置“Release|x64”: 将平台工具集更改为“v140”(之前为“v100”)。
重定目标结束: 1 个已完成，0 个未通过，0 个已跳过

正在升级项目“winutils”...
	配置“Debug|x64”: 将平台工具集更改为“v140”(之前为“v100”)。 
	配置“Release|x64”: 将平台工具集更改为“v140”(之前为“v100”)。
正在升级项目“libwinutils”...
	配置“Debug|x64”: 将平台工具集更改为“v140”(之前为“v100”)。 
	配置“Release|x64”: 将平台工具集更改为“v140”(之前为“v100”)。
重定目标结束: 2 个已完成，0 个未通过，0 个已跳过
```

另外需要修改下面的文件：

* C:\dfs265\hadoop-hdfs-project\hadoop-hdfs\pom.xml

将 “Visual Studio 10” 改成 “Visual Studio 14”，保存后退出。

### Cygwin

下载地址：`https://cygwin.com/install.html`，安装并添加bin目录到PAHT：
{% highlight shell %}
CYGWIN_HOME=cygwin安装目录
PATH=%CYGWIN_HOME%\bin;
{% endhighlight %}

### Zlib

好像不安装Zlib也是可以编译成功，但还是安装下吧。可以网上找编译好的版本，也可以自己从源码编译。
下载地址忘记了，自己搜下别人编译好的windows可用即可：

{% highlight shell %}
ZLIB_HOME=zlib目录
PATH=%ZLIB_HOME%\bin;
{% endhighlight %}

### 其它设置

上面的设置好之后，还需要将如下配置增加到环境变量：

{% highlight shell %}
Platform=x64
VCTargetsPath=C:\Program Files (x86)\MSBuild\Microsoft.Cpp\v4.0\V140
MSBUILD_HOME=C:\Program Files (x86)\MSBuild\14.0\Bin
PATH=%MSBUILD_HOME%;
{% endhighlight %}


## 编译

一般指令如下所示：

{% highlight shell %}
mvn clean package -Pdist,native-win -DskipTests -Dtar
{% endhighlight %}

指令中加入 `-Dmaven.javadoc.skip=true` 参数，可以剔除指令文档的编译，如果使用的是JDK1.8，那么必须加入此参数才能编译通过：
{% highlight shell %}
mvn clean package -Pdist,native-win -DskipTests -Dtar -Dmaven.javadoc.skip=true
{% endhighlight %}

编译成功大概需要半小时，机器好的话可能更快，如果省略说明文档的编译也会提高速度，
编译成功之后的目录为 `C:\dfs265\hadoop-dist\target`，成功信息显示如下：

```
[INFO] Executing tasks
main:
     [exec] $ tar cf hadoop-2.6.5.tar hadoop-2.6.5
     [exec] $ gzip -f hadoop-2.6.5.tar
     [exec]
     [exec] Hadoop dist tar available at: C:\dfs265\hadoop-dist\target/hadoop-2.6.5.tar.gz
     [exec]
[INFO] Executed tasks
[INFO]
[INFO] --- maven-javadoc-plugin:2.8.1:jar (module-javadocs) @ hadoop-dist ---
[INFO] Building jar: C:\dfs265\hadoop-dist\target\hadoop-dist-2.6.5-javadoc.jar
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO]
[INFO] Apache Hadoop Main ................................. SUCCESS [  5.302 s]
[INFO] Apache Hadoop Build Tools .......................... SUCCESS [  3.245 s]
[INFO] Apache Hadoop Project POM .......................... SUCCESS [  3.383 s]
[INFO] Apache Hadoop Annotations .......................... SUCCESS [  5.051 s]
[INFO] Apache Hadoop Assemblies ........................... SUCCESS [  0.782 s]
[INFO] Apache Hadoop Project Dist POM ..................... SUCCESS [  2.886 s]
[INFO] Apache Hadoop Maven Plugins ........................ SUCCESS [  8.258 s]
[INFO] Apache Hadoop MiniKDC .............................. SUCCESS [ 16.756 s]
[INFO] Apache Hadoop Auth ................................. SUCCESS [ 13.092 s]
[INFO] Apache Hadoop Auth Examples ........................ SUCCESS [  5.844 s]
[INFO] Apache Hadoop Common ............................... SUCCESS [03:30 min]
[INFO] Apache Hadoop NFS .................................. SUCCESS [ 13.650 s]
[INFO] Apache Hadoop KMS .................................. SUCCESS [ 28.320 s]
[INFO] Apache Hadoop Common Project ....................... SUCCESS [  0.095 s]
[INFO] Apache Hadoop HDFS ................................. SUCCESS [05:42 min]
[INFO] Apache Hadoop HttpFS ............................... SUCCESS [ 55.960 s]
[INFO] Apache Hadoop HDFS BookKeeper Journal .............. SUCCESS [ 13.488 s]
[INFO] Apache Hadoop HDFS-NFS ............................. SUCCESS [ 12.245 s]
[INFO] Apache Hadoop HDFS Project ......................... SUCCESS [  0.096 s]
[INFO] hadoop-yarn ........................................ SUCCESS [  0.089 s]
[INFO] hadoop-yarn-api .................................... SUCCESS [03:22 min]
[INFO] hadoop-yarn-common ................................. SUCCESS [01:44 min]
[INFO] hadoop-yarn-server ................................. SUCCESS [  0.105 s]
[INFO] hadoop-yarn-server-common .......................... SUCCESS [ 29.556 s]
[INFO] hadoop-yarn-server-nodemanager ..................... SUCCESS [ 43.315 s]
[INFO] hadoop-yarn-server-web-proxy ....................... SUCCESS [  9.429 s]
[INFO] hadoop-yarn-server-applicationhistoryservice ....... SUCCESS [ 16.351 s]
[INFO] hadoop-yarn-server-resourcemanager ................. SUCCESS [ 58.206 s]
[INFO] hadoop-yarn-server-tests ........................... SUCCESS [ 17.863 s]
[INFO] hadoop-yarn-client ................................. SUCCESS [ 16.329 s]
[INFO] hadoop-yarn-applications ........................... SUCCESS [  0.094 s]
[INFO] hadoop-yarn-applications-distributedshell .......... SUCCESS [  4.064 s]
[INFO] hadoop-yarn-applications-unmanaged-am-launcher ..... SUCCESS [  6.835 s]
[INFO] hadoop-yarn-site ................................... SUCCESS [  0.110 s]
[INFO] hadoop-yarn-registry ............................... SUCCESS [ 14.566 s]
[INFO] hadoop-yarn-project ................................ SUCCESS [  4.299 s]
[INFO] hadoop-mapreduce-client ............................ SUCCESS [  0.355 s]
[INFO] hadoop-mapreduce-client-core ....................... SUCCESS [01:27 min]
[INFO] hadoop-mapreduce-client-common ..................... SUCCESS [ 38.155 s]
[INFO] hadoop-mapreduce-client-shuffle .................... SUCCESS [ 12.238 s]
[INFO] hadoop-mapreduce-client-app ........................ SUCCESS [ 26.710 s]
[INFO] hadoop-mapreduce-client-hs ......................... SUCCESS [ 23.336 s]
[INFO] hadoop-mapreduce-client-jobclient .................. SUCCESS [ 20.261 s]
[INFO] hadoop-mapreduce-client-hs-plugins ................. SUCCESS [  3.353 s]
[INFO] Apache Hadoop MapReduce Examples ................... SUCCESS [ 17.791 s]
[INFO] hadoop-mapreduce ................................... SUCCESS [  2.927 s]
[INFO] Apache Hadoop MapReduce Streaming .................. SUCCESS [ 11.117 s]
[INFO] Apache Hadoop Distributed Copy ..................... SUCCESS [ 22.965 s]
[INFO] Apache Hadoop Archives ............................. SUCCESS [  5.038 s]
[INFO] Apache Hadoop Rumen ................................ SUCCESS [ 13.747 s]
[INFO] Apache Hadoop Gridmix .............................. SUCCESS [ 10.653 s]
[INFO] Apache Hadoop Data Join ............................ SUCCESS [  6.081 s]
[INFO] Apache Hadoop Ant Tasks ............................ SUCCESS [  3.085 s]
[INFO] Apache Hadoop Extras ............................... SUCCESS [  4.128 s]
[INFO] Apache Hadoop Pipes ................................ SUCCESS [  0.074 s]
[INFO] Apache Hadoop OpenStack support .................... SUCCESS [ 14.496 s]
[INFO] Apache Hadoop Amazon Web Services support .......... SUCCESS [ 10.876 s]
[INFO] Apache Hadoop Client ............................... SUCCESS [ 10.715 s]
[INFO] Apache Hadoop Mini-Cluster ......................... SUCCESS [  1.497 s]
[INFO] Apache Hadoop Scheduler Load Simulator ............. SUCCESS [ 15.287 s]
[INFO] Apache Hadoop Tools Dist ........................... SUCCESS [ 10.746 s]
[INFO] Apache Hadoop Tools ................................ SUCCESS [  0.073 s]
[INFO] Apache Hadoop Distribution ......................... SUCCESS [ 55.172 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 28:23 min
[INFO] Finished at: 2016-11-01T11:03:50+08:00
[INFO] Final Memory: 120M/1007M
[INFO] ------------------------------------------------------------------------
```

## 安装配置
参考另外一篇博文：
《[Hadoop，Spark，HBase 开发环境配置]({% post_url 2016-10-28-hadoop-spark-hbase-develop-environment %})》
，需要注意的是需要将sh的文件配置更改为cmd的文件配置。

## 参考资料
* [Building Hadoop 2.6 on 64-bit Windows 7](http://coderearth.org/building-hadoop-26-on-64-bit-windows-7.html)


