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
    # 准备日志目录
    && mkdir -p /var/log/nginx

# 准备资源
# https://tengine.taobao.org/download.html
ADD tengine-2.3.3.tar.gz .
# https://www.openssl.org/source/
ADD openssl-1.1.1o.tar.gz .

# 打补丁并编译
RUN cd tengine-2.3.3 \
    && ./configure --with-openssl=/var/temp/openssl-1.1.1o --add-module=modules/ngx_http_upstream_check_module \
    && make \
    && make install

# 暴露端口
EXPOSE 80

STOPSIGNAL SIGTERM

# 启动nginx 将nginx主进程 pid为1，nginx一旦挂掉那么docker容器就会直接退出
CMD ["/usr/local/nginx/sbin/nginx", "-g", "daemon off;"]
