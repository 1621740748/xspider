package fund.jrj.com.xspider;

import fund.jrj.com.xspider.bo.PageLink1;
import fund.jrj.com.xspider.constants.PageTypeEnum;
import fund.jrj.com.xspider.dao.PageLink1Dao;
import fund.jrj.com.xspider.utils.DBUtils;

public class TestDao {

	public static void main(String[] args) {
		PageLink1Dao plDao=DBUtils.getInstance().create(PageLink1Dao.class);
		PageLink1 p=new PageLink1();
		p.setLinkUrl("https://www.baidu.com");
		p.setPageType(PageTypeEnum.HTML.getPageType());
		plDao.add(p);
	}

}
