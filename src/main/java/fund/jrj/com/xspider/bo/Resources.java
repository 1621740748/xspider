package fund.jrj.com.xspider.bo;

import org.jfaster.mango.annotation.ID;

public class Resources {
	@ID
	private Integer rId;
	private String url;
	private String host;
	private String hostPath;
	private Integer httpEnable;
	private Integer httpsEnable;
	// url 哈希
	private String hash; 
	// 0、没有问题 1、有问题 2、不确定有无问题
	private Integer problemType;
	//1、html 页面加载 2、js或者css加载
	private Integer loadType;
	public Integer getrId() {
		return rId;
	}
	public void setrId(Integer rId) {
		this.rId = rId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getHostPath() {
		return hostPath;
	}
	public void setHostPath(String hostPath) {
		this.hostPath = hostPath;
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
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public Integer getProblemType() {
		return problemType;
	}
	public void setProblemType(Integer problemType) {
		this.problemType = problemType;
	}
	public Integer getLoadType() {
		return loadType;
	}
	public void setLoadType(Integer loadType) {
		this.loadType = loadType;
	}
	
}
