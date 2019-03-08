package fund.jrj.com.xspider;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Test {

	public static void main(String[] args) {
		String url="http://fund.jrj.com.cn";
		String md5=DigestUtils.sha384Hex(url);
		System.out.println(md5);

	}

}
