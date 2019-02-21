package fund.jrj.com.xspider.dao;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.crud.CrudDao;

import fund.jrj.com.xspider.bo.PageLink;

@DB(table = "page_link")
public interface PageLinkDao extends CrudDao<PageLink, Integer> {

}
