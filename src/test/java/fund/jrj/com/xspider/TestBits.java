package fund.jrj.com.xspider;

public class TestBits {

	public static void main(String[] args) {
		Integer a=(1<<2|0<<1|1)&0b111;
		System.out.println(a);
		Integer b=(a&0b100)>>2;
		System.out.println(b);

	}

}
