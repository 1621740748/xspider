package fund.jrj.com.xspider.dao;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;

import fund.jrj.com.xspider.bo.PageLink1;

@DB
public interface PageLink1Dao {
	@SQL("insert into page_link1_#{:2} "
			+ "(link_url ,link_host ,link_host_path ,link_parent_url "
			+ ",link_parent_host ,link_parent_host_path "
			+ ",page_type ,auto_adapt ,http_exist ) "
			+ "values(:1.linkUrl ,:1.linkHost ,:1.linkHostPath ,:1.linkParentUrl " + 
			"	,:1.linkParentHost ,:1.linkParentHostPath" + 
			",:1.pageType ,:1.autoAdapt ,:1.httpExist )")
	void add(PageLink1 p,String host);

}
