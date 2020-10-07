/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler_1461390;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author 1461390
 */
public class MyCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp3|zip|gz))$");

    public static String storageFolder = "F:/crawler/";
    
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && href.startsWith("http://www.ics.uci.edu/");
    }
    
    @Override
     public void visit(Page page) {
         String url = page.getWebURL().getURL();
         System.out.println("URL: " + url);

         if (page.getParseData() instanceof HtmlParseData) {
             //Lấy thông tin trang web:
             HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
             String text = htmlParseData.getText();
             String html = htmlParseData.getHtml();
             
             String fileName =htmlParseData.getTitle();
             try {
                 //Ghi file text:
                 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(storageFolder + fileName + ".txt"),"UTF-8"));
                 bw.write(text);
                 bw.close();
                 
                 //Ghi file html:
                 bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(storageFolder + fileName + ".html"),"UTF-8"));
                 bw.write(html);
                 bw.close();
             } catch (FileNotFoundException ex) {
                 Logger.getLogger(MyCrawler.class.getName()).log(Level.SEVERE, null, ex);
             } catch (UnsupportedEncodingException ex) {
                 Logger.getLogger(MyCrawler.class.getName()).log(Level.SEVERE, null, ex);
             } catch (IOException ex) {
                 Logger.getLogger(MyCrawler.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
    }
}
