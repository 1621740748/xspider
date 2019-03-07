package fund.jrj.com.xspider;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fund.jrj.com.xspider.bo.PageResult;
import fund.jrj.com.xspider.utils.ExtractUtils;
import fund.jrj.com.xspider.utils.OkhttpUtils;
import fund.jrj.com.xspider.utils.PageUtils;

public class TestGetResourceUrls2 {

	public static void main(String[] args) {
		String pUrl="http://fund.jrj.com.cn";
		List<String> rs=PageUtils.getResourceUrls(pUrl);
		System.out.println(StringUtils.join(rs,"\n"));
		System.out.println("rs:"+rs.size());
		for(String url:rs) {
			System.out.println("url:"+url);
			System.out.println("host:"+ExtractUtils.getHost(url));
			System.out.println("path:"+ExtractUtils.getHostAndPath(url));
			if(url.startsWith("http://")) {
				PageResult p=OkhttpUtils.getInstance().getUrl(url);
				System.out.println("http support:"+(p.getOk()==1?"yes":"no"));
				PageResult p1=OkhttpUtils.getInstance().getUrl(url.replace("http://", "https://"));
				System.out.println("https support:"+(p1.getOk()==1?"yes":"no"));
				System.out.println("content the same:"+(p.equals(p1)?"yes":"no"));
			}else if (url.startsWith("https://")) {
				PageResult p=OkhttpUtils.getInstance().getUrl(url);
				System.out.println("https support:"+(p.getOk()==1?"yes":"no"));
				PageResult p1=OkhttpUtils.getInstance().getUrl(url.replace("https://", "http://"));
				System.out.println("http support:"+(p1.getOk()==1?"yes":"no"));
				System.out.println("content the same:"+(p.equals(p1)?"yes":"no"));
			}
		}
	}
}
