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
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cn.edu.hfut.dmic.webcollector.crawldb.DBManager;
import cn.edu.hfut.dmic.webcollector.crawler.Crawler;
import cn.edu.hfut.dmic.webcollector.fetcher.Executor;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.RocksDBManager;
import fund.jrj.com.xspider.service.ProblemResourceService;
import fund.jrj.com.xspider.utils.ExtractUtils;

/**
 * 本教程演示如何利用WebCollector爬取javascript生成的数据
 *
 * @author hu
 */
public class JRJWebkitCrawlerV2 {
	static List<String> seeds = null;
	public static volatile Map<String, Integer> urlProccessed = new ConcurrentHashMap<>();
	public static volatile Map<String, Integer> added = new ConcurrentHashMap<>();
	static File ALL_PAGE_LINKS_FILE = new File("cache/links");
	static {
		// 禁用Selenium的日志
		Logger logger = Logger.getLogger("com.gargoylesoftware.htmlunit");
		logger.setLevel(Level.OFF);

		try {
			seeds = FileUtils.readLines(new File(JRJWebkitCrawlerV2.class.getResource("").getPath() + "seeds6.txt"),
					"utf-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		Executor executor = new Executor() {
			@Override
			public void execute(CrawlDatum datum, CrawlDatums next) throws Exception {
				try {
					if (datum == null || datum.url() == null || datum.url() == "") {
						return;
					}
					if (urlProccessed.get(datum.url()) != null) {
						return;
					}
					urlProccessed.put(datum.url(), 1);
					System.out.println(datum.url());
					ProblemResourceService.findProblemResource(datum.url());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		// 创建一个基于伯克利DB的DBManager
		DBManager manager = new RocksDBManager("crawl");

		// 创建一个Crawler需要有DBManager和Executor

		Crawler crawler = new Crawler(manager, executor);
		for (String seed : seeds) {
			if (StringUtils.isNotBlank(seed)) {
				crawler.addSeed(seed.trim());
			}
		}
		crawler.setThreads(5);
		crawler.getConf().setExecuteInterval(200);
		crawler.start(1);
	}

}
