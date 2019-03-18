package fund.jrj.com.xspider;

import java.util.concurrent.ConcurrentHashMap;

public class TestHashMap {

	public static void main(String[] args) {
		ConcurrentHashMap<String,String> m=new ConcurrentHashMap<>();
		m.compute("aaa", null);
		
	}

}
