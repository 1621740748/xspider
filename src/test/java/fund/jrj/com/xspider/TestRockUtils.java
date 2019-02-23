package fund.jrj.com.xspider;

import fund.jrj.com.xspider.utils.RockUtils;

public class TestRockUtils {

	public static void main(String[] args) {
		for(int i=0;i<10;i++) {
			RockUtils.put("key"+i, "value"+i);
		}
		for(int i=0;i<10;i++) {
			System.out.println(RockUtils.get("key"+i));
		}

	}

}
