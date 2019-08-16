package cays.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

/**
 * 加载document的方式
 *
 * @author Chai yansheng
 * @create 2019-08-16 9:49
 **/
public class LoadDocumentUtil {
    /**
     * 从URL加载文档，使用Jsoup.connect()方法从URL加载HTML。
     * @param url
     * @return
     */
    public Document getDocumentFromUrl(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * 从Html文件解析Document
     * @param path 文件路径
     * @param encode 文件编码方式
     * @return
     */
    public Document getDocumentFromHtml(String path, String encode) {
        Document document = null;
        try {
            document = Jsoup.parse(new File(path), encode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * 从string中解析document
     * @param html
     * @return
     */
    public Document getDocumentFromString(String html) {
         return Jsoup.parse(html);
    }

    /**
     * 获取fav图标
     * @param url
     */
    public void getFavImg(String url) {
        String favImage = "Not Found!";
        Document document = getDocumentFromUrl(url);
        Element element = document.head().select("link[href~=.*\\.(ico|png)]").first();
        if (element == null) {
            element = document.head().select("meta[itemprop=image]").first();
            if (element != null) {
                favImage = element.attr("content");
            }
        } else {
            favImage = element.attr("href");
        }
        System.out.println(favImage);
        printLine();
    }

    /**
     * 获取网站所有连接
     * @param url
     */
    public void getAllUrls(String url) {
        Document document = getDocumentFromUrl(url);
        Elements links = document.select("a[href]");
        for (Element link : links) {
            System.out.println("link : " + link.attr("href"));
            System.out.println("text : " + link.text());
        }
        printLine();
    }

    /**
     * 获取网站所有图像
     * @param url
     */
    public void getAllImgs(String url) {
        Document document = getDocumentFromUrl(url);
        Elements images = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
        for (Element image : images) {
            System.out.println("src : " + image.attr("src"));
            System.out.println("height : " + image.attr("height"));
            System.out.println("width : " + image.attr("width"));
            System.out.println("alt : " + image.attr("alt"));
        }
        printLine();
    }

    /**
     * 获取网站元信息
     * @param url
     */
    public void getUrlInfo(String url) {
        Document document = getDocumentFromUrl(url);

        String description = document.select("meta[name=description]").get(0).attr("content");
        System.out.println("Meta description : " + description);

        String keywords = document.select("meta[name=keywords]").first().attr("content");
        System.out.println("Meta keyword : " + keywords);
        printLine();
    }

    /**
     * 获取表单元素
     * @param url
     * @param formId 表单id
     */
    public void getForm(String url, String formId) {
        Document doc = getDocumentFromUrl(url);
        Element formElement = doc.getElementById(formId);

        Elements inputElements = formElement.getElementsByTag("input");
        for (Element inputElement : inputElements) {
            String key = inputElement.attr("name");
            String value = inputElement.attr("value");
            System.out.println("Param name: " + key + "\r\nParam value: " + value);
        }
    }

    /**
     * 消除不信任的HTML(以防止XSS)
     * @param html
     */
    public void cleanXSS(String html) {
        String cleanHTML = Jsoup.clean(html, Whitelist.basic());
        System.out.println(cleanHTML);
        printLine();
    }
    /**
     * 输出一行等号
     */
    public void printLine() {
        System.out.println("======================================================");
    }
    public static void main(String[] args) {
        LoadDocumentUtil loadDocumentUtil = new LoadDocumentUtil();
        String url = "https://www.baidu.com";
        Document document = null;
        // 1. 从url解析document并输出标题
        document = loadDocumentUtil.getDocumentFromUrl(url);
        System.out.println("url  title : " + document.title());
        loadDocumentUtil.printLine();
        // 2. 从html文件解析document并输出标题
        String path = LoadDocumentUtil.class.getClassLoader().getResource("index.html").getPath();
        document = loadDocumentUtil.getDocumentFromHtml(path, "utf-8");
        System.out.println("html title : " + document.title());
        loadDocumentUtil.printLine();
        // 3. 从string解析document并输出标题
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>HelloWorld</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>第一个html</p>\n" +
                "</body>\n" +
                "</html>";
        document = loadDocumentUtil.getDocumentFromString(html);
        System.out.println("str  title : " + document.title());
        loadDocumentUtil.printLine();
        // 4. 获取Fav图标
        url = "https://www.baidu.com";
        loadDocumentUtil.getFavImg(url);
        // 5. 获取网站所有连接
        loadDocumentUtil.getAllUrls(url);
        // 6. 获取网站所有图像
        loadDocumentUtil.getAllImgs(url);
        // 7. 获取网站元信息
        url = "http://www.meituan.com";
        loadDocumentUtil.getUrlInfo(url);
        // 8. 消除不信任的HTML(以防止XSS)
        html = "<p><a href='http://www.yiibai.com/' onclick='sendCookiesToMe()'>Link</a></p>";
        loadDocumentUtil.cleanXSS(html);
    }
}
