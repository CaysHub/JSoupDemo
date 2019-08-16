# JSoupDemo
Java的Jsoup学习，参考教程和博客

# 一、Java的JSoup

## 0、Idea创建Maven项目

File->New->Project...->Maven...

## 1、引入JSoup支持

```xml
<!-- https://mvnrepository.com/artifact/org.jsoup/jsoup -->
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.12.1</version>
</dependency>
```

## 2、连接

```java
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
```

## 3、Jsoup入门

`Jsoup`是用于解析`HTML`，就类似`XML`解析器用于解析`XML`。 `Jsoup`它解析`HTML`成为真实世界的`HTML`。 它与`jquery`选择器的语法非常相似，并且非常灵活容易使用以获得所需的结果。

**1、能用`Jsoup`实现什么？**

- 从`URL`，文件或字符串中刮取并解析`HTML`
- 查找和提取数据，使用`DOM`遍历或`CSS`选择器
- 操纵`HTML`元素，属性和文本
- 根据安全的白名单清理用户提交的内容，以防止`XSS`攻击
- 输出整洁的`HTML`

**2、JSoup应用的主要类**

`org.jsoup.Jsoup`类

`Jsoup`类是任何`Jsoup`程序的入口点，并将提供从各种来源加载和解析`HTML`文档的方法。

`Jsoup`类的一些重要方法：

| 方法                                                        | 描述                                                         |
| ----------------------------------------------------------- | ------------------------------------------------------------ |
| `static Connection connect(String url)`                     | 创建并返回URL的连接。                                        |
| `static Document parse(File in, String charsetName)`        | 将指定的字符集文件解析成文档。                               |
| `static Document parse(String html)`                        | 将给定的html代码解析成文档。                                 |
| `static String clean(String bodyHtml, Whitelist whitelist)` | 从输入HTML返回安全的HTML，通过解析输入HTML并通过允许的标签和属性的白名单进行过滤。 |

`org.jsoup.nodes.Document`类

该类表示通过`Jsoup`库加载`HTML`文档。可以使用此类执行适用于整个`HTML`文档的操作。

`org.jsoup.nodes.Element`类

`HTML`元素是由标签名称，属性和子节点组成。 使用`Element`类，您可以提取数据，遍历节点和操作`HTML`。

**3、应用实例**

一些使用`Jsoup API`处理HTML文档的例子。

**载入文件**

从URL加载文档，使用`Jsoup.connect()`方法从`URL`加载`HTML`。

```java
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
```

**从文件加载文档**

使用`Jsoup.parse()`方法从文件加载HTML。

```java
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
```

**从String加载文档**

使用`Jsoup.parse()`方法从字符串加载HTML。

```java
    /**
     * 从string中解析document
     * @param html
     * @return
     */
    public Document getDocumentFromString(String html) {
         return Jsoup.parse(html);
    }
```

**4、获取Fav图标**

```java
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
    }
```

**5、获取网站所有链接**

```java
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
    }
```

**6、获取HTML页面中的所有图像**

```java
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
```

**7、获取URL的元信息**

元信息包括Google等搜索引擎用来确定网页内容的索引为目的。 它们以HTML页面的`HEAD`部分中的一些标签的形式存在。 

```java
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
```

**8、在`HTML`页面中获取表单属性**

在网页中获取表单输入元素非常简单。 使用唯一`id`查找`form`元素; 然后找到该表单中存在的所有`input`元素。

```java
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
```

**9、更新元素的属性/内容**

可以使用`Jsoup API`来更新这些元素的属性或`innerHTML`。 例如，想更新文档中存在的“`rel = nofollow`”的所有链接。

```java
Document document = getDocumentFromUrl(url);
Elements links = document.select("a[href]");  
links.attr("rel", "nofollow");
```

**10、消除不信任的`HTML`(以防止`XSS`)**

假设在应用程序中，想显示用户提交的`HTML`片段。 例如 用户可以在评论框中放入`HTML`内容。 这可能会导致非常严重的问题，如果允许直接显示此`HTML`。 用户可以在其中放入一些恶意脚本，并将用户重定向到另一个脏网站。

为了清理这个`HTML`，`Jsoup`提供`Jsoup.clean()`方法。 此方法期望`HTML`格式的字符串，并将返回清洁的`HTML`。 要执行此任务，`Jsoup`使用白名单过滤器。 `jsoup`白名单过滤器通过解析输入`HTML(`在安全的沙盒环境中)工作，然后遍历解析树，只允许将已知安全的标签和属性(和值)通过清理后输出。

