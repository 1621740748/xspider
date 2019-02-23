package fund.jrj.com.xspider.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.alibaba.fastjson.JSON;

import fund.jrj.com.xspider.bo.PageLink;
import fund.jrj.com.xspider.constants.PageTypeEnum;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ExtractUtils {
	public static Map<String, Boolean> hostHttpsMap = new HashMap<>();
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

	private static String getAbsUrl(String base, String uri) {
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

	private static void checkHttpsHost(String url, String host, PageLink pl) {
		if (hostHttpsMap.containsKey(host)) {
			if (hostHttpsMap.get(host)) {
				pl.setHttpsEnable(1);
			} else {
				pl.setHttpsEnable(0);
			}
			return;
		}
		OkhttpUtils.getInstance().doGet(url, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				pl.setHttpsEnable(0);
				hostHttpsMap.put(host, false);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				pl.setHttpsEnable(1);
				hostHttpsMap.put(host, true);
				response.close();
			}
		});
	}

	private static void addUrl(List<PageLink> result, List<WebElement> webElements, Integer pageType, String base,
			String parentUrl) {
		for (WebElement we : webElements) {
			PageLink p = new PageLink();
			p.setPageType(pageType);
			String u = we.getAttribute("href");
			if (u != null && u.startsWith("//")) {
				p.setAutoAdapt(1);
			}
			String linkUrl = getAbsUrl(base, u);
			if (StringUtils.isBlank(linkUrl)) {
				continue;
			}
			p.setLinkUrl(linkUrl);
			p.setLinkParentUrl(parentUrl);
			System.out.println(linkUrl);
			checkExistHttp(p);
			result.add(p);
		}
	}

	private static String getHost(String url) {
		try {
			return new URL(url).getHost();
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
		List<String> result=new LinkedList<>();
		Matcher m = HTTPURL.matcher(body); // 操作的字符串
		while (m.find()) {
			result.add(m.group(1));
		}
		if(result.isEmpty()) {
			return "";
		}else {
			String s= StringUtils.join(result, "\n");
			if(s.length()>512) {
				s=s.substring(0, 511);
			}
			return s;
		}
	}

	private static void checkExistHttp(final PageLink pl) {
		// 检查host是否jrj域名
		String host = getHost(pl.getLinkUrl());
		boolean flag = isJRJHost(host);
		// 不检查http页面是否
		if (pl.getPageType() == PageTypeEnum.HTML.getPageType() && !flag) {
			return;
		}
		//检查是否已经分析过该资源
		String value=RockUtils.get(pl.getLinkUrl());
		if(StringUtils.isNotBlank(value)) {
			PageLink temp=JSON.parseObject(value,PageLink.class );
			pl.setHttpEnable(temp.getHttpEnable());
			pl.setHttpsEnable(temp.getHttpsEnable());
			pl.setHttpExist(temp.getHttpExist());
			pl.setHttpExistContent(temp.getHttpExistContent());
			return;
		}
		if (pl.getLinkUrl().startsWith("http://")) {
			// 检查http支持情况以及是否包含http写死的情况
			OkhttpUtils.getInstance().doGet(pl.getLinkUrl(), new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					pl.setHttpEnable(0);
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					pl.setHttpEnable(1);
					if(pl.getPageType()==PageTypeEnum.CSS.getPageType()
							||pl.getPageType()==PageTypeEnum.JS.getPageType()) {
						String body=response.body().string();
						String https=findHttpAbs(body);
						if(StringUtils.isNotBlank(https)) {
							pl.setHttpExist(1);
							pl.setHttpExistContent(https);
						}else {
							pl.setHttpExist(0);
							pl.setHttpExistContent("");
							
						}
					}
					RockUtils.put(pl.getLinkUrl(), JSON.toJSONString(pl));
					response.close();
				}
				
			});
			// 检查时候支持https
			String url = pl.getLinkUrl().replace("http://", "https://");
			checkHttpsHost(url, host, pl);
		}
		else if (pl.getLinkUrl().startsWith("https://")) {
			// 检查http支持情况以及是否包含http写死的情况
			OkhttpUtils.getInstance().doGet(pl.getLinkUrl(), new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					pl.setHttpsEnable(0);
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					pl.setHttpsEnable(1);
					if(pl.getPageType()==PageTypeEnum.CSS.getPageType()
							||pl.getPageType()==PageTypeEnum.JS.getPageType()) {
						String body=response.body().string();
						String https=findHttpAbs(body);
						if(StringUtils.isNotBlank(https)) {
							pl.setHttpExist(1);
							pl.setHttpExistContent(https);
						}else {
							pl.setHttpExist(0);
							pl.setHttpExistContent("");
							
						}
					}
					RockUtils.put(pl.getLinkUrl(), JSON.toJSONString(pl));
					response.close();
				}

			});
		}

	}

	public static List<PageLink> extractLinks(String url) {
		String base = getBaseURL(url);
		List<PageLink> result = new LinkedList<>();
		HtmlUnitDriver driver = new HtmlUnitDriver();
		driver.setJavascriptEnabled(true);
		driver.get(url);
		List<WebElement> webElements = driver.findElementsByTagName("link");
		System.out.println("--------------link------------------------");
		addUrl(result, webElements, PageTypeEnum.CSS.getPageType(), base, url);
		webElements = driver.findElementsByTagName("script");
		System.out.println("--------------script------------------------");
		addUrl(result, webElements, PageTypeEnum.JS.getPageType(), base, url);
		webElements = driver.findElementsByTagName("img");
		System.out.println("--------------img------------------------");
		addUrl(result, webElements, PageTypeEnum.IMG.getPageType(), base, url);
		webElements = driver.findElementsByTagName("a");
		System.out.println("--------------a------------------------");
		addUrl(result, webElements, PageTypeEnum.HTML.getPageType(), base, url);
		return result;
	}
}
