package fund.jrj.com.xspider.bo;

import org.jfaster.mango.annotation.ID;

public class PageLink {
	@ID
    private Integer linkId;
    private String linkUrl;
    private String linkParentUrl;
    private String linkHost;
    private Integer pageType;
    private Integer autoAdapt;
    private Integer httpExist;
    private String httpExistContent;
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
	public String getLinkParentUrl() {
		return linkParentUrl;
	}
	public void setLinkParentUrl(String linkParentUrl) {
		this.linkParentUrl = linkParentUrl;
	}
	public Integer getPageType() {
		return pageType;
	}
	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}
	public Integer getHttpExist() {
		return httpExist;
	}
	public void setHttpExist(Integer httpExist) {
		this.httpExist = httpExist;
	}
	public String getHttpExistContent() {
		return httpExistContent;
	}
	public void setHttpExistContent(String httpExistContent) {
		this.httpExistContent = httpExistContent;
	}
	public Integer getAutoAdapt() {
		return autoAdapt;
	}
	public void setAutoAdapt(Integer autoAdapt) {
		this.autoAdapt = autoAdapt;
	}
	public String getLinkHost() {
		return linkHost;
	}
	public void setLinkHost(String linkHost) {
		this.linkHost = linkHost;
	}
    
    
}
