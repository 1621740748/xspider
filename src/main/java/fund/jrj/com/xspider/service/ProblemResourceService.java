package fund.jrj.com.xspider.service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.util.concurrent.RateLimiter;

import fund.jrj.com.xspider.bo.PageResources;
import fund.jrj.com.xspider.bo.PageResult;
import fund.jrj.com.xspider.bo.Resources;
import fund.jrj.com.xspider.dao.PageResourcesDao;
import fund.jrj.com.xspider.dao.ResourcesDao;
import fund.jrj.com.xspider.utils.DBUtils;
import fund.jrj.com.xspider.utils.ExtractUtils;
import fund.jrj.com.xspider.utils.OkhttpUtils;
import fund.jrj.com.xspider.utils.PageUtils;
public class ProblemResourceService {
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
	private static     RateLimiter limiter = RateLimiter.create(10);
	private static final  Map<String,Integer> urlMap=new ConcurrentHashMap<>();
	private static File logFile=new File("cache/link_log");
	public static  void  findProblemResource(String pUrl) {
		try {
			FileUtils.write(logFile, pUrl+"\n","utf-8",true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		List<String> rs=PageUtils.getResourceUrls(pUrl);
		List<CompletableFuture<Void>> futureList=new LinkedList<>();
		List<PageResult>jscssFiles=new LinkedList<>();
		List<Resources> resList=new LinkedList<>();
		List<PageResources>prList=new LinkedList<>();
		for(String url:rs) {
			String hash=DigestUtils.sha384Hex(url);
			PageResources prbo=new PageResources();
			prbo.setPageType(1);
			prbo.setPageUrl(pUrl);
			prbo.setResHash(hash);
			prList.add(prbo);
			if(urlMap.get(hash)==null) {

				CompletableFuture<Void> future=CompletableFuture.supplyAsync(()->{
					limiter.acquire();
					PageResult p=OkhttpUtils.getInstance().getUrl(url);
					return p;
				},fixedThreadPool).thenAcceptAsync(pr->{
					Resources res=new Resources();
					res.setUrl(pr.getUrl());
					res.setHost(ExtractUtils.getHost(pr.getUrl()));
					res.setHostPath(ExtractUtils.getHostAndPath(pr.getUrl()));
					if(pr.getUrl().startsWith("http://")) {
						PageResult p=pr;
						if(p.getOk()==1&&p.getType()==1) {
							jscssFiles.add(p);
						}
						res.setHttpEnable(p.getOk());
						PageResult p1=OkhttpUtils.getInstance().getUrl(pr.getUrl().replace("http://", "https://"));
						res.setHttpsEnable(p1.getOk());
						res.setTheSame(p.equals(p1)?1:0);
					}else if (pr.getUrl().startsWith("https://")) {
						PageResult p=pr;
						if(p.getOk()==1 &&p.getType()==1) {
							jscssFiles.add(p);
						}
						res.setHttpsEnable(p.getOk());
						PageResult p1=OkhttpUtils.getInstance().getUrl(pr.getUrl().replace("https://", "http://"));
						res.setHttpEnable(p1.getOk());
						res.setTheSame(p.equals(p1)?1:0);
					}
					resList.add(res);
					urlMap.put(hash, res.getHttpEnable()<<2|res.getHttpsEnable()<<1|res.getTheSame()&0b111);
				},fixedThreadPool);
				futureList.add(future);
			}else {
				Integer status=urlMap.get(hash);
				if(status!=null) {
					Resources res=new Resources();
					res.setUrl(url);
					res.setHost(ExtractUtils.getHost(url));
					res.setHostPath(ExtractUtils.getHostAndPath(url));
					res.setHttpEnable((status&0b100)>>2);
					res.setHttpsEnable((status&0b010)>>1);
					res.setTheSame(status&0b001);
					res.setHash(hash);
					resList.add(res);
				}
			}
		}
		if(!futureList.isEmpty()) {
			CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
		}

		CompletableFuture<PageResult> futureHttps=CompletableFuture.supplyAsync(()->{
			PageResult page=OkhttpUtils.getInstance().getUrl(pUrl);
			return page;
		});
		List<String> linksForHttps=PageUtils.getResourceUrls(pUrl.replace("http://", "https://"));
		//查找没有问题的http资源，在http请求中有，在https请求中也有，为了防止一些页面有时间戳造成不是同一个链接，仅使用host+path比较
		resList.stream().forEach(r->{
			boolean find=linksForHttps.stream().anyMatch(s->{
				String httpsHostPath=ExtractUtils.getHostAndPath(s);
				if(r.getHostPath()!=null&&httpsHostPath.equals(r.getHostPath())) {
					return true;
				}
				return false;
			});
			if(find) {
				r.setProblemType(0);
			}else {
				r.setProblemType(1);
			}
		});

		try {
			//找出在html中自动加载的资源
			PageResult page = futureHttps.get();
			resList.parallelStream().forEach(r->{
				String hostPath=ExtractUtils.getHostAndPath(r.getUrl());
				if(page.getOk()==1&&page.getType()==1&&page.getContent()!=null&&StringUtils.isNotBlank(hostPath)&&page.getContent().indexOf(hostPath)!=-1) {
					r.setLoadType(1);
				}else {
					r.setLoadType(2);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		ResourcesDao resDao=DBUtils.getInstance().create(ResourcesDao.class);
		resDao.add(resList);
		PageResourcesDao prDao=DBUtils.getInstance().create(PageResourcesDao.class);
		prDao.add(prList);		
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
