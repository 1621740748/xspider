package fund.jrj.com.xspider.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class PageUtils {
	public static List<String> getResourceUrls(String pUrl){
		List<String> resultList=new LinkedList<>();
		BufferedReader buff = null;
		Process p = null;
		try {
			//打印所有url自动加载的资源
			String path="python "+PageUtils.class.getResource("").getPath()+"br1.py "+pUrl.trim();
			p = Runtime.getRuntime().exec(path);
			p.waitFor();
			//用流读出来
			buff = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String temp = null;
			//遍历
			//跳过第一行
			buff.readLine();
			while ((temp=buff.readLine())!=null) {
				if(temp.startsWith("https://")||temp.startsWith("http://")) {
					resultList.add(temp);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			if(buff!=null){
				try {
					buff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return resultList;
	}
	public static List<String> getResourceUrlsV3(String pUrl){
		List<String> resultList=new LinkedList<>();
		BufferedReader buff = null;
		Process p = null;
		try {
			//打印所有url自动加载的资源
			String path="/usr/bin/phantomjs  "+PageUtils.class.getResource("").getPath()+"n1.js "+pUrl.trim();
			p = Runtime.getRuntime().exec(path);
			p.waitFor();
			//用流读出来
			buff = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String temp = null;
			//遍历
			//跳过第一行
			buff.readLine();
			while ((temp=buff.readLine())!=null) {
				if(temp.startsWith("https://")||temp.startsWith("http://")) {
					resultList.add(temp);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			if(buff!=null){
				try {
					buff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return resultList;
	}	
	public static void main(String[]args) {
		String url="http://fund.jrj.com.cn";
		List<String> results=PageUtils.getResourceUrls(url);
		System.out.println(StringUtils.join(results,"\n"));
	}
}
