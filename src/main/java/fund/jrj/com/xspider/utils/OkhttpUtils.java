package fund.jrj.com.xspider.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fund.jrj.com.xspider.bo.PageResult;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpUtils {
	private static OkhttpUtils okhttpUtils;
	private final OkHttpClient okHttpClient;
	private static Logger log = LoggerFactory.getLogger(OkhttpUtils.class);
	private static  String cacheFileDir="cache/file/";
	private static  String cacheHtmlDir="cache/html/";
	// 构造方法要私有化
	private OkhttpUtils() {
		// 创建OkhttpClient
		okHttpClient = new OkHttpClient
				.Builder()
				.connectTimeout(2, TimeUnit.SECONDS)
				.readTimeout(2, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
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
	private  PageResult getUrlFromCache(String url) {
		String hash=DigestUtils.sha384Hex(url);
		File file=new File(cacheFileDir+hash);
		if(file.exists()) {
			try {
				String content=FileUtils.readFileToString(file, "utf-8");
				PageResult r=new PageResult();
				r.setUrl(url);
				r.setContent(content);
				r.setOk(1);
				r.setType(1);
				r.setImage(null);
				return r;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	private void storeUrlToCache(PageResult result) {
		if(result!=null&&result.getOk()==1&&result.getType()==1) {
			String hash=DigestUtils.sha384Hex(result.getUrl());
			File file=new File(cacheFileDir+hash);
			if(!file.exists()) {
				try {
					FileUtils.writeByteArrayToFile(file, result.getContent().getBytes("utf-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	public void storeHtml(String url,String content) {
		    if(StringUtils.isBlank(url)) {
		    	return;
		    }
		    String path=ExtractUtils.getPath(url);
			String hash=DigestUtils.md5Hex(url);
			File file=null;
		    if(StringUtils.isNotBlank(path)) {
		        File f=new File(cacheHtmlDir+path);
		        if(path.endsWith("/")) {
		        	if(!f.exists()) {
		        		f.mkdirs();
		        	}
	        		file=new File(cacheHtmlDir+path+hash);
		        }else {
		        	path=f.getParent();
		        	String name=f.getName();
		        	File p=new File(path);
		        	if(!p.exists()) {
		        		p.mkdirs();
		        	}
		        	file=new File(path+File.separator+hash+"_"+name);
		        }
		    }else {
		    	file=new File(cacheHtmlDir+hash);
		    }
		
			if(!file.exists()) {
				try {
					FileUtils.writeByteArrayToFile(file, content.getBytes("utf-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	public  PageResult getUrl(String url) {
		PageResult result=this.getUrlFromCache(url);
		if(result!=null) {
			return result;
		}
        Request request = new Request.Builder().url(url)
                .get().build();
        Call call = okHttpClient.newCall(request);
        result=new PageResult();
        result.setUrl(url);
        try {
            Response response = call.execute();
            result.setOk(0);
            if(response.isSuccessful()) {
            	result.setOk(1);
            	String type=response.body().contentType().type();
            	if(type!=null&&type.equals("text")) {
            		result.setType(1);
            		result.setContent(response.body().string());
            		result.setImage(null);
            	}else if(type!=null&&type.equals("image")) {
            		result.setType(2);
            		result.setContent(null);
            		result.setImage(response.body().bytes());
            	}
            }
            response.close();
           
        } catch (Exception e) {
           // e.printStackTrace();
        }
        this.storeUrlToCache(result);
		return result;
	}

}
