package fund.jrj.com.xspider.bo;

import org.jfaster.mango.annotation.ID;

public class PageLink {
	@ID
    private Integer linkId;
    private String linkUrl;
    private String linkParentUrl;
    private Integer pageType;
    private Integer httpsEnable;
    private Integer httpEnable;
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
	public Integer getHttpsEnable() {
		return httpsEnable;
	}
	public void setHttpsEnable(Integer httpsEnable) {
		this.httpsEnable = httpsEnable;
	}
	public Integer getHttpEnable() {
		return httpEnable;
	}
	public void setHttpEnable(Integer httpEnable) {
		this.httpEnable = httpEnable;
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
    
    
}
