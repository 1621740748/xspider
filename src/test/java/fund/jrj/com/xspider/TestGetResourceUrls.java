package fund.jrj.com.xspider;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fund.jrj.com.xspider.utils.PageUtils;

public class TestGetResourceUrls {

	public static void main(String[] args) {
		String pUrl="http://fund.jrj.com.cn";
		List<String> rs=PageUtils.getResourceUrls(pUrl);
		System.out.println(StringUtils.join(rs,"\n"));
		System.out.println("rs:"+rs.size());
		
		String pUrl2="https://fund.jrj.com.cn";
		List<String> rs2=PageUtils.getResourceUrls(pUrl2);
		System.out.println("---------------------------");
		System.out.println(StringUtils.join(rs2,"\n"));
		System.out.println("rs2:"+rs2.size());
		System.out.println("---------------------------");
		List<String>rs3=(List<String>) CollectionUtils.intersection(rs, rs2);
		System.out.println(StringUtils.join(rs3,"\n"));
		System.out.println("rs3:"+rs3.size());
		System.out.println("---------------------------");
		
		List<String>rs4=(List<String>) CollectionUtils.subtract(rs, rs2);
		System.out.println(StringUtils.join(rs4,"\n"));
		System.out.println("rs4:"+rs4.size());
		System.out.println("---------------------------");
		
		List<String>rs5=(List<String>) CollectionUtils.subtract(rs2, rs);
		System.out.println(StringUtils.join(rs5,"\n"));
		System.out.println("rs5:"+rs5.size());
		System.out.println("---------------------------");
	}

}
