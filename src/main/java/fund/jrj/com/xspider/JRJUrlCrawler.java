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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.IOUtils;
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
import fund.jrj.com.xspider.filter.JRJNextFilter;
import fund.jrj.com.xspider.utils.ExtractUtils;


/**
 * 本教程演示如何利用WebCollector爬取javascript生成的数据
 *
 * @author hu
 */
public class JRJUrlCrawler {
  static {
  //禁用Selenium的日志
  Logger logger = Logger.getLogger("com.gargoylesoftware.htmlunit");
  logger.setLevel(Level.OFF);
}
  final static    BlockingQueue<String> urlQueue=new  LinkedBlockingQueue<>();
    public static void main(String[] args) throws Exception {
       	String scanUrl="https://stock.jrj.com.cn/";
    	String hostSingleName=ExtractUtils.getHostSingleName(scanUrl);
    	//DBUtils.createTable(hostSingleName);
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
                		pl.setLinkUrl(pl.getLinkUrl().replace("http://", "https://"));
                		if(pl.getLinkUrl().startsWith(scanUrl)
                			&&StringUtils.isNotBlank(pl.getLinkParentUrl())
                			&&!pl.getLinkUrl().toLowerCase().endsWith(".pdf")
                			&&!pl.getLinkUrl().toLowerCase().endsWith(".mp4")) {
                			next.add(pl.getLinkUrl());
                			urlQueue.put(pl.getLinkUrl());
                		}
                	}
                }
            }
        };
        Executors.newFixedThreadPool(1).submit(()->{
        	OutputStreamWriter f=null;
        	try {
				f=new OutputStreamWriter(new FileOutputStream(new File("/data/temp/urls.txt")));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	while(true) {
        		int i=0;
        		try {
        			//System.out.println("urlQueue Size:"+urlQueue.size());
					String url=urlQueue.take();
					i++;
					if(i%2000==0) {
						Thread.sleep(300);
						f.flush();
					}
					f.append(url).append("\n");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        });
        NextFilter filter=new JRJNextFilter();
        //创建一个基于伯克利DB的DBManager
        DBManager manager = new RocksDBManager("crawl");
        
        //创建一个Crawler需要有DBManager和Executor
        List<String> seeds= IOUtils.readLines(
        		JRJUrlCrawler.class.getResource("/fund_seed.txt").openStream()
        		,"utf-8");
        Crawler crawler = new Crawler(manager, executor);
        for(String seed:seeds) {
        	if(StringUtils.isNotBlank(seed)) {
        		crawler.addSeed(seed);
        	}
        }
        if(!seeds.contains(scanUrl)) {
        	crawler.addSeed(scanUrl);
        }
        crawler.setNextFilter(filter);
        crawler.setThreads(1);
        crawler.getConf().setExecuteInterval(200);
        crawler.start(4);

    }

}
