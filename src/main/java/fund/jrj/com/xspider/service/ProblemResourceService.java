package fund.jrj.com.xspider.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.util.concurrent.RateLimiter;

import fund.jrj.com.xspider.bo.PageResult;
import fund.jrj.com.xspider.utils.ExtractUtils;
import fund.jrj.com.xspider.utils.OkhttpUtils;
import fund.jrj.com.xspider.utils.PageUtils;
public class ProblemResourceService {
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(8);
	private static     RateLimiter limiter = RateLimiter.create(200);
    private static Map<String,Integer> ImageUrlMap=new HashMap<>();
	public  void  findProblemResource(String pUrl) {
		List<String> rs=PageUtils.getResourceUrls(pUrl);
		List<CompletableFuture<Void>> futureList=new LinkedList<>();
		List<PageResult>jscssFiles=new LinkedList<>();
		for(String url:rs) {
			String hash=DigestUtils.sha384Hex(url);
			//如果是图像并且已经访问过，则不必再访问
			if(ImageUrlMap.get(hash)!=null) {
				continue;
			}
			CompletableFuture<Void> future=CompletableFuture.supplyAsync(()->{
				limiter.acquire();
				PageResult p=OkhttpUtils.getInstance().getUrl(url);
				if(p!=null&&p.getType()!=1) {
					ImageUrlMap.put(hash, p.getOk());
				}
				return p;
			},fixedThreadPool).thenAcceptAsync(pr->{
				System.out.println("url:"+url);
				System.out.println("host:"+ExtractUtils.getHost(url));
				System.out.println("path:"+ExtractUtils.getHostAndPath(url));
				if(url.startsWith("http://")) {
					PageResult p=pr;
					if(p.getOk()==1&&p.getType()==1) {
						jscssFiles.add(p);
					}
					System.out.println("http support:"+(p.getOk()==1?"yes":"no"));
					PageResult p1=OkhttpUtils.getInstance().getUrl(url.replace("http://", "https://"));
					System.out.println("https support:"+(p1.getOk()==1?"yes":"no"));
					System.out.println("content the same:"+(p.equals(p1)?"yes":"no"));
				}else if (url.startsWith("https://")) {
					PageResult p=pr;
					if(p.getOk()==1 &&p.getType()==1) {
						jscssFiles.add(p);
					}
					System.out.println("https support:"+(p.getOk()==1?"yes":"no"));
					PageResult p1=OkhttpUtils.getInstance().getUrl(url.replace("https://", "http://"));
					System.out.println("http support:"+(p1.getOk()==1?"yes":"no"));
					System.out.println("content the same:"+(p.equals(p1)?"yes":"no"));
				}
			},fixedThreadPool);
			futureList.add(future);
		}
		CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
		
		System.out.println(StringUtils.join(rs,"\n"));
		System.out.println("rs:"+rs.size());		
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
	}
	public static void main(String[] args) {
		Long startTime=System.currentTimeMillis();
		String pUrl="http://fund.jrj.com.cn";
		ProblemResourceService prs=new ProblemResourceService();
		prs.findProblemResource(pUrl);
		Long endTime=System.currentTimeMillis();
		System.out.println("take times:"+(endTime-startTime)/1000+" seconds");
		fixedThreadPool.shutdown();
	}
}
