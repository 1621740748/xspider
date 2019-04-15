1、查询项目中的 js script
create table temp select distinct link_parent_url   from page_link1 where http_exist=1 and page_type in (1,5) and link_parent_url  like '%.js' order by link_parent_url;

2、查找包含blocked js 的页面
create table page_js select  p.link_host_path as js ,any_value(p.link_parent_url) as page  from temp t left join page_link1 p  on t.link_parent_url=p.link_url group by p.link_host_path;

3、查看典型页面 
 select distinct page from page_js;
 
 4、复制典型页面的内容到xcheck项目的seeds.txt 并且替换链接为https协议，运行xcheck项目
 
 5、得到需要修改的结果在 blocked_resources表中
   select distinct initiate_url  from blocked_resources  where initiate_url like '%.js' order by initiate_url;

6、得到被blocked的请求 
 select distinct res_host_path from blocked_resources where initiate_url not like '%.html' order by res_host_path ;
 
 7、查询nginx中需要替换的url
 select distinct link_host_path from page_link1 where page_type in (1,2) and http_exist=1 order by link_host_path;
 
 
 