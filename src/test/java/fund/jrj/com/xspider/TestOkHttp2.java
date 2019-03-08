package fund.jrj.com.xspider;

import fund.jrj.com.xspider.utils.OkhttpUtils;

public class TestOkHttp2 {

	public static void main(String[] args) {
		//String url = "http://push.zhanzhang.baidu.com/push.js";
		//String url="https://ss0.bdstatic.com/-0U0b8Sm1A5BphGlnYG/tam-ogel/f17e6319f90c9ddfe97d2d97f6d28066_60_60.jpg";
		String url="https://cashier.jrj.com.cn";
		OkhttpUtils.getInstance().getUrl(url);

	}

}
