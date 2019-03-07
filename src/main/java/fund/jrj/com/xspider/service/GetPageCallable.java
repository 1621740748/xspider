package fund.jrj.com.xspider.service;

import java.util.concurrent.Callable;

import com.google.common.util.concurrent.RateLimiter;

import fund.jrj.com.xspider.bo.PageResult;
import fund.jrj.com.xspider.utils.OkhttpUtils;

public class GetPageCallable implements Callable<PageResult>{
	private static     RateLimiter limiter = RateLimiter.create(8);
	private String url;
	
	public GetPageCallable(String url) {
		this.url=url;
	}
	@Override
	public PageResult call() throws Exception {
		limiter.acquire();
		PageResult p=OkhttpUtils.getInstance().getUrl(url);
		return p;
	}

}
