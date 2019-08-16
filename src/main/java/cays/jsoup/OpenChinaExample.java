package cays.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 开源中国数据提取
 *
 * @author Chai yansheng
 * @create 2019-08-16 13:51
 **/
public class OpenChinaExample {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";

    /**
     * 获取开源中国上的软件的基本信息
     * @throws IOException
     */
    public void execute() throws IOException {
        // 填写链接到集合中
        Set<String> setUrls = new HashSet<>();
        for(int i = 1; i <= 5; i++) {
            String strUrl = "https://www.oschina.net/project/list?company=0&sort=score&lang=0&recommend=false&p="+i;
            setUrls.add(strUrl);
        }
        // 根据软件的标题访问软件详情页
        Set<String> setProjUrls = new HashSet<>();
        for(String stringUrl : setUrls) {
            Document document = Jsoup.connect(stringUrl)
                    .userAgent(USER_AGENT)
                    .get();
            //  获取软件标题的url
            Elements elements = document.select("div.item");
            for(Element element : elements) {
                Elements eleUrl = element.select("div.content a");
                String strPrjUrl = eleUrl.attr("href");
                if (strPrjUrl.isEmpty()) {
                    continue;
                }
                Elements elName = eleUrl.select(".project-name");
                String name = elName.text();
                Elements elTitle = eleUrl.select(".project-title");
                String title = elTitle.text();
                if (name.isEmpty() || title.isEmpty()) {
                    continue;
                }
                setProjUrls.add(strPrjUrl);
                System.out.println(strPrjUrl);
                System.out.println("project-name: " + name);
                System.out.println("project-title: " + title);
            }
        }
        // 遍历软件url访问软件基本信息页
        for(String stringUrl : setProjUrls) {
            Document document = Jsoup.connect(stringUrl)
                    .userAgent(USER_AGENT)
                    .get();
            // 获取软件发布标题
            Elements elements = document.select("div.info-wrap h1");
            String strTitle = elements.text();
            System.out.println("标题：" + strTitle);
            // 获取软件的基本信息
            Elements elementsSection = document.select("div.info-item");
            for (Element element : elementsSection) {
                Elements label = element.select("label");
                Elements span = element.select("span");
                System.out.println(label.text() + span.text());
            }
            System.out.println("========================================================");

        }
    }
    public static void main(String[] args) throws IOException {
        OpenChinaExample openChinaExample = new OpenChinaExample();
        openChinaExample.execute();
    }
}
