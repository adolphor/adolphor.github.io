---
layout:     post
title:      maven编译可运行jar包
date:       2023-11-27 14:23:35 +0800
postId:     2023-11-27-14-23-35
categories: [Maven]
keywords:   [Tools,Maven]
---

可运行jar的两种打包方式：
* assembly：将依赖的jar打包到一个jar中
* dependency：将依赖的jar放到一个目录下，然后在MANIFEST.MF中指定classpath

## maven-assembly-plugin
```xml
<build>
<plugins>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
  </plugin>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.2.2</version>
    <configuration>
      <archive>
        <manifest>
          <addClasspath>true</addClasspath>
          <mainClass>${main.class}</mainClass>
        </manifest>
      </archive>
    </configuration>
  </plugin>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <executions>
      <execution>
        <phase>package</phase>
        <goals>
          <goal>single</goal>
        </goals>
      </execution>
    </executions>
    <configuration>
      <archive>
        <manifest>
          <addClasspath>true</addClasspath>
          <mainClass>${main.class}</mainClass>
        </manifest>
      </archive>
      <descriptorRefs>
        <descriptorRef>jar-with-dependencies</descriptorRef>
      </descriptorRefs>
    </configuration>
  </plugin>
</plugins>
</build>
```

## maven-dependency-plugin
```xml
<build>
<plugins>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
  </plugin>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.2.2</version>
    <configuration>
      <archive>
        <manifest>
          <addClasspath>true</addClasspath>
          <mainClass>${main.class}</mainClass>
          <classpathPrefix>dependency-jars/</classpathPrefix>
        </manifest>
      </archive>
    </configuration>
  </plugin>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>2.5.1</version>
    <executions>
      <execution>
        <id>copy-dependencies</id>
        <phase>package</phase>
        <goals>
          <goal>copy-dependencies</goal>
        </goals>
        <configuration>
          <includeScope>runtime</includeScope>
          <outputDirectory>${project.build.directory}/dependency-jars/</outputDirectory>
        </configuration>
      </execution>
    </executions>
  </plugin>
</plugins>
</build>
```

## 参考资料
* [maven编译可运行jar包]({% post_url tools/maven/2023-11-27-02-runnable-jar-build-by-maven %})

开始写作吧
```
![image-alter]({{ site.baseurl }}/image/post/2023/11/27/02/xxx.png)
```
