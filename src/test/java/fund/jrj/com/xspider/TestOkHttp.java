package fund.jrj.com.xspider;

import java.io.IOException;

import com.alibaba.fastjson.JSON;

import fund.jrj.com.xspider.utils.OkhttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TestOkHttp {

	public static void main(String[] args) {
		String url="http://fund.jrj.com.cn";
		OkhttpUtils.getInstance().doGet(url, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				System.out.println(response.code());
				System.out.println(response.body().string());
				System.out.println(JSON.toJSONString(response));
			}

		});
		
	}

}
