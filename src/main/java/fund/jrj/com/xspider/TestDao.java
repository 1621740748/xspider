package fund.jrj.com.xspider;

import fund.jrj.com.xspider.bo.PageLink;
import fund.jrj.com.xspider.constants.PageTypeEnum;
import fund.jrj.com.xspider.dao.PageLinkDao;
import fund.jrj.com.xspider.utils.DBUtils;

public class TestDao {

	public static void main(String[] args) {
		PageLinkDao plDao=DBUtils.getInstance().create(PageLinkDao.class);
		PageLink p=new PageLink();
		p.setLinkUrl("https://www.baidu.com");
		p.setPageType(PageTypeEnum.HTML.getPageType());
		plDao.add(p);
	}

}
