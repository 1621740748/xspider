create table page_link(
link_id int auto_increment primary key comment '链接ID' ,
link_url varchar(256) comment '链接',
link_parent_url varchar(256) comment '父链接',
page_type tinyint comment '页面类型 1、js 2、css 3、img 4、html',
auto_adapt tinyint default 0 comment '是否自适应 0 否 1 是',
https_enable tinyint default 0 comment '1 可以https访问 0 不可以',
http_enable tinyint  default 1 comment '1 可以http访问 0 不可以',
http_exist tinyint default 0 comment '是否存在http内容',
http_exist_content varchar(512) default '' comment 'http内容'
);