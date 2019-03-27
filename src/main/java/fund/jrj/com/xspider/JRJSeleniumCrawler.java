/*
 * Copyright (C) 2015 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package fund.jrj.com.xspider;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cn.edu.hfut.dmic.webcollector.crawldb.DBManager;
import cn.edu.hfut.dmic.webcollector.crawler.Crawler;
import cn.edu.hfut.dmic.webcollector.fetcher.Executor;
import cn.edu.hfut.dmic.webcollector.fetcher.NextFilter;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.RocksDBManager;
import fund.jrj.com.xspider.bo.PageLink1;
import fund.jrj.com.xspider.constants.PageTypeEnum;
import fund.jrj.com.xspider.dao.PageLink1Dao;
import fund.jrj.com.xspider.filter.JRJNextFilter;
import fund.jrj.com.xspider.utils.DBUtils;
import fund.jrj.com.xspider.utils.ExtractUtils;


/**
 * 本教程演示如何利用WebCollector爬取javascript生成的数据
 *
 * @author hu
 */
public class JRJSeleniumCrawler {
  static {
  //禁用Selenium的日志
  Logger logger = Logger.getLogger("com.gargoylesoftware.htmlunit");
  logger.setLevel(Level.OFF);
}
    public static void main(String[] args) throws Exception {
        Executor executor = new Executor() {
            @Override
            public void execute(CrawlDatum datum, CrawlDatums next) throws Exception {
            	if(datum==null||datum.url()==null) {
            		return;
            	}
                List<PageLink1> links=ExtractUtils.extractLinks(datum.url());
                for(PageLink1 pl:links) {
                	if(pl.getPageType()==PageTypeEnum.HTML.getPageType()
                			||pl.getPageType()==PageTypeEnum.IFRAME.getPageType()) {
                		if(pl.getLinkUrl().startsWith("http://fund.jrj.com.cn")
                			&&StringUtils.isNotBlank(pl.getLinkParentUrl())
                			&&!pl.getLinkUrl().toLowerCase().endsWith(".pdf")
                			&&!pl.getLinkUrl().toLowerCase().endsWith(".mp4")) {
                			next.add(pl.getLinkUrl());
                		}
                	}
        			//检查资源是否包含http写死的情况
        			if (
        					pl.getPageType()==PageTypeEnum.JS.getPageType()) {
        	
        				ExtractUtils.checkJscssExistHttp(pl);
        			}
                }
            	PageLink1Dao plDao=DBUtils.getInstance().create(PageLink1Dao.class);
            	plDao.add(links);
            }
        };
        NextFilter filter=new JRJNextFilter();
        //创建一个基于伯克利DB的DBManager
        DBManager manager = new RocksDBManager("crawl");
        
        //创建一个Crawler需要有DBManager和Executor
        List<String> seeds= FileUtils.readLines(
        		new File(JRJSeleniumCrawler.class.getResource("").getPath()+"fund_seed.txt")
        		,"utf-8");
        Crawler crawler = new Crawler(manager, executor);
        for(String seed:seeds) {
        	if(StringUtils.isNotBlank(seed)) {
        		crawler.addSeed(seed);
        	}
        }
        crawler.setNextFilter(filter);
        crawler.setThreads(5);
        crawler.getConf().setExecuteInterval(200);
        crawler.start(6);
    }

}
