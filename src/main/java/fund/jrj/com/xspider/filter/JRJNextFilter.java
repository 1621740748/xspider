package fund.jrj.com.xspider.filter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.edu.hfut.dmic.webcollector.fetcher.NextFilter;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import fund.jrj.com.xspider.utils.ExtractUtils;

/**
 * @author hu
 */
public class JRJNextFilter  implements NextFilter {
	//保存已经爬取的url信息，用于去重。对于相同url，同名参数最多取SIZE个不同的值，超过认为重复，不再爬取
	private ConcurrentHashMap<String,ConcurrentHashMap<String,Set<String>>> items;
	//保留的最大参数值
	private static final int SIZE= 15;
    @Override
    public CrawlDatum filter(CrawlDatum nextItem, CrawlDatum referer) {
        String url = nextItem.url();
        String path=ExtractUtils.getHostAndPath(url);
        if (items.contains(url)) {
            return null;
        } else {
        	//url
            return nextItem;
        }
    }

}
