create schema redict collate utf8_general_ci;

create table clocking_in
(
	id varchar(32) not null,
	user_id varchar(32) null comment '用户ID',
	daily_task tinyint(1) default '0' null comment '日常任务是否完成',
	clock_year int(4) null comment '年',
	clock_month int(2) null comment '月',
	deleted tinyint(1) default '0' not null comment '是否删除',
	create_user varchar(32) not null comment '创建用户',
	create_time datetime not null comment '创建时间',
	update_user varchar(32) not null comment '更新用户',
	update_time datetime not null comment '更新时间'
)
comment '打卡记录表';

create table dict_user
(
	id varchar(32) not null,
	nick_name varchar(32) null comment '用户昵称',
	user_name varchar(32) null comment '用户名称',
	avactor_img varchar(32) null comment '头像',
	gender tinyint(1) null comment '性别',
	age int(3) null comment '年龄',
	telephone varchar(11) null comment '手机号',
	email varchar(32) null comment '邮箱',
	open_id varchar(32) null comment '微信openId',
	union_id varchar(32) null comment '微信unionId',
	deleted tinyint(1) default '0' not null comment '是否删除',
	create_user varchar(32) not null comment '创建用户',
	create_time datetime not null comment '创建时间',
	update_user varchar(32) not null comment '更新用户',
	update_time datetime not null comment '更新时间'
);

create table primary_test
(
	id varchar(32) not null
		primary key,
	name varchar(32) not null
);

create table sentence
(
	id varchar(32) not null,
	show_date varchar(10) null comment '推荐展示日期',
	content_en varchar(1024) null comment '英文内容',
	content_zh varchar(1024) null comment '中文内容',
	img_url varchar(256) null comment '图片地址',
	note varchar(1024) null comment '札记',
	love int default '0' null comment '点赞数量',
	deleted tinyint(1) default '0' not null comment '是否删除',
	create_user varchar(32) not null comment '创建用户',
	create_time datetime not null comment '创建时间',
	update_user varchar(32) not null comment '更新用户',
	update_time datetime not null comment '更新时间'
)
comment '每日推荐';

create table vocabulary
(
	id varchar(32) not null,
	vocabulary varchar(32) null comment '词汇',
	phonetic_usa varchar(32) null comment '美音音标',
	phonetic_uk varchar(32) null comment '英音音标',
	coca_order int null comment 'coca词频排序',
	deleted tinyint(1) default '0' not null comment '是否删除',
	create_user varchar(32) not null comment '创建用户',
	create_time datetime not null comment '创建时间',
	update_user varchar(32) not null comment '更新用户',
	update_time datetime not null comment '更新时间'
);

