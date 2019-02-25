package fund.jrj.com.xspider.dao;

import java.util.List;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.crud.CrudDao;

import fund.jrj.com.xspider.bo.PageLink;

@DB(table = "page_link")
public interface PageLinkDao extends CrudDao<PageLink, Integer> {
	@SQL("select * from page_link where http_exist=1 and page_type in(1,2,3)")
	List<PageLink> selectHttpExists();
}
