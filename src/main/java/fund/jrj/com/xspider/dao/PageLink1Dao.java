package fund.jrj.com.xspider.dao;

import java.util.List;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.crud.CrudDao;

import fund.jrj.com.xspider.bo.PageLink1;

@DB(table = "page_link1")
public interface PageLink1Dao extends CrudDao<PageLink1, Integer> {
	@SQL("select * from page_link1 where http_exist=1 and page_type in(1,2,3)")
	List<PageLink1> selectHttpExists();
}
