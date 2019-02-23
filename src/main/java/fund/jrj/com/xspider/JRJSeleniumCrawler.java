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
import org.rocksdb.RocksDB;

import cn.edu.hfut.dmic.webcollector.crawldb.DBManager;
import cn.edu.hfut.dmic.webcollector.crawler.Crawler;
import cn.edu.hfut.dmic.webcollector.fetcher.Executor;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.RocksDBManager;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.RocksDBUtils;
import fund.jrj.com.xspider.bo.PageLink;
import fund.jrj.com.xspider.constants.PageTypeEnum;
import fund.jrj.com.xspider.dao.PageLinkDao;
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
                List<PageLink> links=ExtractUtils.extractLinks(datum.url());
                for(PageLink pl:links) {
                	if(pl.getPageType()==PageTypeEnum.HTML.getPageType()) {
                		if(pl.getLinkUrl().startsWith("http://fund.jrj.com.cn")) {
                			next.add(pl.getLinkUrl());
                		}
                	}
                }
            	PageLinkDao plDao=DBUtils.getInstance().create(PageLinkDao.class);
            	plDao.add(links);
            }
        };
        //创建一个基于伯克利DB的DBManager
        DBManager manager = new RocksDBManager("crawl");
        
        //创建一个Crawler需要有DBManager和Executor
        List<String> seeds= FileUtils.readLines(
        		new File(JRJSeleniumCrawler.class.getResource("").getPath()+"fund_seed2.txt")
        		,"utf-8");
        Crawler crawler = new Crawler(manager, executor);
        for(String seed:seeds) {
        	if(StringUtils.isNotBlank(seed)) {
        		crawler.addSeed(seed);
        	}
        }
        crawler.setThreads(5);
        crawler.getConf().setExecuteInterval(200);
        crawler.start(1);
    }

}