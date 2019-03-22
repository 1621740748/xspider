1、查询项目中的 js script
create table temp select distinct link_parent_url   from page_link1 where http_exist=1 and page_type in (1) and link_parent_url  like '%.js' order by link_parent_url;

2、查找包含blocked js 的页面
create table page_js select  p.link_host_path as js ,any_value(p.link_parent_url) as page  from temp t left join page_link1 p  on t.link_parent_url=p.link_url group by p.link_host_path;

3、查看典型页面 
 select distinct page from page_js;
 