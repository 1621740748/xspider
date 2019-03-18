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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fund.jrj.com.xspider.bo.HttpResources;
import fund.jrj.com.xspider.bo.PageLink1;
import fund.jrj.com.xspider.constants.PageTypeEnum;
import fund.jrj.com.xspider.dao.HttpResourcesDao;
import fund.jrj.com.xspider.dao.PageLink1Dao;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ExtractUtils {
	public static Map<String, Boolean> hostHttpsMap = new HashMap<>();
	public static final Integer MAX_INFOSIZE = 2048;
	public static Pattern HTTPURL = Pattern.compile("(http://(?:[a-z,A-Z,0-9]+\\.){1,6}[^\"\'\\s]+)"); // 正则表达式

	private static String getBaseURL(String url) {
		try {
			URL u = new URL(url);
			String base = u.getProtocol() + "://" + u.getHost();
			if (u.getPort()!=-1&&u.getPort() != 80 && u.getProtocol().equals("http")) {
				base = base + ":" + u.getPort();
			} else if (u.getPort()!=-1&&443 != u.getPort() && u.getProtocol().equals("https")) {
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

	public static void checkHostSupportProtocal(String url, String host, PageLink1 pl) {
		if (StringUtils.isBlank(url)) {
			return;
		}
		if (!hostHttpsMap.containsKey(url)) {
			boolean httpEnable = false;
			boolean httpsEnable = false;
			if (url.startsWith("http://")) {
				httpEnable = OkhttpUtils.getInstance().checkUrlOk(url);
				httpsEnable = OkhttpUtils.getInstance().checkUrlOk(url.replace("http://", "https://"));
			} else if (url.startsWith("https://")) {
				httpsEnable = OkhttpUtils.getInstance().checkUrlOk(url);
				httpEnable = OkhttpUtils.getInstance().checkUrlOk(url.replace("https://", "http://"));
			}
			try {
				HttpResources hr = new HttpResources();
				hr.setLinkUrl(url);
				hr.setLinkHost(host);
				hr.setPageType(pl.getPageType());
				hr.setHttpEnable(httpEnable ? 1 : 0);
				hr.setHttpsEnable(httpsEnable ? 1 : 0);
				HttpResourcesDao hrDao = DBUtils.getInstance().create(HttpResourcesDao.class);
				hrDao.add(hr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			hostHttpsMap.put(url, true);
		}

	}

	private static List<PageLink1> getAddUrl(List<Element> webElements, Integer pageType, String base,
			String parentUrl) {
		List<PageLink1> addList = new LinkedList<>();
		for (Element we : webElements) {
			PageLink1 p = new PageLink1();
			p.setPageType(pageType);
			String u = null;
			if (pageType == PageTypeEnum.CSS.getPageType() || pageType == PageTypeEnum.HTML.getPageType()) {
				u = we.attr("href");
			} else if (pageType == PageTypeEnum.JS.getPageType() || pageType == PageTypeEnum.IMG.getPageType()) {
				u = we.attr("src");
			}
			if (u != null && u.startsWith("//")) {
				p.setAutoAdapt(1);
			} else {
				p.setAutoAdapt(0);
			}
			String linkUrl = getAbsUrl(base, u);
			if (StringUtils.isBlank(linkUrl)) {
				continue;
			}
			p.setLinkUrl(linkUrl);
			p.setLinkHost(ExtractUtils.getHost(linkUrl));
			p.setLinkHostPath(ExtractUtils.getHostAndPath(linkUrl));
			p.setLinkParentUrl(parentUrl);
			p.setLinkParentHost(ExtractUtils.getHost(parentUrl));
			p.setLinkParentHostPath(ExtractUtils.getHostAndPath(parentUrl));
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

	public static String getHostAndPath(String url) {
		if (StringUtils.isBlank(url)) {
			return "";
		}
		try {
			URL u = new URL(url);
			return u.getProtocol()+"://"+u.getHost()+((u.getDefaultPort()==u.getPort()||u.getPort()==-1)?"":":"+u.getPort())+u.getPath();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return "";
	}
	private static Map<String,String> Split(String urlparam){
		   Map<String,String> map = new HashMap<String,String>();
		   if(StringUtils.isBlank(urlparam)) {
			   return map;
		   }
		   String[] param =  urlparam.split("&");
		   for(String keyvalue:param){
		      String[] pair = keyvalue.split("=");
		      if(pair.length==2){
		         map.put(pair[0], pair[1]);
		      }
		   }
		   return map;
		}

	public static Map<String,String> getParams(String url) {
		Map<String,String> result=new HashMap<>();
		if (StringUtils.isBlank(url)) {
			return result;
		}
		try {
			URL u = new URL(url);
			return Split(u.getQuery());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static List<String> findHttpAbs(String body) {
		List<String> result = new LinkedList<>();
		Matcher m = HTTPURL.matcher(body); // 操作的字符串
		while (m.find()) {
			result.add(m.group(1));
		}
		return result;
	}

	/**
	 * 检查资源链接内容是否包含http
	 * 
	 * @param pl
	 */
	public static void checkJscssExistHttp(final PageLink1 pl) {
		// 检查是否已经分析过该资源
		String value = RockUtils.get(pl.getLinkUrl());
		if (StringUtils.isNotBlank(value)) {
			return;
		}
		try {
			// 检查http支持情况以及是否包含http写死的情况
			OkhttpUtils.getInstance().doGet(pl.getLinkUrl(), new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					if (pl.getPageType() == PageTypeEnum.CSS.getPageType()
							|| pl.getPageType() == PageTypeEnum.JS.getPageType()) {
						String body = response.body().string();
						List<String> httpLinks = findHttpAbs(body);
						if (httpLinks != null && httpLinks.size() > 0) {
							List<PageLink1> jsLinks = new LinkedList<>();
							for (String l : httpLinks) {
								if (StringUtils.isBlank(l)) {
									continue;
								}
								String host = ExtractUtils.getHost(l);
								if (StringUtils.isBlank(host)) {
									continue;
								}
								PageLink1 p = new PageLink1();
								p.setAutoAdapt(0);
								p.setHttpExist(1);
								p.setLinkUrl(l);
								p.setPageType(PageTypeEnum.JS.getPageType());
								p.setLinkHost(host);
								p.setLinkHostPath(ExtractUtils.getHostAndPath(l));
								p.setLinkParentHost(ExtractUtils.getHost(pl.getLinkUrl()));
								p.setLinkParentHostPath(ExtractUtils.getHostAndPath(pl.getLinkUrl()));
								p.setLinkParentUrl(pl.getLinkUrl());
								jsLinks.add(p);
							}
							if (!jsLinks.isEmpty()) {
								PageLink1Dao plDao = DBUtils.getInstance().create(PageLink1Dao.class);
								plDao.add(jsLinks);
							}
						}
					}
					RockUtils.put(pl.getLinkUrl(), "1");
					response.close();
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String joinHttpUrls(List<PageLink1> pls) {
		StringBuilder r = new StringBuilder();
		for (PageLink1 p : pls) {
			r.append(p.getLinkUrl());
			r.append("\n");
		}
		String result = r.toString();
		return StringUtils.strip(result, "\n");
	}

	public static List<String> extractLinksV3(String url) {
		List<String> result = new LinkedList<>();
		if (StringUtils.isBlank(url)) {
			return result;
		}
		String base = getBaseURL(url);
		try {
			Document document = Jsoup.connect(url).get();
			Elements links = document.select("a[href]");
			for (Element link : links) {

				String href = link.attr("href");
				String absUrl = ExtractUtils.getAbsUrl(base, href);
				if (StringUtils.isNotBlank(absUrl)) {
					result.add(absUrl);
				}
			}
			links = document.select("frame[src]");
			for (Element link : links) {

				String href = link.attr("src");
				String absUrl = ExtractUtils.getAbsUrl(base, href);
				if (StringUtils.isNotBlank(absUrl)) {
					result.add(absUrl);
				}
			}
			links = document.select("iframe[src]");
			for (Element link : links) {

				String href = link.attr("src");
				String absUrl = ExtractUtils.getAbsUrl(base, href);
				if (StringUtils.isNotBlank(absUrl)) {
					result.add(absUrl);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static List<PageLink1> extractLinks(String url) {
		List<PageLink1> result = new LinkedList<>();
		if (StringUtils.isBlank(url)) {
			return result;
		}
		String base = getBaseURL(url);

		try {
			Document document = Jsoup.connect(url).get();
			Elements links = document.select("link[href]");
			System.out.println("--------------link------------------------");
			List<PageLink1> cssList = getAddUrl(links, PageTypeEnum.CSS.getPageType(), base, url);
			links = document.select("script[src]");
			System.out.println("--------------script------------------------");
			List<PageLink1> jsList = getAddUrl(links, PageTypeEnum.JS.getPageType(), base, url);
			links = document.select("img[src]");
			System.out.println("--------------img------------------------");
			List<PageLink1> imgList = getAddUrl(links, PageTypeEnum.IMG.getPageType(), base, url);
			links = document.select("a[href]");
			System.out.println("--------------a------------------------");
			List<PageLink1> htmlList = getAddUrl(links, PageTypeEnum.HTML.getPageType(), base, url);
			result.addAll(cssList);
			result.addAll(jsList);
			result.addAll(imgList);
			// 检查页面是否是有写死的http资源
			for (PageLink1 p : result) {
				if (p.getLinkUrl() != null && p.getLinkUrl().startsWith("http://")
						&& (p.getAutoAdapt() == null || p.getAutoAdapt() == 0)) {
					p.setHttpExist(1);
				}
			}
			result.addAll(htmlList);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}
}
