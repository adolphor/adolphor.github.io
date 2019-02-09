* 增强lan稳定性
* lan连接的时候增加鉴权机制
    - 一个lan一个秘钥，一个秘钥只能存在一条连接
    - 增加秘钥有效时间段限制
* 加密方式增加 none 不做任何加密的限制
    - ss加密和lan加密分离，可以启动任一加密对另一端没有影响
* 解决postman请求HTTPS时，如果启动 MITM 则请求失败的BUG
* 加解密公共方法重构
    - 规范调用方式
    - 增加更多加密方式的实现
* netty框架的学习
    - ByteBuf
    - selector原理剖析
    - outBound为什么使用inBoundChannel的loop