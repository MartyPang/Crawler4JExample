package com.ecnu.psf;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.*;
import java.util.regex.Pattern;

/**
 * Created by Marty Pang on 2017/4/12.
 * Description
 */
public class MyCrawler extends WebCrawler {

    // 正则匹配指定的后缀文件
    private static final Pattern FILTERS = Pattern.compile(
            ".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|pdf" +
                    "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    private String[] myCrawlDomains; // 目标爬虫域名

    //private static String txtFile = "";

    /**
     * 爬虫实例启动前调用，一般是作用是初始化一些数据
     */
    @Override
    public void onStart() {
        myCrawlDomains = (String[]) myController.getCustomData(); // 获取自定义数据 目标爬虫域名  控制器类对象里会设置的
    }

    /**
     * 这个方法主要是决定哪些url我们需要抓取，返回true表示是我们需要的，返回false表示不是我们需要的Url
     * 第一个参数referringPage封装了当前爬取的页面信息
     * 第二个参数url封装了当前爬取的页面url信息
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase(); // 得到小写的url

        // 正则匹配，过掉一些指定后缀的文件
        if (FILTERS.matcher(href).matches()) {
            return false;
        }

        // 遍历目标域名 指定目标爬虫域名
        for (String crawlDomain : myCrawlDomains) {
            if (href.startsWith(crawlDomain)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 当我们爬到我们需要的页面，这个方法会被调用，我们可以尽情的处理这个页面
     * page参数封装了所有页面信息
     */
    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid(); // 获取docid url的唯一识别 类似主键
        String url = page.getWebURL().getURL(); // 获取url
        int parentDocid = page.getWebURL().getParentDocid(); // 获取上级页面的docId

        System.out.println("docId:"+docid);
        System.out.println("url:"+url);
        System.out.println("上级页面docId:"+parentDocid);

        if (page.getParseData() instanceof HtmlParseData) { // 判断是否是html数据
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData(); // 强制类型转换，获取html数据对象
            String text = htmlParseData.getText(); // 获取页面纯文本（无html标签）
            //text to file
            text2File(text);
            //txtFile += text;
//            String html = htmlParseData.getHtml(); // 获取页面Html
//            Set<WebURL> links = htmlParseData.getOutgoingUrls(); // 获取页面输出链接

//            System.out.println("纯文本长度: " + text.length());
//            System.out.println("html长度: " + html.length());
//            System.out.println("输出链接个数: " + links.size());
            //System.out.println(text.substring(0,150));
        }

        System.out.println("======================");
    }

    private void text2File(String text){
        byte[] bytestring = text.getBytes();

        try {
//            File f = new File("data/text/"+System.currentTimeMillis()+".txt");
//            if(!f.exists()){
//                f.createNewFile();
//            }
//            OutputStream os = new FileOutputStream(f);
//            os.write(bytestring);
//            os.flush();
//            os.close();
            File f = new File("data/text/crawlerdata.txt");
            if(!f.exists()){
                f.createNewFile();
            }
            FileWriter fw = new FileWriter("data/text/crawlerdata.txt",true);
            fw.write(text);
            fw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
