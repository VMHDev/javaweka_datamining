/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scraper_1461390;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author 1461390
 */
public class Scraper_1461390 {

    public static void main(String[] args) {
        try {
            //Đọc toàn bộ nội dung trang web:
            //Ref: https://viblo.asia/khiemnd5/posts/l0rvmxKaGyqA
            String fileName = "F:\\VnExpress.html";
            String content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);

            //Xử lý - ghi file:
            //Ref: http://giasutinhoc.vn/lap-trinh-java-co-ban/doc-va-ghi-file-trong-java-bai-5/
            Document doc = Jsoup.parse(content);
            Elements page_news = doc.getElementsByClass("list_news");

            File f = new File("F:\\news.txt");
            FileWriter fw = new FileWriter(f);

            for (Element list_news : page_news) {
                Elements news = list_news.children();
                for (Element li : news) {
                    Element h3 = li.getElementsByClass("title_news").first();
                    Element a = h3.getElementsByTag("a").first();
                    String title = a.text();
                    fw.write("Title: " + title + "\n");
                    Element news_lead = li.getElementsByClass("news_lead").first();
                    String news_content = news_lead.text();
                    fw.write("Text: " + news_content + "\n");
                }
            }
            
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Scraper_1461390.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
