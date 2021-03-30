
# docker 服务编排

* [Docke —— 从入门到实战 ](https://yeasy.gitbooks.io/docker_practice/compose/compose_file.html)
* [Docker Compose 配置文件详解](https://www.jianshu.com/p/2217cfed29d7)
* [Docker Compose](https://www.qikqiak.com/k8s-book/docs/8.Docker%20Compose.html)

# TODO

* 怎么重新构建镜像？
第一次使用 `docker-compose build` 之后会构建镜像，但如果构建错误了应该怎么修复呢？
如果还是执行上面的命令，那么会出现 `v2ray uses an image, skipping` 的提示。
当前的解决方案是使用 `docker-compose up --build` 来解决这个问题，但使用这个命令
就不是单纯的构建镜像了，而是构建成功之后直接启动docker服务了。所以，还是需要找到根本原因。