它不使用正则表达式，这对于此任务是不合适的。

清洁器不仅用于避免`XSS`，还限制了用户可以提供的元素的范围：您可以使用文本，强元素，但不能构造`div`或表元素。

```java
    /**
     * 消除不信任的HTML(以防止XSS)
     * @param html
     */
    public void cleanXSS(String html) {
        String cleanHTML = Jsoup.clean(html, Whitelist.basic());
        System.out.println(cleanHTML);
        printLine();
    }
```

## 4、Jsoup的高级

### 1、Jsoup的打开方式

我们可以使用Connection对象来设置一些请求信息。比如：头信息，cookie，请求等待时间，代理等等来模拟浏览器的行为。

```java
Document doc = Jsoup.connect("http://example.com")
  .data("query", "Java")
  .userAgent("Mozilla")
  .cookie("auth", "token")
  .timeout(3000)
  .post();
```

### 2、使用document方式获取

```java
getElementById(String id)//通过id来获取
getElementsByTag(String tagName)//通过标签名字来获取
getElementsByClass(String className)//通过类名来获取
getElementsByAttribute(String key)//通过属性名字来获取
getElementsByAttributeValue(String key, String value)//通过指定的属性名字，属性值来获取
getAllElements()//获取所有元素
```

### 3、通过类似于css或jQuery的选择器来查找元素

1. `tagname` ： 通过标签查找元素，比如：`a`。

2. `ns|tag` ： 通过标签在命名空间查找元素，比如：可以用 `fb|name` 语法来查找 `<fb:name>` 元素。

3. `#id` ： 通过`id`查找元素，比如：`#logo`。

4. `.class`: 通过`class`名称查找元素，比如：`.masthead`。

5. `[attribute]`: 利用属性查找元素，比如：`[href]`。

6. `[^attr]`: 利用属性名前缀来查找元素，比如：可以用`[^data-]` 来查找带有`HTML5 Dataset`属性的元素。

7. `[attr=value]`: 利用属性值来查找元素，比如：`[width=500]`。

8. `[attr^=value]`, `[attr$=value]`,` [attr=value]`: 利用匹配属性值开头、结尾或包含属性值来查找元素，比如：`[href=/path/]`。

9. `[attr~=regex]`: 利用属性值匹配正则表达式来查找元素，比如：`img[src~=(?i).(png|jpe?g)]`。

10. `*`: 这个符号将匹配所有元素。

11. `el#id`: 元素+id，比如：`div#logo`。

12. `el.class`: 元素+class，比如：`div.masthead`。

13. `el[attr]`: 元素+class，比如：`a[href]`。

14. 任意组合，比如：`a[href].highlight`。

15. `ancestor child`: 查找某个元素下子元素，比如：可以用`.body p `查找在`<body>`元素下的所有 `<p>`元素。

16. `parent > child`: 查找某个父元素下的直接子元素，比如：可以用`div.content > p `查找 `<p>` 元素，也可以用`body > * `查找`body`标签下所有直接子元素。

17. `siblingA + siblingB`: 查找在A元素之前第一个同级元素B，比如：`div.head + div`。

18. `siblingA ~ siblingX`: 查找A元素之前的同级X元素，比如：`h1 ~ p`。

19. `el, el, el`:多个选择器组合，查找匹配任一选择器的唯一元素，例如：`div.masthead, div.logo`。
20. `:lt(n)`: 查找哪些元素的同级索引值（它的位置在DOM树中是相对于它的父节点）小于n，比如：`td:lt(3)` 表示小于三列的元素。
21. `:gt(n)`:查找哪些元素的同级索引值大于n，比如：`div p:gt(2)`表示哪些`<div>`中有包含2个以上的`<p>`元素。
22. `:eq(n)`: 查找哪些元素的同级索引值与n相等，比如：`form input:eq(1)`表示包含一个`<input>`标签的`<form>`元素。
23. `:has(seletor)`: 查找匹配选择器包含元素的元素，比如：`div:has(p)`表示哪些`<div>`包含了`<p>`元素。
24. `:not(selector)`: 查找与选择器不匹配的元素，比如：`div:not(.logo) `表示不包含` class="logo" `元素的所有`<div>` 列表。
25. `:contains(text)`: 查找包含给定文本的元素，搜索不区分大不写，比如：`p:contains(jsoup)`。
26. `:containsOwn(text)`: 查找直接包含给定文本的元素。
27. `:matches(regex)`: 查找哪些元素的文本匹配指定的正则表达式，比如：`div:matches((?i)login)`。
28. `:matchesOwn(regex)`: 查找自身包含文本匹配指定正则表达式的元素。

