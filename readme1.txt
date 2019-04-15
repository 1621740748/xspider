 1、查询页面css链接
 select distinct  link_host_path from  page_link1 where page_type in(2) and link_host_path not like '%.shtml' order by link_host_path ;
 2、查询页面js链接 
  select distinct  link_host_path from  page_link1 where page_type in(1) and link_host_path not like '%.shtml' order by link_host_path ;
 3、查询iframe链接 
  select distinct  link_host_path from  page_link1 where page_type in(6) and link_host_path not like '%.shtml' order by link_host_path ;
 4、查询js中使用的链接 
  select distinct  link_host_path from  page_link1 where page_type in(5) and link_host_path not like '%.shtml' order by link_host_path ;