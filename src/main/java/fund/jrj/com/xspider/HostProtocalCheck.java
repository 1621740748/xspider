package fund.jrj.com.xspider;

import fund.jrj.com.xspider.bo.PageLink;
import fund.jrj.com.xspider.utils.ExtractUtils;

public class HostProtocalCheck  implements Runnable{
	private PageLink pl;
	public HostProtocalCheck(PageLink p) {
		pl=p;
	}
	public void run() {
		try {
			ExtractUtils.checkHostSupportProtocal(pl.getLinkUrl(), pl.getLinkHost(), pl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
