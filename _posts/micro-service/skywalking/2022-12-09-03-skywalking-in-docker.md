---
layout:     post
title:      使用Docker在本地部署SkyWalking
date:       2022-12-09 17:19:47 +0800
postId:     2022-12-09-17-19-47
categories: [SkyWalking]
keywords:   [SkyWalking]
---
使用Docker在本地部署SkyWalking，部署流程和脚本备份。

## 准备工作
创建ES数据存储目录并赋权：
```shell
mkdir -p /Users/adolphor/data/elasticsearch7/config
mkdir -p /Users/adolphor/data/elasticsearch7/data
mkdir -p /Users/adolphor/data/elasticsearch7/logs
chmod -R 777 /Users/adolphor/data/elasticsearch7/config
chmod -R 777 /Users/adolphor/data/elasticsearch7/data
chmod -R 777 /Users/adolphor/data/elasticsearch7/logs
```

创建ES配置文件：
```shell
echo "http.host: 0.0.0.0" >> /Users/adolphor/data/elasticsearch7/config/elasticsearch.yml
```

## 构建镜像并启动

### ES7
验证地址：[http://127.0.0.1:9200/](http://127.0.0.1:9200/)
```shell
docker run --name elasticsearch7 \
  --restart=always \
  -e TZ=Asia/Shanghai \
  -e "discovery.type=single-node" \
  -e ES_JAVA_OPTS="-Xms512m -Xmx1024m" \
  -v /Users/adolphor/data/elasticsearch7/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
  -v /Users/adolphor/data/elasticsearch7/data:/usr/share/elasticsearch/data  \
  -v /Users/adolphor/data/elasticsearch7/logs:/usr/share/elasticsearch/logs \
  -v /Users/adolphor/data/elasticsearch7/plugins:/usr/share/elasticsearch/plugins \
  -p 9200:9200 -p 9300:9300  \
  -d elasticsearch:7.17.4
```

### SkyWalking 9
```shell
docker run --name skywalking-oap-server-9.1.0-es7 \
    --restart always \
    -e TZ=Asia/Shanghai \
    --link elasticsearch7:es7 \
    -e SW_STORAGE=elasticsearch \
    -e SW_STORAGE_ES_CLUSTER_NODES=es7:9200 \
    -e SW_NAMESPACE=skywalking \
    -p 11800:11800 -p 12800:12800 \
    -d apache/skywalking-oap-server:9.1.0
```
* 环境变量参数：[skywalking-es-storage](https://skywalking.apache.org/docs/main/v9.2.0/en/setup/backend/backend-storage/#elasticsearch)

### SkyWalking UI
验证地址：[http://127.0.0.1:18080/](http://127.0.0.1:18080/)
```shell
docker run --name skywalking-ui-9-es7 \
    --restart always \
    -e TZ=Asia/Shanghai \
    --link skywalking-oap-server-9.1.0-es7:oap \
    -e SW_OAP_ADDRESS=http://oap:12800 \
    -p 18080:8080 \
    -d apache/skywalking-ui:9.1.0
```

## 项目集成
```shell
cd /Users/adolphor/IdeaProjects/bob/tree-node-boot && mvnjoy clean install
java -javaagent:/Users/adolphor/Applications/skywalking-agent-9.1.0/skywalking-agent.jar \
    -Dskywalking.agent.service_name=spring-boot-demo \
    -Dskywalking.collector.backend_service=127.0.0.1:11800 \
    -jar spring-boot-demo/target/spring-boot-demo-0.1.2-SNAPSHOT.jar \
    --spring.profiles.active=local
```

## 参考资料
* [使用Docker在本地部署SkyWalking]({% post_url micro-service/skywalking/2022-12-09-03-skywalking-in-docker %})
* [docker hub - skywalking-ui](https://hub.docker.com/r/apache/skywalking-ui/tags)
* [docker hub - skywalking-oap-server](https://hub.docker.com/r/apache/skywalking-oap-server/tags)
* [docker hub - kibana](https://hub.docker.com/_/kibana?tab=tags)
* [基于Docker实现Skywalking安装](https://juejin.cn/post/7073720092273590309)
* [Docker部署ElasticSearch及使用](https://juejin.cn/post/6844904202204872711#heading-4)
* [skywalking 官网下载](https://skywalking.apache.org/downloads/)
* [skywalking 官网文档](https://skywalking.apache.org/docs/main/v9.3.0/readme/)
* [使用CURL命令操作ES](https://cloud.tencent.com/developer/article/1509404)

开始写作吧

```
![image-alter]({{ site.baseurl }}/image/post/2022/12/09/03/xxx.jpg)
```
