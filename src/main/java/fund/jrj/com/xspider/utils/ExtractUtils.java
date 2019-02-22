package fund.jrj.com.xspider.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import fund.jrj.com.xspider.bo.PageLink;
import fund.jrj.com.xspider.constants.PageTypeEnum;

public class ExtractUtils {
	private static String getBaseURL(String url) {
		try {
			URL u=new URL(url);
			String base=u.getProtocol()+"://"+u.getHost();
			if(u.getPort()!=80&&u.getProtocol().equals("http")) {
				base=base+":"+u.getPort();
			}else if(443!=u.getPort()&&u.getProtocol().equals("https")) {
				base=base+":"+u.getPort();
			}
			return base;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return "";
	}
	private static String getAbsUrl(String base,String uri) {
		if(uri==null) {
			return "";
		}
		if(uri.startsWith("//")) {
			return "http:"+uri;
		}
		if(uri.startsWith("http://")||uri.startsWith("https://")) {
			return uri;
		}
		if(uri.startsWith("/")) {
			return base+uri;
		}
		return base+"/"+uri;
	}
	private static void addUrl(List<PageLink> result, List<WebElement>webElements ,Integer pageType,String base,String parentUrl) {
		for(WebElement we:webElements) {
			PageLink p=new PageLink();
			p.setPageType(PageTypeEnum.CSS.getPageType());
			String u=we.getAttribute("href");
			if(u!=null&&u.startsWith("//")) {
				p.setAutoAdapt(1);
			}
			String linkUrl=getAbsUrl(base,u);
			if(StringUtils.isBlank(linkUrl)) {
				continue;
			}
			p.setLinkUrl(linkUrl);
			p.setLinkParentUrl(parentUrl);
			System.out.println(linkUrl);
			result.add(p);
		}
	}
	public static List<PageLink> extractLinks(String url){
		String base=getBaseURL(url);
		List<PageLink> result=new LinkedList<>();
		HtmlUnitDriver driver = new HtmlUnitDriver();
		driver.setJavascriptEnabled(true);
		driver.get(url);
		List<WebElement> webElements=driver.findElementsByTagName("link");
		System.out.println("--------------link------------------------");
		addUrl(result,webElements,PageTypeEnum.CSS.getPageType(),base,url);
		webElements=driver.findElementsByTagName("script");
		System.out.println("--------------script------------------------");
		addUrl(result,webElements,PageTypeEnum.JS.getPageType(),base,url);
		webElements=driver.findElementsByTagName("img");
		System.out.println("--------------img------------------------");
		addUrl(result,webElements,PageTypeEnum.IMG.getPageType(),base,url);
		webElements=driver.findElementsByTagName("a");
		System.out.println("--------------a------------------------");
		addUrl(result,webElements,PageTypeEnum.HTML.getPageType(),base,url);
		return result;
	}
}
