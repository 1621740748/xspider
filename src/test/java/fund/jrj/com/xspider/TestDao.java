package fund.jrj.com.xspider;

import org.apache.commons.codec.digest.DigestUtils;

import fund.jrj.com.xspider.bo.Resources;
import fund.jrj.com.xspider.dao.ResourcesDao;
import fund.jrj.com.xspider.utils.DBUtils;

public class TestDao {

	public static void main(String[] args) {
		ResourcesDao plDao=DBUtils.getInstance().create(ResourcesDao.class);
		Resources p=new Resources();
		String url="http://fund.jrj.com.cn/aa/rr";
		p.setUrl(url);
		p.setHost("fund.jrj.com.cn");
		p.setHash(DigestUtils.sha384Hex(url));
		p.setHostPath("fund.jrj.com.cn/aa/rr");
		p.setHttpEnable(1);
		p.setHttpsEnable(1);
		p.setLoadType(1);
		p.setProblemType(1);
		plDao.add(p);
	}

}
