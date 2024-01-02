# 实现方式

## aspect
* 支持异步方法的日志记录
* 不方便获取body参数

## interceptor
* 方便获取body参数
* 方便获取最终返回结果，不受抛出的异常影响

## 表结构
```mysql
create table request_log
(
    id           bigint PRIMARY KEY auto_increment DEFAULT 1,
    uri          varchar(256)                       not null,
    method       varchar(16)                        not null,
    req_param    text                               not null,
    resp_param   text                               null,
    async_result text                               null,
    was_success  tinyint                            not null,
    create_time  datetime default CURRENT_TIMESTAMP not null,
    update_time  datetime default CURRENT_TIMESTAMP not null
)
comment '接口调用日志' collate = utf8mb4_general_ci;
```