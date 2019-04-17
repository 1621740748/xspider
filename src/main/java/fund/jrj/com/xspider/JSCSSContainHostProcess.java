package fund.jrj.com.xspider;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import fund.jrj.com.xspider.bo.JscssContains;
import fund.jrj.com.xspider.bo.PageLink1;
import fund.jrj.com.xspider.dao.JscssContainsDao;
import fund.jrj.com.xspider.dao.PageLink1Dao;
import fund.jrj.com.xspider.utils.DBUtils;

public class JSCSSContainHostProcess {
	private static Pattern HOST=Pattern.compile("http://([^/\\s]+)");
	private static List<String> extractHosts(String src){
		List<String > result=new LinkedList<>();
		if(StringUtils.isBlank(src)) {
			return result;
		}
		Matcher m=HOST.matcher(src);
		while(m.find()) {
			result.add(m.group(1));
		}
		return result;
	}

}
