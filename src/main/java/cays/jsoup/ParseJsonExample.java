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
