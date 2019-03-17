package fund.jrj.com.xspider;

import java.net.MalformedURLException;
import java.net.URL;

import fund.jrj.com.xspider.utils.ExtractUtils;

public class TestUrl {

	public static void main(String[] args) throws MalformedURLException {
		 URL u=new URL("https://www.baidu.com:9090");
		 System.out.println(u.getDefaultPort());
		 System.out.println(u.getPort());
		 String s="https://www.sohu.com?a=1&b=2";
		 System.out.println(ExtractUtils.getHostAndPath(s));
		 
	}

}
