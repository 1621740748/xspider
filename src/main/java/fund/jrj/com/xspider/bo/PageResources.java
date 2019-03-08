package fund.jrj.com.xspider.bo;

import org.jfaster.mango.annotation.ID;

/**
 * 页面和链接的关系
 * @author huangyan
 *
 */
public class PageResources {
	@ID
	private Integer prId;
	private String pageUrl;
	private String resHash;
	//1、html 2、jscss
	private Integer pageType;
	public Integer getPrId() {
		return prId;
	}
	public void setPrId(Integer prId) {
		this.prId = prId;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public String getResHash() {
		return resHash;
	}
	public void setResHash(String resHash) {
		this.resHash = resHash;
	}
	public Integer getPageType() {
		return pageType;
	}
	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	} 
	
}
