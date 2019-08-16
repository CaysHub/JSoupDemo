package cays.biquge;

import cays.jsoup.QQImageExample;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 爬取笔趣阁小说的demo
 *
 * @author Chai yansheng
 * @create 2019-08-16 17:00
 **/
public class CrawlBiqugeTxtDemo {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";

    /**
     * 将txt保存到本地文件夹
     * @param fileName
     * @param text
     */
    public void writeStringToFile(String fileName, String text) {
        // 若指定文件夹没有，则先创建
        String dirStr = "src\\main\\java\\cays\\biquge\\txt\\";
        File dir = new File(dirStr);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dirStr + fileName + ".txt");
        // 如果文件不存在则创建文件
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("文件创建失败");
                e.printStackTrace();
            }
        }
        try {
            //true,则追加写入
            FileWriter resultFile = new FileWriter(file, true);
            PrintWriter myFile = new PrintWriter(resultFile);
            // 追加写入文件
            myFile.println(text);

            myFile.close();
            resultFile.close();
        } catch (IOException e) {
            System.err.println("写入文件失败");
            e.printStackTrace();
        }
    }

    /**
     * 获取网站document
     * @param url
     * @return
     */
    public Document getDocument(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .header("Content-Type", "application/json")
                    .header("User-Agent",USER_AGENT)
                    .timeout(10000).ignoreContentType(true).get();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * 解析每一章节小说
     * @param fileName 要写入到的文件名称
     * @param url 章节url
     */
    public void parseChapter(String fileName, String url) {
        Document document = getDocument(url);
        String chapterName, chapterContent;
        try {
            // 章节名称，www.xbiquge.la的标题select是 div.bookname > h1
            Elements bookName = document.select("div.bookname > h1");
            chapterName = bookName.text().trim();
            System.out.println(chapterName);
            // 章节内容，www.xbiquge.la的内容select是 div#content
            Elements bookContent = document.select("div#content");
            chapterContent = bookContent.text().replace(" ", "\n");
            chapterContent = chapterName + "\n" + chapterContent + "\n";
            // 将章节名称和章节内容同时写入文件
            writeStringToFile(fileName, chapterContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一本小说
     * @param fileName 要保存的小说名称
     * @param url 小说目录页面url
     */
    public void getText(String fileName, String url) {
        Document document = getDocument(url);
        // 目录地址，www.xbiquge.la的目录地址的select是 div#list a
        Elements chapterUrls = document.select("div#list a");
        List<String> urls = new ArrayList<>();
        for (Element chapterUrl : chapterUrls) {
            urls.add(chapterUrl.attr("abs:href"));
        }
        // 遍历目录地址解析每一章节
        for (String urlStr : urls) {
            parseChapter(fileName, urlStr);
        }
    }
    public static void main(String[] args) {
        CrawlBiqugeTxtDemo biqugeTxtDemo = new CrawlBiqugeTxtDemo();
        // 小说目录页面url
        String url = "http://www.xbiquge.la/10/10489/";
        biqugeTxtDemo.getText("三寸人间", url);
    }
}
