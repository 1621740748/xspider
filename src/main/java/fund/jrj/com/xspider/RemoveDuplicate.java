package fund.jrj.com.xspider;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import fund.jrj.com.xspider.utils.RemoveDuplicateUtils;


/**
 * 去重规则生成
 * @author huangyan
 *
 */
public class RemoveDuplicate {


	public static void main(String[] args) throws IOException {
		File links=new File("cache/links3");
		List<String> files=FileUtils.readLines(links,"utf-8");
		char[] seps= new char[] {'/','.',',','?','=','-','_','#','&'};
		String[] rr=new String[] {
				"\\d+"
				,"0000000000000[0-9,a-z,A-Z]{5}"
				,"0000000010001[0-9,a-z,A-Z]{5}"
				,"djdj\\d+","qnj\\d+"
				,"jjgc\\d+"
				,"jjztc\\d+"
				,"pinyin[A-Z]"
				,"fund20yearqbx_[a-z]+"
				,"daoji\\d+"
				};
		//		System.out.println(StringUtils.join(files,"\n"));
		RemoveDuplicateUtils rd=new RemoveDuplicateUtils(seps,rr);
		//rules.add(0, "http://fund.jrj.com.cn/archives,\\\\d+,gg,[0-9,a-z,A-Z].shtml");
		List<String> result=rd.tidyByRules(files);
	   System.out.println(StringUtils.join(result,"\n"));
	   System.out.println("result size:"+result.size());
	   System.out.println("files size:"+files.size());
}

}
