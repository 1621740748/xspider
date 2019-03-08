create table page_link(
  link_id int auto_increment primary key comment '链接ID' ,
  link_url varchar(256) comment '链接',
  link_host varchar(128) comment '链接host',
  link_parent_url varchar(256) comment '父链接',
  page_type tinyint comment '页面类型 1、js 2、css 3、img 4、html',
  auto_adapt tinyint default 0 comment '是否自适应 0 否 1 是',
  http_exist tinyint default 0 comment '是否存在http内容',
  http_exist_content varchar(2048) default '' comment 'http内容',
  create_time timestamp default CURRENT_TIMESTAMP comment '创建时间'
);
create table http_resources(
  link_id int auto_increment primary key comment '链接ID' ,
  link_url varchar(256) comment '链接',
  link_host varchar(128) comment '链接host',
  page_type tinyint comment '页面类型 1、js 2、css 3、img ',
  http_enable tinyint default 0 comment '是否http可访问',
  https_enable tinyint default 0 comment '是否https可访问',
  create_time timestamp default CURRENT_TIMESTAMP comment '创建时间'
);
create table  jscss_contains(
  url_id int auto_increment primary key comment '链接ID',
  url varchar(256) comment '链接',
  contain_host varchar(128) comment '包含的host',
  create_time timestamp default CURRENT_TIMESTAMP comment '创建时间'
);
create table  host_certificate(
  host_id int auto_increment primary key comment 'url ID',
  host varchar(256) comment '主机',
  version varchar(64) comment '版本',
  serial_number varchar(128) comment '证书编号',
  subject_dn varchar(512) comment '颁发机构',  
  issue_dn varchar(512) comment '颁发者',  
  start_time datetime comment '证书开始时间', 
  end_time datetime comment '证书结束时间', 
  sig_alg_name varchar(64) comment '签名算法',
  public_key varchar(4096) comment '公钥',
  private_key varchar(4096) comment '私钥',
  create_time timestamp default CURRENT_TIMESTAMP comment '创建时间'
);