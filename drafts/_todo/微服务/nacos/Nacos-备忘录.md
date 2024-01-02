# TODO 参考资料
* [你应该了解的Nacos注册中心](https://www.lagou.com/lgeduarticle/137212.html)
* [Nacos官网：集群部署说明](https://nacos.io/zh-cn/docs/cluster-mode-quick-start.html)
* [Nacos配置中心集群原理及源码分析](https://segmentfault.com/a/1190000041625660)
* [详解nacos注册中心服务注册流程](https://juejin.cn/post/6950867039284101151)
* [Nacos 集群部署模式最佳实践](https://lexburner.github.io/nacos-cluster-mode/)
* [Nacos 监控](https://nacos.io/zh-cn/docs/monitor-guide.html)
* [？？Nacos 集群选举原理](https://www.cnblogs.com/wuzhenzhao/p/13641155.html)：应该没有选举机制吧？
* [Nacos集群部署实现原理&分布式一致性算法raft协议](https://www.i4k.xyz/article/u012425860/114779266)


# 端口
server 8848
client-rpc 9848
raft-rpc 9849
old-raft-rpc 7848


172.16.4.163:8848

# 本地测试版本兼容性
```shell
docker run -d \
    --name nacos-server-210 \
    --restart always \
    -e TZ=Asia/Shanghai \
    --network host
    --privileged=true --restart=always \
    -e JVM_XMS=256m -e JVM_XMX=256m \
    -e MODE=standalone \
    -e SPRING_DATASOURCE_PLATFORM=mysql \
    -e MYSQL_SERVICE_HOST=10.157.170.233 \
    -e MYSQL_SERVICE_PORT=3306 \
    -e MYSQL_SERVICE_DB_NAME=jy_microservice_auth \
    -e MYSQL_SERVICE_USER=bobzhu \
    -e MYSQL_SERVICE_PASSWORD='Adolphor!@#123' \
    -e MYSQL_DATABASE_NUM=1 \
    -e NACOS_AUTH_ENABLE=true \
    -v /Users/adolphor/Applications/nacos/logs/nacos/:/home/nacos/logs \
    -p 8848:8848 \
    -d nacos/nacos-server:v2.1.0

docker run -d \
    --name nacos-server-131 \
    --restart always \
    -e TZ=Asia/Shanghai \
    --privileged=true --restart=always \
    -e JVM_XMS=256m -e JVM_XMX=256m \
    -e MODE=standalone \
    -e SPRING_DATASOURCE_PLATFORM=mysql \
    -e MYSQL_SERVICE_HOST=10.157.170.233 \
    -e MYSQL_SERVICE_PORT=3306 \
    -e MYSQL_SERVICE_DB_NAME=jy_microservice_auth \
    -e MYSQL_SERVICE_USER=bobzhu \
    -e MYSQL_SERVICE_PASSWORD='Adolphor!@#123' \
    -e MYSQL_DATABASE_NUM=1 \
    -e NACOS_AUTH_ENABLE=true \
    -v /Users/adolphor/logs/nacos/:/home/nacos/logs \
    -p 8847:8848 \
    -d registry.cn-hangzhou.aliyuncs.com/jy-cloud-dev/nacos:1.3.1



docker tag nacos/nacos-server:v2.1.0 registry.cn-hangzhou.aliyuncs.com/jy-cloud-dev/nacos-server:v2.1.0
docker push registry.cn-hangzhou.aliyuncs.com/jy-cloud-dev/nacos-server:v2.1.0


```



