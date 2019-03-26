package fund.jrj.com.xspider.constants;

public enum PageTypeEnum {
	JS(1),CSS(2),IMG(3),HTML(4),JSINJS(5);
	private Integer pageType;
	private PageTypeEnum(Integer pageType) {
		this.setPageType(pageType);
	}
	public Integer getPageType() {
		return pageType;
	}
	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}
}
