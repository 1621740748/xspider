package fund.jrj.com.xspider.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;


/**
 * 去重规则生成
 * @author huangyan
 *
 */
public class RemoveDuplicateUtils {
	private char[] chars;
	private String[] replaceRules;
	
	public RemoveDuplicateUtils(char[] chars, String[] replaceRules) {
		super();
		this.chars = chars;
		this.replaceRules = replaceRules;
	}
	private  List<String> split(String src){
		List<String> result=new LinkedList<>();
		if(src==null) {
			return result;
		}
		StringBuilder temp=new StringBuilder();
		for(int i=0;i<src.length();i++) {
			if(ArrayUtils.contains(chars, src.charAt(i))) {
				if(temp.length()!=0) {
					result.add(temp.toString());
					temp.setLength(0);
				}
				result.add(""+src.charAt(i));
			}else {
				temp.append(src.charAt(i));
			}
		}
		if(temp.length()>0) {
			result.add(temp.toString());
		}
		return result;

	}
	private  String toRule(String urlpath) {
		List<String> fs=this.split(urlpath);
		StringBuilder temp =new StringBuilder();
		for(String s:fs) {
			boolean find=false;
			for(String rr:this.replaceRules)	{
				if(s.matches(rr)) {
					find=true;
					temp.append(rr);
				}
			}
			if(!find) {
				temp.append(s);
			}
		
		}
		String rule=temp.toString();	
		return rule;
	}
	public  String toParamsRule(String urlpath) {
		List<String> fs=this.split(urlpath);
		StringBuilder temp =new StringBuilder();
		int count=0;
		for(String s:fs) {
			boolean find=false;
			for(String rr:this.replaceRules)	{
				if(s.matches(rr)) {
					find=true;
					count++;
					temp.append("{{").append(count).append("}}");
				}
			}
			if(!find) {
				temp.append(s);
			}
		
		}
		String rule=temp.toString();	
		return rule;
	}
	public  Map<String,String> toParamsMap(String urlpath) {
		List<String> fs=this.split(urlpath);
		Map<String,String> result=new HashMap<>();
		int count=0;
		for(String s:fs) {
			boolean find=false;
			for(String rr:this.replaceRules)	{
				if(s.matches(rr)) {
					find=true;
					count++;
					result.put(String.valueOf(count), s);
				}
			}
		}
		return result;
		
	}
	public  List<String> tidyByRules(List<String>files){
		Map<String,String>uses=new HashMap<>();
		List<String> result=new LinkedList<>();
		for(String f:files) {
			String fTrim=f;
			if(fTrim.contains("?")) {
				int index=fTrim.indexOf("?");
				if(index>0) {
					fTrim=fTrim.substring(0,index);
				}
			}
			String rule=this.toRule(fTrim);
			if(uses.get(rule)==null) {
				uses.put(rule, "1");
				result.add(f);
			}
		}
		return result;
	}

}