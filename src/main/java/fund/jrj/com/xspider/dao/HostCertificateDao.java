package fund.jrj.com.xspider.dao;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.crud.CrudDao;

import fund.jrj.com.xspider.bo.HostCertificate;

@DB(table = "host_certificate")
public interface HostCertificateDao extends CrudDao<HostCertificate, Integer> {
}
