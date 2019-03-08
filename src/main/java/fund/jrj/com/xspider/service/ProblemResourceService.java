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

import com.google.common.util.concurrent.RateLimiter;

import fund.jrj.com.xspider.bo.PageResources;
import fund.jrj.com.xspider.bo.PageResult;
import fund.jrj.com.xspider.bo.Resources;
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
		List<Resources> resList=new LinkedList<>();
		List<PageResources>prList=new LinkedList<>();
		for(String url:rs) {
			String hash=DigestUtils.sha384Hex(url);
			//如果是图像并且已经访问过，则不必再访问
			if(ImageUrlMap.get(hash)!=null) {
				continue;
			}
			PageResources prbo=new PageResources();
			prbo.setPageType(1);
			prbo.setPageUrl(pUrl);
			prbo.setResHash(hash);
			prList.add(prbo);
	
			CompletableFuture<Void> future=CompletableFuture.supplyAsync(()->{
				limiter.acquire();
				PageResult p=OkhttpUtils.getInstance().getUrl(url);
				if(p!=null&&p.getType()!=1) {
					ImageUrlMap.put(hash, p.getOk());
				}
				return p;
			},fixedThreadPool).thenAcceptAsync(pr->{
				Resources res=new Resources();
				res.setUrl(url);
				res.setHost(ExtractUtils.getHost(url));
				res.setHostPath(ExtractUtils.getHostAndPath(url));
				if(url.startsWith("http://")) {
					PageResult p=pr;
					if(p.getOk()==1&&p.getType()==1) {
						jscssFiles.add(p);
					}
					res.setHttpEnable(p.getOk());
					PageResult p1=OkhttpUtils.getInstance().getUrl(url.replace("http://", "https://"));
					res.setHttpsEnable(p1.getOk());
					res.setTheSame(p.equals(p1)?1:0);
				}else if (url.startsWith("https://")) {
					PageResult p=pr;
					if(p.getOk()==1 &&p.getType()==1) {
						jscssFiles.add(p);
					}
					res.setHttpsEnable(p.getOk());
					PageResult p1=OkhttpUtils.getInstance().getUrl(url.replace("https://", "http://"));
					res.setHttpEnable(p1.getOk());
					res.setTheSame(p.equals(p1)?1:0);
				}
				resList.add(res);
			},fixedThreadPool);
			futureList.add(future);
		}
		CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
		List<String> linksForHttps=PageUtils.getResourceUrls(pUrl.replace("http://", "https://"));
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
		PageResult page=OkhttpUtils.getInstance().getUrl(pUrl);
		List<String> problemUrlInHtml=new LinkedList<>();
		for(String url:problemUrlInPage) {
			if(page.getContent()!=null&&page.getContent().indexOf(url)!=-1) {
				problemUrlInHtml.add(url);
			}
		}
		List<String> problemUrlInJsAndCss=(List<String>) CollectionUtils.subtract(problemUrlInPage, problemUrlInHtml);
		
		
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
