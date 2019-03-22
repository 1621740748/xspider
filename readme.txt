1、查询项目中的 js script
select distinct link_parent_url   from page_link1 where http_exist=1 and page_type in (1) and link_parent_url  like '%.js' order by link_parent_url;

2、