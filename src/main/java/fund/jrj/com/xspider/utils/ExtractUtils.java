package fund.jrj.com.xspider.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class ExtractUtils {
	public static Map<String, Boolean> hostHttpsMap = new HashMap<>();
	public static final Integer MAX_INFOSIZE = 2048;
	public static Pattern HTTPURL = Pattern.compile("(http://(?:[a-z,A-Z,0-9]+\\.){1,6}[^\"\'\\s]+)"); // 正则表达式
	private static String getBaseURL(String url) {
		try {
			URL u = new URL(url);
			String base = u.getProtocol() + "://" + u.getHost();
			if (u.getPort() != 80 && u.getProtocol().equals("http")) {
				base = base + ":" + u.getPort();
			} else if (443 != u.getPort() && u.getProtocol().equals("https")) {
				base = base + ":" + u.getPort();
			}
			return base;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getAbsUrl(String base, String uri) {
		if (uri == null) {
			return "";
		}
		if (uri.startsWith("javascript")) {
			return "";
		}
		if (uri.startsWith("//")) {
			return "http:" + uri;
		}
		if (uri.startsWith("http://") || uri.startsWith("https://")) {
			return uri;
		}
		if (uri.startsWith("/")) {
			return base + uri;
		}
		return base + "/" + uri;
	}
	public static String getHost(String url) {
		if(StringUtils.isBlank(url)) {
			return "";
		}
		try {
			return new URL(url).getHost();
		} catch (MalformedURLException e) {
			System.out.println("error："+url);
			e.printStackTrace();
		}
		return "";
	}
	public static String getHostAndPath(String url) {
		if(StringUtils.isBlank(url)) {
			return "";
		}
		try {
			URL u=new URL(url);
			return u.getHost()+u.getPath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String getPath(String url) {
		if(StringUtils.isBlank(url)) {
			return "";
		}
		try {
			URL u=new URL(url);
			return u.getPath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return "";
	}
	private static boolean isJRJHost(String host) {
		if (host.endsWith(".jrj.com.cn") || host.endsWith(".jrjimg.cn")) {
			return true;
		}
		return false;
	}

	private static String findHttpAbs(String body) {
		List<String> result = new LinkedList<>();
		Matcher m = HTTPURL.matcher(body); // 操作的字符串
		while (m.find()) {
			result.add(m.group(1));
		}
		if (result.isEmpty()) {
			return "";
		} else {
			String s = StringUtils.join(result, "\n");
			return StringUtils.abbreviate(s, MAX_INFOSIZE);
		}
	}


	public static List<String> extractLinksV2(String url) {
		List<String> result = new LinkedList<>();
		if (StringUtils.isBlank(url)) {
			return result;
		}
		String base = getBaseURL(url);
		HtmlUnitDriver driver = new HtmlUnitDriver();
		driver.setJavascriptEnabled(true);
		driver.get(url);
		//保存html dom
	//	String content=driver.getPageSource();
	//	OkhttpUtils.getInstance().storeHtml(url, content);
		List<WebElement> webElements = driver.findElementsByTagName("a");
		if(webElements!=null) {
			webElements.stream().forEach(e->{
				if(e!=null&&StringUtils.isNotBlank(e.getAttribute("href"))) {
					String href=e.getAttribute("href");
					String absUrl=ExtractUtils.getAbsUrl(base, href);
					if(StringUtils.isNotBlank(absUrl)) {
						result.add(absUrl);
					}
				}
			});
		}
		 webElements = driver.findElementsByTagName("iframe");
		if(webElements!=null) {
			webElements.stream().forEach(e->{
				if(e!=null&&StringUtils.isNotBlank(e.getAttribute("src"))) {
					String href=e.getAttribute("src");
					String absUrl=ExtractUtils.getAbsUrl(base, href);
					if(StringUtils.isNotBlank(absUrl)) {
						result.add(absUrl);
					}
				}
			});
		}
		 webElements = driver.findElementsByTagName("frame");
		if(webElements!=null) {
			webElements.stream().forEach(e->{
				if(e!=null&&StringUtils.isNotBlank(e.getAttribute("src"))) {
					String href=e.getAttribute("src");
					String absUrl=ExtractUtils.getAbsUrl(base, href);
					if(StringUtils.isNotBlank(absUrl)) {
						result.add(absUrl);
					}
				}
			});
		}
		return result;
	}
}
