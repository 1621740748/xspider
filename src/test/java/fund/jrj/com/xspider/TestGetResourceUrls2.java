package fund.jrj.com.xspider;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fund.jrj.com.xspider.bo.PageResult;
import fund.jrj.com.xspider.service.GetPageCallable;
import fund.jrj.com.xspider.utils.ExtractUtils;
import fund.jrj.com.xspider.utils.OkhttpUtils;
import fund.jrj.com.xspider.utils.PageUtils;
public class TestGetResourceUrls2 {
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(8);
	   // 构建完成服务
    private static CompletionService<PageResult> completionService = new ExecutorCompletionService<PageResult>(
    		fixedThreadPool);

	public static void main(String[] args) {
		Long startTime=System.currentTimeMillis();
		String pUrl="http://fund.jrj.com.cn";
		List<String> rs=PageUtils.getResourceUrls(pUrl);
		List<Future<PageResult>> futureList=new LinkedList<>();
		for(String url:rs) {
			Future<PageResult>future=completionService.submit(new GetPageCallable(url));
			futureList.add(future);
		}
		System.out.println(StringUtils.join(rs,"\n"));
		System.out.println("rs:"+rs.size());
		List<PageResult>jscssFiles=new LinkedList<>();
		for(String url:rs) {
			System.out.println("url:"+url);
			System.out.println("host:"+ExtractUtils.getHost(url));
			System.out.println("path:"+ExtractUtils.getHostAndPath(url));
			if(url.startsWith("http://")) {
				PageResult p=OkhttpUtils.getInstance().getUrl(url);
				if(p.getOk()==1&&p.getType()==1) {
					jscssFiles.add(p);
				}
				System.out.println("http support:"+(p.getOk()==1?"yes":"no"));
				PageResult p1=OkhttpUtils.getInstance().getUrl(url.replace("http://", "https://"));
				System.out.println("https support:"+(p1.getOk()==1?"yes":"no"));
				System.out.println("content the same:"+(p.equals(p1)?"yes":"no"));
			}else if (url.startsWith("https://")) {
				PageResult p=OkhttpUtils.getInstance().getUrl(url);
				if(p.getOk()==1 &&p.getType()==1) {
					jscssFiles.add(p);
				}
				System.out.println("https support:"+(p.getOk()==1?"yes":"no"));
				PageResult p1=OkhttpUtils.getInstance().getUrl(url.replace("https://", "http://"));
				System.out.println("http support:"+(p1.getOk()==1?"yes":"no"));
				System.out.println("content the same:"+(p.equals(p1)?"yes":"no"));
			}
		}
		
		List<String> linksForHttps=PageUtils.getResourceUrls("https://fund.jrj.com.cn");
		System.out.println("-----------------------------------------------");
		System.out.println("find problem resources in page:");
		List<String> problemUrlInPage=new LinkedList<>();
		for(String url:linksForHttps) {
			if(url!=null&&url.startsWith("http://")) {
				problemUrlInPage.add(url);
			}
		}
		for(String url:rs) {
			if(url!=null&&url.startsWith("http://")) {
				if(CollectionUtils
						.exists(linksForHttps,
								u->u.equals(url)
								||u.equals(url.replace("http://", "https://")))) {
					problemUrlInPage.add(url);
				}
			}
		}
		System.out.println(StringUtils.join(problemUrlInPage, "\n"));
		System.out.println("size of problems in page:"+problemUrlInPage.size());
		System.out.println("-----------------------------------------------");
		System.out.println("find problem resources in html:");
		
		PageResult page=OkhttpUtils.getInstance().getUrl(pUrl);
		List<String> problemUrlInHtml=new LinkedList<>();
		for(String url:problemUrlInPage) {
			if(page.getContent()!=null&&page.getContent().indexOf(url)!=-1) {
				problemUrlInHtml.add(url);
			}
		}
		System.out.println(StringUtils.join(problemUrlInHtml, "\n"));
		System.out.println("size of problems in html:"+problemUrlInHtml.size());
		System.out.println("-----------------------------------------------");
		
		System.out.println("find problem resources in js and css");
		List<String> problemUrlInJsAndCss=new LinkedList<>();
		problemUrlInJsAndCss=(List<String>) CollectionUtils.subtract(problemUrlInPage, problemUrlInHtml);
		System.out.println(StringUtils.join(problemUrlInJsAndCss, "\n"));
		System.out.println("size of problems in js and css:"+ problemUrlInJsAndCss.size());
		System.out.println("-----------------------------------------------");
		
		for(String url:problemUrlInJsAndCss) {
			System.out.println("url content contains resource:"+url);
			for(PageResult pr:jscssFiles) {
				if(pr.getContent().contains(url)) {
					System.out.println(pr.getUrl());
				}
			}
			System.out.println("-----------------------------------------");
		}
		
		Long endTime=System.currentTimeMillis();
		System.out.println("take time:"+(endTime-startTime)/1000+" seconds");
	}
}
