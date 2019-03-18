package fund.jrj.com.xspider.filter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.edu.hfut.dmic.webcollector.fetcher.NextFilter;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import fund.jrj.com.xspider.utils.ExtractUtils;
import fund.jrj.com.xspider.utils.RemoveDuplicateUtils;

/**
 * @author hu
 */
public class JRJNextFilter  implements NextFilter {
	//保存已经爬取的url信息，用于去重。对于相同url，同名参数最多取SIZE个不同的值，超过认为重复，不再爬取
	private ConcurrentHashMap<String,ConcurrentHashMap<String,Set<String>>> items;
	private RemoveDuplicateUtils rmDuplicateUtil;
	public JRJNextFilter() {
		items=new ConcurrentHashMap<>();
		char[] seps= new char[] {'/','.',',','?','=','-','_','#','&'};
		String[] rr=new String[] {
				"\\d+"
				,"0000000000000[0-9,a-z,A-Z]{5}"
				,"0000000010001[0-9,a-z,A-Z]{5}"
				,"djdj\\d+","qnj\\d+"
				,"jjgc\\d+"
				,"jjztc\\d+"
				,"pinyin[A-Z]"
				,"fund20yearqbx_[a-z]+"
				,"daoji\\d+"
				};
		//		System.out.println(StringUtils.join(files,"\n"));
		rmDuplicateUtil=new RemoveDuplicateUtils(seps,rr);
	}
	//保留的最大参数值
	private static final int SIZE= 15;
    @Override
    public CrawlDatum filter(CrawlDatum nextItem, CrawlDatum referer) {
        String url = nextItem.url();
        String path=ExtractUtils.getHostAndPath(url);
        String pathRule=rmDuplicateUtil.getPathVariableRule(path);
        if (items.contains(pathRule)) {
        	Map<String,String> params=rmDuplicateUtil.getParamsIncludePathariable(url);
        	if(params.isEmpty()) {
        		//已经存在并且无参数
        		return null;
        	}else {
        		boolean ok=false;
        		Map<String,Set<String>> store=items.get(pathRule);
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
        		items.put(pathRule, null);
        	}else {
        		ConcurrentHashMap<String,Set<String>> item=new ConcurrentHashMap<>();
        		params.forEach((key,value)->{
        			Set<String> s=new HashSet<>();
        			s.add(value);
        			item.putIfAbsent(key, s);
        		});
        	}
            return nextItem;
        }
        return null;
    }

}
