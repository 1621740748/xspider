package fund.jrj.com.xspider.dao;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.crud.CrudDao;

import fund.jrj.com.xspider.bo.PageResources;

@DB(table = "page_resources")
public interface PageResourcesDao extends CrudDao<PageResources, Integer> {

}