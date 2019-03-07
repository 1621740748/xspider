package fund.jrj.com.xspider;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fund.jrj.com.xspider.utils.PageUtils;

public class TestGetResourceUrls {

	public static void main(String[] args) {
		String pUrl="http://fund.jrj.com.cn";
		List<String> rs=PageUtils.getResourceUrls(pUrl);
		System.out.println(StringUtils.join(rs,"\n"));
		System.out.println("rs:"+rs.size());
	}

}
