/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler_1461390;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 1461390
 */
public class Crawler_1461390 {

    public static void main(String[] args) {
        try {
            String crawlStorageFolder = "F:\\crawler";
            int numberOfCrawlers = 7;
            int MAX_DEPTH = 7;
            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(crawlStorageFolder);
            config.setMaxDepthOfCrawling(MAX_DEPTH);
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

            String seed = "vnexpress";
            controller.addSeed("http://" + seed + ".net/");
            MyCrawler.storageFolder = "F:/crawler/" + seed + "/";
            controller.start(MyCrawler.class, numberOfCrawlers);
        } catch (Exception ex) {
            Logger.getLogger(Crawler_1461390.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
