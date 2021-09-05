---
layout:     post
title:      spring boot 和 mybatis 中配置druid多数据源
date:       2021-09-05 15:29:56 +0800
postId:     2021-09-05-15-29-56
categories: [framework]
keywords:   [Spring, Mybatis]
---

Spring boot 和 Mybatis 搭配使用的范例已经很多了，多数据源的情况也有很多教程范例，但两者搭配
阿里巴巴的druid的资料不多，本文将搭建过程的关键步骤进行备份存档，以备使用。


## 项目结构
主要是将java代码中的dao/mapper层拆分为多数据源，然后对应的配置文件中mapper目录下的文件拆分为多数据源：

```
|- java
    |- controller
    |- service
        |- impl
    |- dao
        |- db1
            |- Db1TestDao.java
        |- db2
            |- Db2TestDao.java
    |- entity
|- resources
    |- mapper
        |- db1
            |- Db1TestDao.xml
        |- db2
            |- Db2TestDao.xml
```

## 配置数据源

因为后面手动配置了多数据源，所以App启动类需要排除 `DataSourceAutoConfiguration.class` 的自动加载

> SpringBootDemoApplication.java

```java
@SpringBootApplication(
    scanBasePackages = {"com.example.**"},
    exclude = {DataSourceAutoConfiguration.class}
)
public class DataScmApplication {
  public static void main(String[] args) {
    SpringApplication.run(DataScmApplication.class, args);
  }
}
```


> Db1Config.java

```java
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/3 下午4:04
 */
@Configuration
@MapperScan(basePackages = Db1Config.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactoryDb1")
public class Db1Config {

    // 这个路径是 标记了@Repository 注解的 mapper 所在目录，不同数据库的mapper放在不同路径下，这样就可以自动关联绑定不同数据源
    static final String PACKAGE = "com.example.dao.db1";
    // 同理，将xml配置文件也分开存放
    static final String MAPPER_LOCATION = "classpath*:mapper/db1/*.xml";

    // 应为使用了druid，所以需要手动获取相关参数信息
    @Value("${multi-datasource.db1.url}")
    private String url;
    @Value("${multi-datasource.db1.username}")
    private String user;
    @Value("${multi-datasource.db1.password}")
    private String password;
    @Value("${multi-datasource.db1.driver-class-name}")
    private String driverClass;

    @Primary
    @Bean(name = "dataSourceDb1")
    public DataSource dataSourceDb1() {
        // druid实例也需要手动创建
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }
    @Bean
    public SqlSessionFactory sqlSessionFactoryDb1() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceDb1());
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return factoryBean.getObject();
    }
    @Bean
    public SqlSessionTemplate sqlSessionTemplateDb1() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactoryDb1());
    }
}
```

> Db2Config.java

```java

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/3 下午4:04
 */
@Configuration
@MapperScan(basePackages = Db2Config.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactoryDb2")
public class Db2Config {

    static final String PACKAGE = "com.example.dao.db2";
    static final String MAPPER_LOCATION = "classpath*:mapper/db2/*.xml";

    @Value("${multi-datasource.db2.url}")
    private String url;
    @Value("${multi-datasource.db2.username}")
    private String user;
    @Value("${multi-datasource.db2.password}")
    private String password;
    @Value("${multi-datasource.db2.driver-class-name}")
    private String driverClass;

    @Primary
    @Bean(name = "dataSourceDb2")
    public DataSource dataSourceDb2() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }
    @Bean
    public SqlSessionFactory sqlSessionFactoryDb2() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceDb2());
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return factoryBean.getObject();
    }
    @Bean
    public SqlSessionTemplate sqlSessionTemplateDb2() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactoryDb2());
    }
}
```

```yaml
multi-datasource:
  db1:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/adolphor?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&allowMultiQueries=true
    username: root
    password: Adolphor!@#123
  db2:
    driver-class-name: oracle.jdbc.driver.OracleDriver
    url: jdbc:oracle:thin:@localhost:1521:xe
    username: system
    password: oracle
```

## 扫描配置

1. spring 的扫描策略是只要再App.java目录下标记了 `@Configuration` 的都会自动扫描
2. 但是配置了多数据源之后，需要排除 `DataSourceAutoConfiguration.class` 的自动加载
3. 对于 `@Repository` 注解，spring扫描后不会自动加载，需要通过 `@MapperScan` 指定扫描路径才会被加载

## 参考资料
* [spring boot 和 mybatis 中配置多数据源]({% post_url framework/spring/2021-09-05-01-spring-boot-druid-multi-datasource %})
