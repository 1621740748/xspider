package fund.jrj.com.xspider.bo;

import org.jfaster.mango.annotation.ID;

public class HttpResources {
	@ID
	private Integer linkId;
	private String linkUrl;
	private String linkHost;
	private Integer pageType;
	private Integer httpEnable;
	private Integer httpsEnable;
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getLinkHost() {
		return linkHost;
	}
	public void setLinkHost(String linkHost) {
		this.linkHost = linkHost;
	}
	public Integer getPageType() {
		return pageType;
	}
	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}
	public Integer getHttpEnable() {
		return httpEnable;
	}
	public void setHttpEnable(Integer httpEnable) {
		this.httpEnable = httpEnable;
	}
	public Integer getHttpsEnable() {
		return httpsEnable;
	}
	public void setHttpsEnable(Integer httpsEnable) {
		this.httpsEnable = httpsEnable;
	}
	
}
