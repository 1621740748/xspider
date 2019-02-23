package fund.jrj.com.xspider.utils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpUtils {
	private static OkhttpUtils okhttpUtils;
	private final OkHttpClient okHttpClient;
	private static Logger log = LoggerFactory.getLogger(OkhttpUtils.class);

	// 构造方法要私有化
	private OkhttpUtils() {
		// 创建OkhttpClient
		okHttpClient = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS).readTimeout(2, TimeUnit.SECONDS)
				.build();
	}

	// 懒汉式
	public static OkhttpUtils getInstance() {
		if (okhttpUtils == null) {
			okhttpUtils = new OkhttpUtils();
		}
		return okhttpUtils;
	}

	/**
	 * GET请求
	 *
	 * @param url
	 * @param params
	 */
	public void doGet(String url, Map<String, String> params, final Callback callback) {
		// 判断params是否为null
		if (params != null) {
			StringBuilder sb = new StringBuilder();
			if (!url.contains("?")) {
				sb.append("?");
			}
			// 拼接参数
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey());
				sb.append("=");
				sb.append(entry.getValue());
				sb.append("&");
			}
			// ?mobile=12354678954&password=123456&
			String s = sb.toString();
			String strParam = s.substring(0, s.length() - 1);
			url += strParam;
			log.info("url = " + url);
		}
		// 创建Request对象
		Request request = new Request.Builder().url(url).build();
		// 发送请求
		okHttpClient.newCall(request).enqueue(callback);
	}

	/**
	 * GET请求
	 *
	 * @param url
	 * @param params
	 */
	public void doGet(String url, final Callback callback) {
		// 创建Request对象
		Request request = new Request.Builder().url(url).build();
		// 发送请求
		okHttpClient.newCall(request).enqueue(callback);
	}

	/**
	 * GET请求
	 *
	 * @param url
	 */
	public boolean checkUrlOk(String url) {
		try {
			// 创建Request对象
			Request request = new Request.Builder().url(url).build();
			// 发送请求
			Call call = okHttpClient.newCall(request);

			Response response = call.execute();
			response.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
