* 增强lan稳定性
* lan连接的时候增加鉴权机制
    - 一个lan一个秘钥，一个秘钥只能存在一条连接
    - 对于socks客户端，也需要根据秘钥来确定身份，这样才能在多用户情况下确定应该将msg转往哪个lan客户端
    - 增加秘钥有效时间段限制
    - 增加lan管理的远程连接和server管理的请求连接之间的心跳检测机制，防止某一端异常断开连接没有通知到另一端进行关闭的情况
    - 各个请求中间会产生影响，一个请求的失败不应该影响另一个请求，导致另一个请求也失败
* 加密方式增加 none 不做任何加密的限制
    - ss加密和lan加密分离，可以启动任一加密对另一端没有影响
* SSL/TLS
    - [X] 优化TLS证书相关逻辑，参考surge相关逻辑流程（增加密码，去掉私钥？）
    - 解决postman请求HTTPS时，如果启动 MITM 则请求失败的BUG
* 加解密公共方法重构
    - 规范调用方式
    - 增加更多加密方式的实现
* netty框架的学习
    - ByteBuf
        - ByteBuf容量可以按需增长，那么当前mynety版本却使用了重新定义特定长度的字节对象类解决，需要优化
        - channel中接收的ByteBuf是堆内存还是直接内存？
        - 池化和非池化区别是什么
    - selector原理剖析
    - outBound为什么使用inBoundChannel的loop
* 框架优化
    - （待评估）将channel自动接收功能关闭，改为手动控制，这样能够保证接收信息不回乱序，另外，节省服务器内存占用
* JDK 升级 
    - 模块化之路：[Migrate Maven Projects to Java 11](https://winterbe.com/posts/2018/08/29/migrate-maven-projects-to-java-11-jigsaw/)
    - 新的语法糖，不好奇吗？





