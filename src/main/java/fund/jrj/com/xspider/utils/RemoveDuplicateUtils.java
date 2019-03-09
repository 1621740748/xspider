package fund.jrj.com.xspider.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;


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
	private  String toRule(String file) {
		List<String> fs=this.split(file);
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
	public  List<String> tidyByRules(List<String>files){
		Map<String,String>uses=new HashMap<>();
		List<String> result=new LinkedList<>();
		for(String f:files) {
			String rule=this.toRule(f);
			if(uses.get(rule)==null) {
				uses.put(rule, "1");
				result.add(f);
			}
		}
		return result;
	}

	public static void main(String[] args) throws IOException {
		File links=new File("cache/links");
		List<String> files=FileUtils.readLines(links,"utf-8");
		char[] seps= new char[] {'/','.',',','?','=','-'};
		String[] rr=new String[] {"\\d+","0000000000000[0-9,a-z,A-Z]{5}","djdj\\d+","qnj\\d+","jjgc\\d+"};
		//		System.out.println(StringUtils.join(files,"\n"));
		RemoveDuplicateUtils rd=new RemoveDuplicateUtils(seps,rr);
		//rules.add(0, "http://fund.jrj.com.cn/archives,\\\\d+,gg,[0-9,a-z,A-Z].shtml");
		List<String> result=rd.tidyByRules(files);
	   System.out.println(StringUtils.join(result,"\n"));
	   System.out.println("result size:"+result.size());
	   System.out.println("files size:"+files.size());
}

}
