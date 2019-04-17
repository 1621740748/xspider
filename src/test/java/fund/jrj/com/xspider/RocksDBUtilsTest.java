package fund.jrj.com.xspider;

import fund.jrj.com.xspider.utils.RocksUtils;

public class RocksDBUtilsTest {

	public static void main(String[] args) {
		RocksUtils r=RocksUtils.getInstance("stock");
		int i=0;
		while(i<1000) {
			r.put("key"+i, "value"+i);
			i++;
		}
		while(i>0) {
			i--;
			String s=r.get("key"+i);
			System.out.println(s);
		}

	}

}
