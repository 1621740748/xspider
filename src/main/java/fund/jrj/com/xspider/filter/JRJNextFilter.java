package fund.jrj.com.xspider.filter;

import java.util.HashSet;
import java.util.Map;
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
        if (items.contains(path)) {
        	Map<String,String> params=ExtractUtils.getParams(url);
        	if(params.isEmpty()) {
        		//已经存在并且无参数
        		return null;
        	}else {
        		boolean ok=false;
        		boolean oneOk=true;
        		Map<String,Set<String>> store=items.get(path);
        		if(store==null) {
        			store=new ConcurrentHashMap<>();
        		}
        		for(Map.Entry<String, String> e:params.entrySet()) {
        			Set<String> storeElem=store.get(e.getKey());
        			if(storeElem==null) {
        				storeElem =new HashSet<>();
        				ok=true;
        				storeElem.add(e.getValue());
        			}else if(storeElem.size()<SIZE&&!storeElem.contains(e.getValue())) {
        				ok=true;
        				storeElem.add(e.getValue());
        			}else if(storeElem.contains(e.getValue())) {
        				continue;
        			}
        		}
        		if(ok) {
        			return nextItem;
        		}
        	}
        } else {
        	Map<String,String> params=ExtractUtils.getParams(url);
        	if(params.isEmpty()) {
        		items.put(path, null);
        	}else {
        		ConcurrentHashMap<String,Set<String>> item=new ConcurrentHashMap<>();
        		params.forEach((key,value)->{
        			Set s=new HashSet();
        			s.add(value);
        			item.putIfAbsent(key, s);
        		});
        	}
            return nextItem;
        }
        return null;
    }

}
