package fund.jrj.com.xspider.dao;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.crud.CrudDao;

import fund.jrj.com.xspider.bo.Resources;

@DB(table = "resources")
public interface ResourcesDao extends CrudDao<Resources, Integer> {

}