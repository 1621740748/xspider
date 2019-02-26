package fund.jrj.com.xspider.bo;

import java.util.Date;

import org.jfaster.mango.annotation.ID;

public class HostCertificate {
	@ID
	private Integer hostId;
	private String host;
	private String version;
	private String serialNumber;
	private String subjectDn;
	private String issueDn;
	private Date startTime;
	private Date endTime;
	private String publicKey;
	private String privateKey;
	public Integer getHostId() {
		return hostId;
	}
	public void setHostId(Integer hostId) {
		this.hostId = hostId;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getSubjectDn() {
		return subjectDn;
	}
	public void setSubjectDn(String subjectDn) {
		this.subjectDn = subjectDn;
	}
	public String getIssueDn() {
		return issueDn;
	}
	public void setIssueDn(String issueDn) {
		this.issueDn = issueDn;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	
}
