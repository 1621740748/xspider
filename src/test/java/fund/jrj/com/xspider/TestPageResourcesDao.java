package fund.jrj.com.xspider;

import org.apache.commons.codec.digest.DigestUtils;

import fund.jrj.com.xspider.bo.PageResources;
import fund.jrj.com.xspider.dao.PageResourcesDao;
import fund.jrj.com.xspider.utils.DBUtils;

public class TestPageResourcesDao {

	public static void main(String[] args) {
		PageResourcesDao plDao=DBUtils.getInstance().create(PageResourcesDao.class);
		PageResources p=new PageResources();
		String url="http://fund.jrj.com.cn/aa/rr";
		p.setPageUrl(url);
		p.setPageType(1);
		p.setResHash(DigestUtils.sha384Hex(url));
		plDao.add(p);
	}

}
