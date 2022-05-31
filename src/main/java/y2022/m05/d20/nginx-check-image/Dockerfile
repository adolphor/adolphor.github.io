FROM alpine:3.14
MAINTAINER adolphor "0haizhu0@gmail.com"

USER root
WORKDIR /var/temp

RUN sed -i 's/https/http/' /etc/apk/repositories

# 安装必要工具 和 调试工具
RUN apk add --no-cache --virtual .build-deps \
            curl \
            gcc \
            libc-dev \
            make \
            openssl-dev \
            pcre-dev \
            zlib-dev \
            linux-headers \
            curl \
            libxslt-dev \
            gd-dev \
            geoip-dev \
            patch \
    # 准备日志目录
    && mkdir -p /var/log/nginx

# 准备资源
# http://nginx.org/en/download.html
ADD nginx-1.20.2.tar.gz .
# https://github.com/yaoweibin/nginx_upstream_check_module
ADD nginx_upstream_check_module-master.tar.gz .

# 打补丁并编译
RUN cd nginx-1.20.2  \
    && patch -p1 < /var/temp/nginx_upstream_check_module-master/check_1.20.1+.patch \
    && ./configure --prefix=/usr/local/nginx --add-module=../nginx_upstream_check_module-master \
    && make \
    && make install

# 暴露端口
EXPOSE 80

STOPSIGNAL SIGTERM

# 启动nginx 将nginx主进程 pid为1，nginx一旦挂掉那么docker容器就会直接退出
CMD ["/usr/local/nginx/sbin/nginx", "-g", "daemon off;"]