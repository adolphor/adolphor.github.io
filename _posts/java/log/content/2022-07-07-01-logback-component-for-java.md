---
layout:     post
title:      Java项目logback日志组件范例
date:       2022-07-07 10:12:06 +0800
postId:     2022-07-07-10-12-06
categories: [log]
keywords:   [Java, log]
---

Java项目logback快速搭建范例

## pom文件配置
```xml
<project>
    <properties>
        <slf4j.version>1.7.33</slf4j.version>
        <logback.version>1.2.10</logback.version>
        <lombok.version>1.18.20</lombok.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-core</artifactId>
            <version>${logback.version}</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>${logback.version}</version>
        </dependency>
    </dependencies>
</project>
```

## 配置范例

> logback.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">

    <springProperty scope="context" name="LOG_PATH" source="logging.path"/>
    <property name="APP_NAME" value="my-project" />
    <property name="HOST_NAME" value="${HOSTNAME:-UNKNOWN}"/>
    <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
    <property name="PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} ${APP_NAME} ${HOST_NAME} [%mdc{traceId} %thread] %-5level %logger{50} -%line- %msg%n"/>
    <!--定义日志文件的存储地址 ，这个使用的是相对路径，即在日志文件存放在项目根路径logs文件夹下-->
    <property name="LOG_HOME" value="${LOG_PATH}/${APP_NAME}"/>
    
    <!-- 控制台输出 -->
    <appender name="ConsoleTrace" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${PATTERN}</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>TRACE</level>
        </filter>
    </appender>
    <appender name="ConsoleWarn" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${PATTERN}</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>WARN</level>
        </filter>
    </appender>
    <appender name="FileDebug" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>DEBUG</level>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/debug-%d{yyyyMMdd}-%i.log</fileNamePattern>
            <maxHistory>10</maxHistory>
            <maxFileSize>100MB</maxFileSize>
            <totalSizeCap>1GB</totalSizeCap>
            <cleanHistoryOnStart>true</cleanHistoryOnStart>
        </rollingPolicy>
        <append>true</append>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <charset>utf-8</charset>
            <pattern>${PATTERN}</pattern>
        </encoder>
    </appender>
    <appender name="FileWarn" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>WARN</level>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/warn-%d{yyyyMMdd}-%i.log</fileNamePattern>
            <maxHistory>10</maxHistory>
            <maxFileSize>100MB</maxFileSize>
            <totalSizeCap>1GB</totalSizeCap>
            <cleanHistoryOnStart>true</cleanHistoryOnStart>
        </rollingPolicy>
        <append>true</append>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <charset>utf-8</charset>
            <pattern>${PATTERN}</pattern>
        </encoder>
    </appender>
    <appender name="FileError" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/error-%d{yyyyMMdd}-%i.log</fileNamePattern>
            <maxHistory>10</maxHistory>
            <maxFileSize>100MB</maxFileSize>
            <totalSizeCap>1GB</totalSizeCap>
            <cleanHistoryOnStart>true</cleanHistoryOnStart>
        </rollingPolicy>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${LOG_HOME}/%d{yyyy-MM-dd}.%i.error.log</FileNamePattern>
            <MaxHistory>30</MaxHistory>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <append>true</append>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <charset>utf-8</charset>
            <pattern>${PATTERN}</pattern>
        </encoder>
    </appender>

    <!-- spring-cloud项目可以使用这个配置 -->
    <if condition='isDefined("LOG_PATH")'>
        <then>
            <root level="DEBUG">
                <!-- 当且仅当开发环境 执行输出到控制台 -->
                <springProfile name="local,dev">
                    <appender-ref ref="ConsoleTrace" />
                </springProfile>
                <springProfile name="test,beta">
                    <appender-ref ref="FileDebug" />
                    <appender-ref ref="FileWarn" />
                    <appender-ref ref="FileError" />
                    <appender-ref ref="ConsoleTrace" />
                </springProfile>
                <springProfile name="prod,official">
                    <appender-ref ref="FileWarn" />
                    <appender-ref ref="FileError" />
                    <appender-ref ref="ConsoleWarn" />
                </springProfile>
            </root>
        </then>
        <else>
            <root level="DEBUG">
                <appender-ref ref="BaseConsole" />
            </root>
        </else>
    </if>

    <!-- 常规项目可以使用这个配置 -->
    <root level="DEBUG">
        <appender-ref ref="ConsoleTrace"/>
        <appender-ref ref="FileDebug"/>
        <appender-ref ref="FileWarn"/>
        <appender-ref ref="FileError"/>
    </root>
</configuration>
```

## 参考资料
* [Java项目logback日志组件]({% post_url java/log/content/2022-07-07-01-logback-component-for-java %})
