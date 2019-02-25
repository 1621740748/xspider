package fund.jrj.com.xspider.dao;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.crud.CrudDao;

import fund.jrj.com.xspider.bo.JscssContains;

@DB(table = "jscss_contains")
public interface JscssContainsDao extends CrudDao<JscssContains, Integer> {

}
