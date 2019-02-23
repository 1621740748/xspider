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
	public static final Integer MAX_INFOSIZE=2048;
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

	public static void checkHttpsHost(String url, String host, PageLink pl) {
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

	private static List<PageLink> getAddUrl(List<WebElement> webElements, Integer pageType, String base,
			String parentUrl) {
		List<PageLink> addList=new LinkedList<>();
		for (WebElement we : webElements) {
			PageLink p = new PageLink();
			p.setPageType(pageType);
			String u = null;
			if(pageType==PageTypeEnum.CSS.getPageType()
					||pageType==PageTypeEnum.HTML.getPageType()) {
				u=we.getAttribute("href");
			}else if(pageType==PageTypeEnum.JS.getPageType()
					||pageType==PageTypeEnum.IMG.getPageType()) 
			{
				u=we.getAttribute("src");
			}
			if (u != null && u.startsWith("//")) {
				p.setAutoAdapt(1);
			}else {
				p.setAutoAdapt(0);
			}
			String linkUrl = getAbsUrl(base, u);
			if (StringUtils.isBlank(linkUrl)) {
				continue;
			}
			p.setLinkUrl(linkUrl);
			p.setLinkParentUrl(parentUrl);
			System.out.println(linkUrl);
			addList.add(p);
		}
		return addList;
	}

	public static String getHost(String url) {
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
			return StringUtils.abbreviate(s, MAX_INFOSIZE);
		}
	}

	/**
	 * 检查资源链接内容是否包含http
	 * @param pl
	 */
	public static void checkJscssExistHttp(final PageLink pl) {
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

	}
	private static String joinHttpUrls(List<PageLink> pls) {
		StringBuilder r=new StringBuilder();
		for(PageLink p:pls) {
			r.append(p.getLinkUrl());
			r.append("\n");
		}
		String result= r.toString();
		return StringUtils.strip(result, "\n");
	}
	public static List<PageLink> extractLinks(String url) {
		List<PageLink> result = new LinkedList<>();
		if(StringUtils.isBlank(url)) {
			return result;
		}
		String base = getBaseURL(url);

		HtmlUnitDriver driver = new HtmlUnitDriver();
		driver.setJavascriptEnabled(true);
		driver.get(url);
		List<WebElement> webElements = driver.findElementsByTagName("link");
		System.out.println("--------------link------------------------");
		List<PageLink >cssList=getAddUrl(webElements, PageTypeEnum.CSS.getPageType(), base, url);
		webElements = driver.findElementsByTagName("script");
		System.out.println("--------------script------------------------");
		List<PageLink >jsList=getAddUrl(webElements, PageTypeEnum.JS.getPageType(), base, url);
		webElements = driver.findElementsByTagName("img");
		System.out.println("--------------img------------------------");
		List<PageLink >imgList=getAddUrl(webElements, PageTypeEnum.IMG.getPageType(), base, url);
		webElements = driver.findElementsByTagName("a");
		System.out.println("--------------a------------------------");
		List<PageLink >htmlList=getAddUrl( webElements, PageTypeEnum.HTML.getPageType(), base, url);
		result.addAll(cssList);
		result.addAll(jsList);
		result.addAll(imgList);
		//检查页面是否是有写死的http资源
		List<PageLink> httpAbs=new LinkedList();
		for(PageLink p:result) {
			if(p.getLinkUrl()!=null&&p.getLinkUrl().startsWith("http://")
					&&(p.getAutoAdapt()==null||p.getAutoAdapt()==0)) {
				httpAbs.add(p);
			}
		}
		result.addAll(htmlList);
		PageLink p=new PageLink();
		p.setAutoAdapt(0);
		p.setLinkUrl(url);
		p.setPageType(PageTypeEnum.HTML.getPageType());
		if(!httpAbs.isEmpty()) {
			p.setHttpExist(1);
			String s=joinHttpUrls(httpAbs);
			p.setHttpExistContent(StringUtils.abbreviate(s, MAX_INFOSIZE));
			
		}else {
			p.setHttpExist(0);
		}
		result.add(p);

		return result;
	}
}
