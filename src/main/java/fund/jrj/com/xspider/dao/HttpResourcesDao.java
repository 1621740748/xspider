package fund.jrj.com.xspider.dao;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.crud.CrudDao;

import fund.jrj.com.xspider.bo.HttpResources;

@DB(table = "http_resources")
public interface HttpResourcesDao extends CrudDao<HttpResources, Integer> {

}
