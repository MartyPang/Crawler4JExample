package com.ecnu.psf;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * Created by Marty Pang on 2017/4/12.
 * Description
 */
public class MyCrawlController {
    public static void main(String[] args) throws Exception {

        String crawlStorageFolder = "data/crawl"; // 定义爬虫数据存储位置

        CrawlConfig config1 = new CrawlConfig(); // 爬虫配置实例1

        // 两个爬虫实例文件分别存储各种文件夹
        config1.setCrawlStorageFolder(crawlStorageFolder);

        config1.setPolitenessDelay(1000); // 设置1秒爬取一次

        config1.setMaxPagesToFetch(5); // 设置最大爬取页数5

        // 使用两个PageFetcher实例
        PageFetcher pageFetcher1 = new PageFetcher(config1);

        // 使用同一个RobotstxtServer实例
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher1);

        CrawlController controller1 = new CrawlController(config1, pageFetcher1, robotstxtServer);

        // 分别指定目标爬虫域名
        String[] crawler1Domains = {"https://en.wikipedia.org/"};

        // 设置自定义数据
        controller1.setCustomData(crawler1Domains);

        // 配置爬虫种子页面，就是规定的从哪里开始爬，可以配置多个种子页面
        controller1.addSeed("https://en.wikipedia.org/");
        controller1.addSeed("https://en.wikipedia.org/wiki/Artificial_intelligence");

        // 启动爬虫，爬虫从此刻开始执行爬虫任务，根据以上配置  根据源码  这种启动是无阻塞的
        controller1.startNonBlocking(MyCrawler.class, 5);

        controller1.waitUntilFinish(); // 直到完成
        System.out.println("爬虫1任务结束");
    }
}