上述伪选择器索引是从0开始的，也就是说第一个元素索引值为0，第二个元素index为1等

通过上面的选择器，我们可以取得一个Elements对象，它继承了ArrayList对象，里面放的全是Element对象。接下来我们要做的就是从Element对象中，取出我们真正需要的内容。

通常有下面几种方法：

- `Element.text()`这个方法用来取得一个元素中的文本。

- `Element.html()`或`Node.outerHtml()`这个方法用来取得一个元素中的html内容。

- `Element.attr(String key)`获得一个属性的值，例如取得超链接`<a href="">`中`href`的值。

### 4、开源中国数据

```java
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
```

### 5、腾讯首页图片

```java
package cays.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * 腾讯首页图片数据
 *
 * @author Chai yansheng
 * @create 2019-08-16 14:39
 **/
public class QQImageExample {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";

    public void downloadImage(String filePath, String imageUrl) {
        // 若指定文件夹没有，则先创建
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 截取图片文件名
        String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1, imageUrl.length());

        try {
            // 文件名里面可能有中文或者空格，所以这里要进行处理。但空格又会被URLEncoder转义为加号
            String urlTail = URLEncoder.encode(fileName, "UTF-8");
            // 因此要将加号转化为UTF-8格式的%20
            imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf('/') + 1) + urlTail.replaceAll("\\+", "\\%20");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 写出的路径
        File file = new File(filePath + File.separator + fileName);

        try {
            // 获取图片URL
            URL url = new URL(imageUrl);
            // 获得连接
            URLConnection connection = url.openConnection();
            // 设置10秒的相应时间
            connection.setConnectTimeout(10 * 1000);
            // 获得输入流
            InputStream in = connection.getInputStream();
            // 获得输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            // 构建缓冲区
            byte[] buf = new byte[1024];
            int size;
            // 写入到文件
            while (-1 != (size = in.read(buf))) {
                out.write(buf, 0, size);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void execute(String url) {
        // 利用Jsoup获得连接
        Connection connect = Jsoup.connect(url);
        try {
            connect.userAgent(USER_AGENT);
            // 得到Document对象
            Document document = connect.get();
            // 查找所有img标签
            Elements imgs = document.getElementsByTag("img");
            System.out.println("共检测到下列图片URL：");
            System.out.println("开始下载");
            // 遍历img标签并获得src的属性
            for (Element element : imgs) {
                //获取每个img标签URL "abs:"表示绝对路径
                String imgSrc = element.attr("abs:src");
                // 打印URL
                System.out.println(imgSrc);
                //下载图片到本地
                downloadImage("src\\main\\java\\cays\\img\\", imgSrc);
            }
            System.out.println("下载完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        QQImageExample qqImageExample = new QQImageExample();
        String url = "http://www.qq.com";
        qqImageExample.execute(url);
    }
}
```

### 6、解析json数据

引入fastjson

```xml
<!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>fastjson</artifactId>
  <version>1.2.59</version>
</dependency>
```

代码

```java
package cays.jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * 解析Json数据
 *
 * @author Chai yansheng
 * @create 2019-08-16 14:52
 **/
public class ParseJsonExample {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";

    /**
     * 解析json数据
     * @param url
     */
    public void parseJson(String url) throws IOException {
        Connection.Response res = Jsoup.connect(url)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("User-Agent",USER_AGENT)
                .timeout(10000).ignoreContentType(true).execute();//.get();
        String body = res.body();
        System.out.println(body);
        JSONObject json = JSON.parseObject(body);
        JSONArray jsonArray = json.getJSONArray("data");

        //JSONArray jsonArray1 = JSONArray.parseArray(JSON_ARRAY_STR);//因为JSONArray继承了JSON，所以这样也是可以的

        //遍历方式1
        int size = jsonArray.size();
        for (int i = 0; i < size; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.containsKey("question")) {
                JSONObject question = jsonObject.getJSONObject("question");
                String qid = question.getString("qid");
                System.out.println(qid);
            }

        }
    }
    public static void main(String[] args) throws IOException {
        ParseJsonExample parseJsonExample = new ParseJsonExample();
        String url = "https://www.wukong.com/wenda/web/nativefeed/brow/?" +
                "concern_id=6300775428692904450&t=1533714730319&_signature=DKZ7mhAQV9JbkTPBachKdgyme4";
        parseJsonExample.parseJson(url);
    }
}
```

