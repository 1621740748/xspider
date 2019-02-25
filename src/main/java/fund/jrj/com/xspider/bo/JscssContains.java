package fund.jrj.com.xspider.bo;

import org.jfaster.mango.annotation.ID;

public class JscssContains {
	@ID
	private Integer urlId;
	private String url;
	private String containHost;
	public Integer getUrlId() {
		return urlId;
	}
	public void setUrlId(Integer urlId) {
		this.urlId = urlId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContainHost() {
		return containHost;
	}
	public void setContainHost(String containHost) {
		this.containHost = containHost;
	}
	
}
