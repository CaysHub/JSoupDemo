package cays.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * 第一个JSoup程序
 *
 * @author Chai yansheng
 * @create 2019-08-16 9:27
 **/
public class FirstJSoupExample {
    /**
     * 连接百度输出网站名称
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Document document = Jsoup.connect("https://www.baidu.com").get();
        String title = document.title();
        System.out.println("title : " + title);
    }
}
